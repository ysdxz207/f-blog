package com.puyixiaowo.fblog.exception;
/**
 * 
 * @author Moses
 * @date 2017-12-13
 * 
 */
public class TimeoutException extends RuntimeException {

    public TimeoutException() {
    }

    public TimeoutException(String message) {
        super(message);
    }
}
