//package com.spartadata.stripebackend.service.impl;
//
//import com.spartadata.stripebackend.service.OnboardingService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.net.URI;
//import java.util.Map;
//
//@Service
//public class OnboardingServiceImpl implements OnboardingService {
//
////    @GetMapping("/return1")
////    public ResponseEntity<Map<String, String>> stripeReturn(
////            @RequestParam String token)  {
////        URI uri = URI.create("cognitopoc1://cognito1.spartadata.com/return");
////        //Your backend responds with a 302 redirect to myapp://stripe-return?..
////        return ResponseEntity.status(HttpStatus.FOUND).location(uri).build();
////    }
////
////    @GetMapping("/refresh")
////    public ResponseEntity<Map<String, String>> stripeRefresh()  {
////        URI uri = URI.create("cognitopoc1://cognito1.spartadata.com/refresh");
////        //Your backend responds with a 302 redirect to myapp://stripe-return?..
////        return ResponseEntity.status(HttpStatus.FOUND).location(uri).build();
////    }
//}
