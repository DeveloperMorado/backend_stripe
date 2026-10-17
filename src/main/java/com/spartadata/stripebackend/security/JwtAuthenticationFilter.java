//package com.spartadata.stripebackend.security;
//
//import com.nimbusds.jose.JWSHeader;
//import com.nimbusds.jose.JWSVerifier;
//import com.nimbusds.jose.crypto.RSASSAVerifier;
//import com.nimbusds.jose.jwk.JWK;
//import com.nimbusds.jose.jwk.JWKSet;
//import com.nimbusds.jwt.SignedJWT;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.security.PublicKey;
//import java.security.interfaces.RSAKey;
//import java.security.interfaces.RSAPublicKey;
//import java.text.ParseException;
//import java.util.Collections;
//import java.util.List;
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//    private JwtTokenProvider jwtTokenProvider;
//    private UserDetailsService userDetailsService;
//
//    private final JWKSet jwkSet;
//
////    @Value("${jwt.secret}")
//    private String secretKey;
//
//    private static final String jwksUrl = "https://cognito-idp.us-east-2.amazonaws.com/us-east-2_uBvyqBeAx";
//
//    public JwtAuthenticationFilter(JWKSet jwkSet) throws IOException, ParseException, ParseException {
//        this.jwkSet = JWKSet.load(new URL(jwksUrl));
//    }
//
////    @Override
////    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
////            throws ServletException, IOException, ServletException, IOException {
////
////        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
////        if (authHeader != null && authHeader.startsWith("Bearer ")) {
////            String token = authHeader.substring(7);
////
////            try {
////                SignedJWT signedJWT = SignedJWT.parse(token);
////                JWSHeader header = signedJWT.getHeader();
////                JWK jwk = jwkSet.getKeyByKeyId(header.getKeyID());
////
////                if (jwk == null) return;
////
////
////                RSAPublicKey publicKey = ((RSAKey) jwk).toPublicKey();
////
////                JWSVerifier verifier = new RSASSAVerifier(publicKey);
////
////                if (!signedJWT.verify(verifier)) return;
////
////                Claims claims = Jwts.parserBuilder()
////                        .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
////                        .build()
////                        .parseClaimsJws(token)
////                        .getBody();
////
////                String username = claims.getSubject();
////
////                UsernamePasswordAuthenticationToken authentication =
////                        new UsernamePasswordAuthenticationToken(username, null, List.of());
////
////                SecurityContextHolder.getContext().setAuthentication(authentication);
////
////            } catch (JwtException e) {
////                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
////                return;
////            } catch (ParseException e) {
////                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Parsing token");
////                return;
////            }
////        }
////
////        filterChain.doFilter(request, response);
////    }
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        // Get the JWT token from the Authorization header
//        String token = jwtTokenProvider.resolveToken(request);
//
//        // Check if the token is valid
//        if (token != null && jwtTokenProvider.validateToken(token)) {
//
//            // Get the user details from the token
//            UserDetails userDetails = userDetailsService.loadUserByUsername(
//                    jwtTokenProvider.getUsername(token));
//            // Create an authentication object and set it in the security context
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                    userDetails, null, userDetails.getAuthorities());
//            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
