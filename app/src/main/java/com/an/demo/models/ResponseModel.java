package com.an.demo.models;

import org.json.JSONArray;

/**
 * Created by knguy on 7/16/2017.
 */

public class ResponseModel {
    private final String url;

    private final Integer code;

    private final Integer num;

    private final JSONArray coordinates;

    private String message;

    public ResponseModel(String url, Integer code, Integer num, JSONArray coordinates) {
        this.url = url;
        this.code = code;
        this.num = num;
        this.coordinates = coordinates;
        switch (code) {
            case 0:
                break;
            case -1:
                message = "Request bằng phương thức không phải GET/POST";
                break;
            case -2:
                message = "Image URL lỗi (GET)";
                break;
            case -3:
                message = "Lỗi hệ thống";
                break;
            case -4:
                message = "Không detect được faces";
                break;
        }
    }

    public String getUrl() {
        return url;
    }

    public Integer getCode() {
        return code;
    }

    public Integer getNum() {
        return num;
    }

    public JSONArray getCoordinates() {
        return coordinates;
    }

    public String getMessage() {
        return message;
    }
}
