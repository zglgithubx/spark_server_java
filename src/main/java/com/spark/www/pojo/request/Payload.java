package com.spark.www.pojo.request;

import com.google.gson.JsonObject;

public class Payload {
    private JsonObject choices;
    private JsonObject usage;

    public JsonObject getChoices() {
        return choices;
    }

    public JsonObject getUsage() {
        return usage;
    }
}
