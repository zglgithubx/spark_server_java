package com.spark.www.pojo.request;

import com.google.gson.JsonObject;

public class ResponseData {
    private JsonObject header;
    private  JsonObject payload;

    public JsonObject getHeader() {
        return header;
    }

    public JsonObject getPayload() {
        return payload;
    }
}
