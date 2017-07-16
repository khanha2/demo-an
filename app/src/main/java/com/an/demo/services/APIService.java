package com.an.demo.services;

import com.an.demo.models.ResponseModel;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by knguy on 7/16/2017.
 */

public class APIService {
    private final String WEBAPP_URL = "http://139.59.252.244:8000";

    private final String DETECT_URL = WEBAPP_URL + "/detect";

    private final String DEMO_WEBAPP_URL = "http://128.199.90.168:8000";

    private final String DEMO_URL = "http://128.199.90.168:8000/detect?url=https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTKMcaBnY3gJESrjkqYkqRIT5hCkP4syc5TfLqBJVp3vjlqFZv7Uw";

    private final int SUCCESS_CODE = 200;

    private ResponseModel result;

    private static APIService instance;

    public static APIService getInstance() {
        if (instance == null) {
            instance = new APIService();
        }
        return instance;
    }

    protected APIService() {
    }

    public void sendRequest(String filePath) throws Exception {
        result = null;
        File file = new File(filePath);
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(DETECT_URL);
        InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(file), -1);
        reqEntity.setContentType("binary/octet-stream");
        reqEntity.setChunked(true); // Send in multiple parts if needed
        httppost.setEntity(reqEntity);
        HttpResponse response = httpClient.execute(httppost);
        if (response.getStatusLine().getStatusCode() == SUCCESS_CODE) {
            parseResponseData(response, WEBAPP_URL);
        }
    }

    public void sendDemoRequest() throws Exception {
        result = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(DEMO_URL);
        HttpResponse response = httpClient.execute(httpGet);
        parseResponseData(response, DEMO_WEBAPP_URL);
    }

    private void parseResponseData(HttpResponse response, String webUrl) throws Exception {
        String json = IOUtils.toString(response.getEntity().getContent());
        JSONObject jsonResult = new JSONObject(json);
        String url = webUrl + jsonResult.getString("url");
        Integer code = jsonResult.getInt("code");
        Integer num = jsonResult.getInt("num");
        JSONArray coordinates = jsonResult.getJSONArray("coordinates");
        result = new ResponseModel(url, code, num, coordinates);
    }

    public ResponseModel getResult() {
        return result;
    }
}
