package me.search.concept.util.http;

import me.search.concept.model.BaseConcept;

import java.io.IOException;
import java.util.List;

public interface ApiHttpClientInterface {

    void getTokenRequest(String mobile, String pwd, ApiHttpClient.ApiCallable<String> callable) throws Exception;

    void getConcepts(String token, ApiHttpClient.ApiCallable<List<BaseConcept>> callable) throws IOException;

    void getConceptStocks(String token,String conceptCode, ApiHttpClient.ApiCallable<List<String>> callable) throws IOException;
}
