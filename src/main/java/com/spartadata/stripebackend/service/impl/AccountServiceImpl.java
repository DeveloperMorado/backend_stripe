package com.spartadata.stripebackend.service.impl;

import com.spartadata.stripebackend.exception.CustomException;
import com.spartadata.stripebackend.model.AccountDTO;
import com.spartadata.stripebackend.model.UserAccountDTO;
import com.spartadata.stripebackend.repository.AccountRepository;
import com.spartadata.stripebackend.service.AccountService;
import com.spartadata.stripebackend.utils.UrlBuilder;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.Customer;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.CustomerCreateParams;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UrlBuilder urlBuilder;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.base-url-host}")
    private String host;

    @Value("${app.base-url-scheme}")
    private String scheme;

    public AccountServiceImpl(AccountRepository accountRepository, UrlBuilder urlBuilder) {
        this.accountRepository = accountRepository;
        this.urlBuilder = urlBuilder;
    }

    @Override
    public String sayHello() {
        return "Hello, world!";
    }

    public Customer create(AccountDTO accountDTO) throws StripeException {

        
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(accountDTO.getEmail())
                .setName(accountDTO.getName())
                .setAddress(CustomerCreateParams.Address.builder()
                        .setCity(accountDTO.getAddress().getCity())
                        .build())
                .build();

        Customer customer = Customer.create(params);

        System.out.println("Created customer: " + customer.getId());

        return customer;
    }

    @PostMapping("/create-account")
    public ResponseEntity<Map<String, String>> createConnectedAccount(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam String userId,
            @RequestParam String email) {

        Account account = null;
        Map<String, String> response = new HashMap<>();

        //String token = authorizationHeader.substring(7); // Remove "Bearer "
        String token = UUID.randomUUID().toString();

        try {
            if (userId == null || userId.isEmpty() || email == null || email.isEmpty()) {
                String errorMessage = "Invalid input data creating connected account.";
                ResponseEntity.badRequest().body(errorMessage);
            }

            //Get AccountId from DynamoDB
            UserAccountDTO userAccount = accountRepository.getUserAccount(userId);

            //String tokenValidationReturnRefresh = UUID.randomUUID().toString();

            if (userAccount == null) {
                account = createAccount(userId, email);

                if (account == null) {
                    //TODO Throw customer exception. When the account cannot retrieve if was saved
                    throw new CustomException(
                            "Error creating account",
                            "500",
                            "",
                            500) ;
                }

                accountRepository.saveAccount(account, userId, token);
            } else  {
                try {
                    //Get Account from Stripe
                    account = Account.retrieve(userAccount.getAccountId());

                    //Save a new token for validate return and refresh
                    accountRepository.saveAccount(account, userId, token);
                } catch (StripeException e) {
                    if (e.getCode().equals("account_invalid")) {
                        //Create a new account in Stripe
                        account = createAccount(userId, email);

                        //Delete Old account in DynamoDB
                        accountRepository.deleteAccount(userAccount, userId);

                        //Save new account in DynamoDB
                        accountRepository.saveAccount(account, userId, token);
                    }
                }

                if (account == null) {
                    //TODO Throw customer exception. When the account cannot retrieve if was saved
                    throw new CustomException(
                            "Error retrieving account",
                            "500",
                            "",
                           500) ;
                }
            }

            response.put("accountId", account.getId());

            //TODO Check by this status
            //account.getDetailsSubmitted()
            //account.getRequirements().getCurrentlyDue()

            if (account.getChargesEnabled()) {
                // We do not need to  create a new AccountLink and redirect user
                System.out.println("Account: " + account.getDetailsSubmitted());

                response.put("onboardingStatus", "FULLY_SETUP");
//                response.put("url", "");

                return ResponseEntity.ok(response);
            }

            AccountLink accountLink = createAccountLink(userId, account.getId(), token);
            response.put("url", accountLink.getUrl());
            response.put("onboardingStatus", "ON_PROCESS");

            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getCode());
            errorResponse.put("message", e.getMessage());

            if (e.getCode().equals("account_invalid")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(errorResponse);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getCode(), "message", e.getMessage()));
        }
    }

    public Account createAccount(String userId, String email) throws StripeException {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("user_id", userId);

        // create a New Account
        AccountCreateParams params = AccountCreateParams.builder()
                .setType(AccountCreateParams.Type.STANDARD)
                .setEmail(email)
                .putAllMetadata(metadata)
                .build();
        return Account.create(params);
    }

    @GetMapping("/resume")
    public void resumeOnboarding(
            @RequestParam String userId,
            HttpServletResponse response) throws IOException, StripeException {
        // Lookup user and get saved Stripe account ID
        //String accountId = userService.getStripeAccountIdForUser(userId);

        String accountId = "acct_123456";

        AccountLinkCreateParams params = AccountLinkCreateParams.builder()
                .setAccount(accountId)
                .setRefreshUrl("http://localhost:8080/api/account/resume?userId=" + userId)
                .setReturnUrl("https://your-server/stripe/success")
                .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                .build();

        AccountLink accountLink = AccountLink.create(params);
        response.sendRedirect(accountLink.getUrl());
    }

    private AccountLink createAccountLink(
            String userId,
            String  accountId,
            String token) throws StripeException {
        //String refreshURL = baseUrl + "/onboarding/stripeRefresh?token=" + token + "&userId=" + userId;
//        String returnURL = baseUrl + "/onboarding/stripeReturn?token=" + token + "&userId=" + userId;

        String refreshURL = UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(host)
                .path("/api/account/onboarding/refreshStripe")
//                .queryParam("token", token)
//                .queryParam("userId", userId)
                .toUriString();

        String returnURL = UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(host)
                .path("/api/account/onboarding/returnStripe")
//                .queryParam("token", token)
//                .queryParam("userId", userId)
                .toUriString();

        String temp = "http://ErickMorados-MacBook-Pro.local:8080/api/account/onboarding/returnStripe";

        returnURL = createUrlWithToken(temp, token, userId);

        temp = "http://ErickMorados-MacBook-Pro.local:8080/api/account/onboarding/refreshStripe";

        refreshURL = createUrlWithToken(temp, token, userId);

        AccountLinkCreateParams linkParams = AccountLinkCreateParams.builder()
                .setAccount(accountId)
                .setRefreshUrl(refreshURL)
                .setReturnUrl(returnURL)
//                .setReturnUrl("https://ced1-108-254-179-133.ngrok-free.app/api/account/return1&token=" + token)
//                .setRefreshUrl("http://192.168.2.27:8080/api/account/refresh?token=" + token)
//                .setReturnUrl("http://192.168.2.27:8080/api/account/return1?token=" + token)
//                .setReturnUrl("http://Ericks-MacBook-Pro-2.local:8080/api/account/onboarding/returnStripe&token=" + token)
//                .setRefreshUrl("http://Ericks-MacBook-Pro-2.local:8080/api/account/onboarding/refreshStripe")
                .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                .build();

        return AccountLink.create(linkParams);
    }

    public static String createUrlWithToken(String baseUrl, String token, String userId) {
        try {
            URI uri = new URI(baseUrl);
            String query = uri.getQuery();
            String newQuery = (query == null ? "" : query + "&") + "token=" + token;
            newQuery = newQuery + "&" + "userId=" + userId;

            URI newUri = new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), newQuery, uri.getFragment());
            return newUri.toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URL: " + baseUrl, e);
        }
    }

    @GetMapping("/refreshStripe")
    public ResponseEntity<Map<String, String>> refreshStripe(String userId, String token) {
        UserAccountDTO userAccount = accountRepository.getUserAccount(userId);
        if (userAccount.getToken().equals(token)) {
            URI uri = URI.create("cognitopoc1://cognito1.spartadata.com/refresh");
            //Your backend responds with a 302 redirect to myapp://stripe-return?..
            return ResponseEntity.status(HttpStatus.FOUND).location(uri).build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/ReturnStripe")
    public ResponseEntity<Map<String, String>> returnStripe(String userId, String token) {
        UserAccountDTO userAccount = accountRepository.getUserAccount(userId);
        if (userAccount.getToken().equals(token)) {
            URI uri = URI.create("cognitopoc1://cognito1.spartadata.com/return");
            //Your backend responds with a 302 redirect to myapp://stripe-return?..
            return ResponseEntity.status(HttpStatus.FOUND).location(uri).build();
        }
        return ResponseEntity.notFound().build();
    }
}
