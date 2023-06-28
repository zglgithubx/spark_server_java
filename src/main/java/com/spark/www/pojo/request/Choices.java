package com.spark.www.pojo.request;

import com.google.gson.JsonArray;

public class Choices {
    private int status;
    private int seq;
    private JsonArray text;

    public int getStatus() {
        return status;
    }

    public int getSeq() {
        return seq;
    }

    public JsonArray getText() {
        return text;
    }
}
