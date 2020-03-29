package com.covid.nepal.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

public class CovidResponseBody {

    private HttpStatus status;
    private Object body;
    /**
     * message String.
     */
    private String message;

    public Object getObj() {
        return body;
    }

    public void setObj(Object obj) {
        this.body = obj;
    }

    /**
     * @return status.
     */
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * @param status status.
     */
    public void setStatus(final HttpStatus status) {
        this.status = status;
    }

    /**
     * @return message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message message.
     */
    public void setMessage(final String message) {
        this.message = message;
    }

}
