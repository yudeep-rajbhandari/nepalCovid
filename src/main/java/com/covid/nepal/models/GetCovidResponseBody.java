package com.covid.nepal.models;

import org.json.JSONArray;
import org.springframework.http.HttpStatus;

public class GetCovidResponseBody {
    private JSONArray array;
    private HttpStatus status;
    private String message;

    public JSONArray getArray() {
        return array;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setArray(JSONArray array) {
        this.array = array;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
