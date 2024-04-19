package com.shapesynergy.dietworkout.diet.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class DietAuthentication {
    private final String consumerKey;
    private final String consumerSecret;
    private final RestTemplate restTemplate;
    private final static String BASE_URL = "https://platform.fatsecret.com/rest/server.api";

    public DietAuthentication(@Value("${fatsecret.api.consumerKey}") String consumerKey, @Value("${fatsecret.api.consumerSecret}") String consumerSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.restTemplate = new RestTemplate();
    }

    public String generateSignature(String method, String url, long timestamp, String nonce) throws NoSuchAlgorithmException, InvalidKeyException {
        String baseString = method.toUpperCase() + "&" + url + "&" + "oauth_consumer_key=" + consumerKey + "&" +
                "oauth_nonce=" + nonce + "&" +
                "oauth_signature_method=HMAC-SHA1&" +
                "oauth_timestamp=" + timestamp + "&" +
                "oauth_version=1.0";
        String signingKey = consumerSecret + "&";
        SecretKeySpec keySpec = new SecretKeySpec(signingKey.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(keySpec);
        byte[] result = mac.doFinal(baseString.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(result);
    }

    private String generateNonce() {
        byte[] nonceBytes = new byte[16];
        new SecureRandom().nextBytes(nonceBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(nonceBytes);
    }

    public HttpHeaders createHeaders(String method, String url) throws NoSuchAlgorithmException, InvalidKeyException {
        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = generateNonce();
        String signature = generateSignature(method, url, timestamp, nonce);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "OAuth oauth_consumer_key=\"" + consumerKey + "\", " +
                "oauth_nonce=\"" + nonce + "\", " +
                "oauth_signature=\"" + encode(signature) + "\", " +
                "oauth_signature_method=\"HMAC-SHA1\", " +
                "oauth_timestamp=\"" + timestamp + "\", " +
                "oauth_version=\"1.0\"");
        return headers;
    }

    public ResponseEntity<String> makeRequest(String method, String url) throws NoSuchAlgorithmException, InvalidKeyException {
        HttpHeaders headers = createHeaders(method, url);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
