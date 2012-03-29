package org.kolmas.mirror.exception;

public class MirrorSetterException extends RuntimeException{

    public static final String SETTER_NOT_FOUND = "Set method for '%s' not found.";
    public static final String SETTER_NOT_FOUND_NO_NAME = "Get method missing";
    private static final long serialVersionUID = -4422078612363526217L;

    public MirrorSetterException(String fieldName, Throwable e) {
        super(String.format(SETTER_NOT_FOUND, fieldName), e);
    }
    
    public MirrorSetterException(Throwable e) {
        super(SETTER_NOT_FOUND_NO_NAME, e);
    }

}
