package com.system.started.rest.controller.error;

//@ControllerAdvice
public class RestErrorHandler {
 
//    @ExceptionHandler(value = Exception.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ResponseBody
    public RestErrorResponse errorResponse(Exception exception) {
        return new RestErrorResponse(exception.getMessage());
    }
 
}