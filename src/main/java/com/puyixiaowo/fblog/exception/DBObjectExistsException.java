package com.puyixiaowo.fblog.exception;

/**
 * @author feihong
 * @date 2017-08-09 22:15
 */
public class DBObjectExistsException extends RuntimeException {

    public DBObjectExistsException(String message) {
        super(message);
    }
}
