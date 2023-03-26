package com.udacity.jwdnd.course1.cloudstorage.exception;

public class FileAlreadyExitsException extends Exception {
    public FileAlreadyExitsException() {
        super();
    }

    public FileAlreadyExitsException(String message) {
        super(message);
    }
}
