package com.an.demo.services;

import com.an.demo.models.ResponseModel;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by knguy on 7/16/2017.
 */

public class APIService {
    private final String WEBAPP_URL = "http://128.199.90.168:8000";

    private final String DETECT_URL = WEBAPP_URL + "/detect";

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

    private byte[] getFileBytes(File file) throws Exception {
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
        buf.read(bytes, 0, bytes.length);
        buf.close();
        return bytes;
    }

    private Header[] getCookies() throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(WEBAPP_URL);
        HttpResponse response = httpClient.execute(httpGet);
        Header[] cookie = response.getHeaders("Cookie");
        return cookie;
    }

    public void sendRequest(String filePath) throws Exception {
        result = null;
        File file = new File(filePath);
        byte[] data = getFileBytes(file);
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(DETECT_URL);
        ByteArrayBody bab = new ByteArrayBody(data, file.getName());
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        reqEntity.addPart("image", bab);
        httpPost.setEntity(reqEntity);
        HttpResponse response = httpClient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() == SUCCESS_CODE) {
            parseResponseData(response, WEBAPP_URL);
        }
    }

    public void sendDemoRequest() throws Exception {
        result = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(DEMO_URL);
        HttpResponse response = httpClient.execute(httpGet);
        parseResponseData(response, WEBAPP_URL);
    }

    private void parseResponseData(HttpResponse response, String webUrl) throws Exception {
        String responseStr = EntityUtils.toString(response.getEntity());
        JSONObject jsonResult = new JSONObject(responseStr);
        String url = null;
        if (jsonResult.has("url")) {
            url = webUrl + jsonResult.getString("url");
        }
        Integer code = null;
        if (jsonResult.has("code")) {
            code = jsonResult.getInt("code");
        }
        Integer num = null;
        if (jsonResult.has("num")) {
            num = jsonResult.getInt("num");
        }
        JSONArray coordinates = null;
        if (jsonResult.has("coordinates")) {
            coordinates = jsonResult.getJSONArray("coordinates");
        }
        result = new ResponseModel(url, code, num, coordinates);
    }

    public ResponseModel getResult() {
        return result;
    }
}
