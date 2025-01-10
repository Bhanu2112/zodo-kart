package com.zodo.kart.controller;

import com.zodo.kart.service.users.OAuth2Service;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Author : Bhanu prasad
 */

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class OAuth2Controller {



    private final OAuth2Service oAuth2Service;

//    @PostMapping("/auth/google")
//    public Map<String, Object> getGoogleInfo(OAuth2AuthenticationToken authentication) {
//        // Retrieve user details from the authentication token
//        Map<String, Object> userDetails = authentication.getPrincipal().getAttributes();
//
//        // You can now register or log in the user based on the retrieved details
//        System.out.println("User's details: " + userDetails);
//
//        // Fetch phone number if available
//        String phoneNumber = getPhoneNumber(authentication);
//        userDetails.put("phoneNumber", phoneNumber);
//
//        // Return some data to the frontend if necessary
//        return userDetails;
//    }
//
//    private String getPhoneNumber() {
//        // You may need to access Google API to get user phone number
//        // Check if the user's phone number is available in the user details
//       // String userId = authentication.getPrincipal().getAttribute("sub");
//        String userId = "111060044442419044989";
//
//        // Here, you will make an API call to get the user's phone number
//        String phoneNumberApiUrl = "https://people.googleapis.com/v1/people/" + "me" + "/phoneNumbers?personFields=phoneNumbers";
//
//        // Assuming you have the access token from the authentication object
//        //String accessToken = authentication.getAuthorizedClientRegistrationId(); // Update this line with the correct method to get the access token
//
//        String accessToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjVhYWZmNDdjMjFkMDZlMjY2Y2NlMzk1YjIxNDVjN2M2ZDQ3MzBlYTUiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI1NzY4ODEzNzkwNjktbzBhMGVkbHJyNThvMnZsYWNka2hkc2FyZ3ZyajBzNTkuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI1NzY4ODEzNzkwNjktbzBhMGVkbHJyNThvMnZsYWNka2hkc2FyZ3ZyajBzNTkuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTEwNjAwNDQ0NDI0MTkwNDQ5ODkiLCJlbWFpbCI6ImJoYW51cHJhc2FkYm9kYXNpbmdpMTIzNEBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmJmIjoxNzI3MzIyODc5LCJuYW1lIjoiQmhhbnUgUHJhc2FkIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hL0FDZzhvY0l0T1FNNXYyczZoMGtkNHM2S3JlS1cwLXJrLTFNZmc3d2ZUMGJoZTZ0dVlVcXdNSDQ9czk2LWMiLCJnaXZlbl9uYW1lIjoiQmhhbnUiLCJmYW1pbHlfbmFtZSI6IlByYXNhZCIsImlhdCI6MTcyNzMyMzE3OSwiZXhwIjoxNzI3MzI2Nzc5LCJqdGkiOiI1Yjk5NTQyZDMwMDg0MWNjZWI3YWUxM2JmNTRhZDQwZjZjMmY1MTQwIn0.DkJq-YGGqxVkwlo8BNVUK17Xq6UFcJT9PqC4U4Y3ycRCK3VQEvwg_P6iTlkM-Tri9rmdvC97-oFNKUqfXCvKXTy6-XDeNajlm5gjS-n3i0Dt_LsBrV-7Arw2dQpsBh9WlzCk-WiKVEh60h2R2eE_l0iheyMk3hLfKD6Rviwy70sL1VTjgqrfUbeYuzcAmsaC4UYO-WTM7XYUAZvtM84qJEDWpZjCZc8gjLcQDipVceOwAp8UlfNqldPIOyB9YE8HlrAKMAYQe81KzAgIMRIxt6bS6klhEATl3F2Su2El4LlbabzRD47Hrqy2kTRereKag1DCX28568w2dIvRzd9iSw";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        ResponseEntity<Map> response = restTemplate.exchange(phoneNumberApiUrl, HttpMethod.GET, entity, Map.class);
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            List<Map<String, String>> phoneNumbers = (List<Map<String, String>>) response.getBody().get("phoneNumbers");
//            if (phoneNumbers != null && !phoneNumbers.isEmpty()) {
//                return phoneNumbers.get(0).get("value"); // Return the first phone number
//            }
//        }
//        return null; // Return null if phone number is not found
//    }

    @PostMapping("/authgoogle")
    public String grantCode(@RequestParam("code") String code, @RequestParam("scope") String scope, @RequestParam("authuser") String authUser, @RequestParam("prompt") String prompt) {

        System.out.println(code);
        System.out.println();
        System.out.println(scope);
        System.out.println();
        System.out.println(authUser);
        System.out.println();
        System.out.println(prompt);
        return null;
    }

    @PostMapping("/auth/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody Map<String, String> googleClaims, HttpServletResponse response) {
        // OAuth2AuthenticationService authService = new OAuth2AuthenticationService();
        // return authService.createOAuth2AuthenticationFromClaims(googleClaims);

        //      String phone = getPhoneNumber();
        return ResponseEntity.ok(oAuth2Service.customerOAuth2SignOn(googleClaims,response));
    }

}
