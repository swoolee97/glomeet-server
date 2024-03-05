package com.example.glomeet.response;

public class Response {
    public String message;
    private Object data;

    public Response(String message) {
        this.message = message;
    }
    
    public Response(String message, Object data) {
        this.message = message;
        this.data = data;
    }

}
