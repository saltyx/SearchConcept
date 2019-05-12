package me.search.concept.util.http;

import com.alibaba.fastjson.JSONObject;
import me.search.concept.model.BaseConcept;
import me.search.concept.util.ParseUtil;
import me.search.concept.util.SQLiteUtil;
import me.search.concept.util.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public enum ApiHttpClient implements ApiHttpClientInterface {

    INSTANCE {
        @Override
        public void getTokenRequest(String mobile, String pwd, ApiCallable<String> callable) throws Exception {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            JSONObject jsonObject = new JSONObject(true);
            jsonObject.put("mob", mobile);
            jsonObject.put("pwd", pwd);
            jsonObject.put("method", "get_token");

            httpClient.execute(ApiHttpClient.buildPost(jsonObject.toJSONString())
                    , (HttpResponse httpResponse) -> {
                int status = httpResponse.getStatusLine().getStatusCode();
                if (status >=200 && status < 300) {
                    String result = EntityUtils.toString(httpResponse.getEntity());
                    if (StringUtils.isEmpty(result) || result.startsWith("error")) {
                        callable.onInvalid("认证失败");
                    } else {
                        callable.onSuccess(result);
                    }
                } else {
                    callable.onFail(String.format("status : %d", status));
                }
                return null;
            });
        }

        @Override
        public void getConcepts(String token, ApiCallable<List<BaseConcept>> callable) throws IOException {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("method", "get_concepts");
            jsonObject.put("token", token);

            httpClient.execute(ApiHttpClient.buildPost(jsonObject.toJSONString()),
                (HttpResponse httpResponse) -> {
                    int status = httpResponse.getStatusLine().getStatusCode();
                    if (status >=200 && status < 300) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        List<BaseConcept> baseConcepts = ParseUtil.parseBaseConcept(result);
                        if (baseConcepts == null) {
                            callable.onInvalid(null);
                        } else {
                            callable.onSuccess(baseConcepts);
                        }
                    } else {
                        callable.onFail(null);
                    }
                    return null;
                });
        }

        @Override
        public void getConceptStocks(String token, String conceptCode, ApiCallable<List<String>> callable) throws IOException {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("method", "get_concept_stocks");
            jsonObject.put("token", token);
            jsonObject.put("code", conceptCode);
            jsonObject.put("data", SQLiteUtil.getToday());

            httpClient.execute(ApiHttpClient.buildPost(jsonObject.toJSONString()),
                    (HttpResponse httpResponse) -> {
                        int status = httpResponse.getStatusLine().getStatusCode();
                        if (status >=200 && status < 300) {
                            String result = EntityUtils.toString(httpResponse.getEntity());
                            List<String> stockCodes = ParseUtil.parseStockCode(result);
                            if (stockCodes == null) {
                                callable.onInvalid(null);
                            } else {
                                callable.onSuccess(stockCodes);
                            }
                        } else {
                            callable.onFail(null);
                        }
                        return null;
                    });
        }
    };

    private static HttpPost buildPost(String json) {
        HttpPost post = new HttpPost("https://dataapi.joinquant.com/apis");
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(6000).setConnectTimeout(6000).build();
        post.setConfig(requestConfig);
        post.setHeader("Content-Type","application/json");

        StringEntity entity = new StringEntity(json, "UTF-8");
        entity.setContentEncoding("UTF-8");
        post.setEntity(entity);

        return post;
    }

    public static ApiHttpClient getInstance() {
        return ApiHttpClient.INSTANCE;
    }

    public interface ApiCallable<V> {
        void onSuccess(V value);
        void onFail(V value);
        void onInvalid(V value);
    }

}
