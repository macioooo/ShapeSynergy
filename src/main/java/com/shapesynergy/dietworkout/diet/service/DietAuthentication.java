package com.shapesynergy.dietworkout.diet.service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class DietAuthentication {
        private final String APP_KEY;
        private final String APP_SECRET;
        final static private String APP_URL = "https://platform.fatsecret.com/rest/server.api";
        final static private String APP_SIGNATURE_METHOD = "HmacSHA1";
        private static String HTTP_METHOD = "POST";
        public DietAuthentication(String APP_KEY, String APP_SECRET) {
            this.APP_KEY = APP_KEY;
            this.APP_SECRET = APP_SECRET;

        }
        public String nonce() {
            Random r = new Random();
            StringBuffer n = new StringBuffer();
            for (int i = 0; i < r.nextInt(8) + 2; i++) {
                n.append(r.nextInt(26) + 'a');
            }
            return n.toString();
        }

        public String[] generateOauthParams() {
            String[] a = {
                    "oauth_consumer_key=" + APP_KEY,
                    "oauth_signature_method=HMAC-SHA1",
                    "oauth_timestamp=" +  new Long(System.currentTimeMillis() / 1000).toString(),
                    "oauth_nonce=" + nonce(),
                    "oauth_version=1.0",
                    "format=json"
            };
            return a;
        }


        public String join(String[] params, String separator) {
            StringBuffer b = new StringBuffer();
            for (int i = 0; i < params.length; i++) {
                if (i > 0) {
                    b.append(separator);
                }
                b.append(params[i]);
            }
            return b.toString();
        }


        public String paramify(String[] params) {
            String[] p = Arrays.copyOf(params, params.length);
            Arrays.sort(p);
            return join(p, "&");
        }

        public String encode(String url) {
            if (url == null)
                return "";

            try {
                return URLEncoder.encode(url, "utf-8")
                        .replace("+", "%20")
                        .replace("!", "%21")
                        .replace("*", "%2A")
                        .replace("\\", "%27")
                        .replace("(", "%28")
                        .replace(")", "%29");
            }
            catch (UnsupportedEncodingException wow) {
                throw new RuntimeException(wow.getMessage(), wow);
            }
        }

        public String sign(String method, String uri, String[] params) throws UnsupportedEncodingException {
            try {
                String encodedURI = encode(uri);
                String encodedParams = encode(paramify(params));

                String[] p = {method, encodedURI, encodedParams};

                String text = join(p, "&");
                String key = APP_SECRET + "&";
                SecretKeySpec sk = new SecretKeySpec(key.getBytes(), APP_SIGNATURE_METHOD);
                Mac m = Mac.getInstance(APP_SIGNATURE_METHOD);
                m.init(sk);
                byte[] signatureBytes = m.doFinal(text.getBytes());

                return Base64.getEncoder().encodeToString(signatureBytes);
            } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                e.printStackTrace();
                return null;
            }
        }

        public String buildFoodsSearchUrl(String query, int pageNumber) throws Exception {
            List<String> params = new ArrayList<String>(Arrays.asList(generateOauthParams()));
            String[] template = new String[1];
            params.add("method=foods.search");
            params.add("max_results=50");
            params.add("page_number=" + pageNumber);
            params.add("search_expression=" + encode(query));
            params.add("oauth_signature=" + sign(HTTP_METHOD, APP_URL, params.toArray(template)));

            return APP_URL + "?" + paramify(params.toArray(template));
        }

        public String buildRecipesSearchUrl(String query, int pageNumber) throws Exception {
            List<String> params = new ArrayList<String>(Arrays.asList(generateOauthParams()));
            String[] template = new String[1];
            params.add("method=recipes.search");
            params.add("max_results=50");
            params.add("page_number=" + pageNumber);
            params.add("search_expression=" + encode(query));
            params.add("oauth_signature=" + sign(HTTP_METHOD, APP_URL, params.toArray(template)));

            return APP_URL + "?" + paramify(params.toArray(template));
        }

    }

