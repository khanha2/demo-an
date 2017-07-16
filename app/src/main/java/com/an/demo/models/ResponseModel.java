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

    public ResponseModel(String url, Integer code, Integer num, JSONArray coordinates) {
        this.url = url;
        this.code = code;
        this.num = num;
        this.coordinates = coordinates;
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
}
