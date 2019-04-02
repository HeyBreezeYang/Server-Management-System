package com.system.started.rest.controller.error;

import java.io.Serializable;

public class RestErrorResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	private String message;
    public RestErrorResponse(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
