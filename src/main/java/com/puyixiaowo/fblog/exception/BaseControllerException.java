package com.puyixiaowo.fblog.exception;

/**
 * @author Moses
 * @date 2017-09-03 22:28
 */
public class BaseControllerException extends RuntimeException {
    public BaseControllerException() {
    }

    public BaseControllerException(String message) {
        super(message);
    }
}
