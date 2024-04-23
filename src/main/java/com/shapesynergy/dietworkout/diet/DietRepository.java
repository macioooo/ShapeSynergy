package com.shapesynergy.dietworkout.diet;

import com.shapesynergy.dietworkout.diet.service.DietAuthentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Repository
public class DietRepository {
    private final String BASE_URL = "https://platform.fatsecret.com/rest/server.api";
    private final String CONSUMER_SECRET;
    //private DietAuthentication dietAuthentication;
    private final String CONSUMER_KEY;

    public DietRepository(@Value("${fatsecret.api.consumerKey}") String consumerKey,
                       @Value("${fatsecret.api.consumerSecret}") String consumerSecret) {
        this.CONSUMER_KEY = consumerKey;
        this.CONSUMER_SECRET = consumerSecret;

    }
    public String buildFoodsSearchUrl(String query, int pageNumber) throws Exception {
        DietAuthentication dietAuthentication = new DietAuthentication(CONSUMER_KEY, CONSUMER_SECRET);
        List<String> params = new ArrayList<String>(Arrays.asList(dietAuthentication.generateOauthParams()));
        String[] template = new String[1];
        params.add("method=foods.search");
        params.add("max_results=50");
        params.add("page_number=" + pageNumber);
        params.add("search_expression=" + dietAuthentication.encode(query));
        params.add("oauth_signature=" + dietAuthentication.sign("POST", BASE_URL, params.toArray(template)));

        return BASE_URL + "?" + dietAuthentication.paramify(params.toArray(template));
    }

    public String buildRecipesSearchUrl(String query, int pageNumber) throws Exception {
        DietAuthentication dietAuthentication = new DietAuthentication(CONSUMER_KEY, CONSUMER_SECRET);
        List<String> params = new ArrayList<String>(Arrays.asList(dietAuthentication.generateOauthParams()));
        String[] template = new String[1];
        params.add("method=recipes.search");
        params.add("max_results=50");
        params.add("page_number=" + pageNumber);
        params.add("search_expression=" + dietAuthentication.encode(query));
        params.add("oauth_signature=" + dietAuthentication.sign("POST", BASE_URL, params.toArray(template)));

        return BASE_URL + "?" + dietAuthentication.paramify(params.toArray(template));
    }
}
