package com.shapesynergy.dietworkout.diet.service;

import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class DietAuthentication {
    private static final char[] HEX = "0123456789ABCDEF".toCharArray();


    private static final Set<Character> UnreservedChars = new HashSet<>(Arrays.asList(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '-', '_', '.', '~'));

    public String consumerSecret;

    public String method;

    public String parameterString;

    public Map<String, String> parameters = new LinkedHashMap<>();

    public String signature;

    public String signatureBaseString;

    public String signingKey;

    public String tokenSecret;

    public String url;

    public static String encodeURIComponent(String s) {
        StringBuilder o = new StringBuilder();
        for (byte b : s.getBytes(StandardCharsets.UTF_8)) {
            if (isSafe(b)) {
                o.append((char) b);
            } else {
                o.append('%');
                o.append(HEX[((b & 0xF0) >> 4)]);
                o.append(HEX[((b & 0x0F))]);
            }
        }
        return o.toString();
    }

    private static boolean isSafe(byte b) {
        return UnreservedChars.contains((char) b);
    }
    private String generateNonce() {
        byte[] nonceBytes = new byte[16]; // Adjust the length as needed
        new SecureRandom().nextBytes(nonceBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(nonceBytes);
    }

    public String build() {
        // For testing purposes, only add the timestamp if it has not yet been added
        if (!parameters.containsKey("oauth_timestamp")) {
            parameters.put("oauth_timestamp", "" + Instant.now().getEpochSecond());
        }

        // Boiler plate parameters
        //parameters.put("oauth_signature_method", "HMAC-SHA1");
        parameters.put("oauth_version", "1.0");
        parameters.put("oauth_nonce", generateNonce());

        // Build the parameter string after sorting the keys in lexicographic order per the OAuth v1 spec.
        parameterString = parameters.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> encodeURIComponent(e.getKey()) + "=" + encodeURIComponent(e.getValue()))
                .collect(Collectors.joining("&"));

        // Build the signature base string
        signatureBaseString = method.toUpperCase() + "&" + encodeURIComponent(url) + "&" + encodeURIComponent(parameterString);

        // If the signing key was not provided, build it by encoding the consumer secret + the token secret
        if (signingKey == null) {
            signingKey = encodeURIComponent(consumerSecret) + "&" + (tokenSecret == null ? "" : encodeURIComponent(tokenSecret));
        }

        // Sign the Signature Base String
        signature = generateSignature(signingKey, signatureBaseString);

        // Add the signature to be included in the header
        parameters.put("oauth_signature", signature);

        // Build the authorization header value using the order in which the parameters were added
        return "OAuth " + parameters.entrySet().stream()
                .map(e -> encodeURIComponent(e.getKey()) + "=\"" + encodeURIComponent(e.getValue()) + "\"")
                .collect(Collectors.joining(", "));
    }

    public String generateSignature(String secret, String message) {
        try {
            byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(bytes, "HmacSHA1"));
            byte[] result = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(result);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public DietAuthentication withConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
        return this;
    }

    public DietAuthentication withMethod(String method) {
        this.method = method;
        return this;
    }


    public DietAuthentication withParameter(String name, String value) {
        parameters.put(name, value);
        return this;
    }




    public DietAuthentication withURL(String url) {
        this.url = url;
        return this;
    }

}
