package org.kolmas.mirror.exception;

public class MirrorGetterException extends RuntimeException {

    public static final String GETTER_NOT_FOUND = "Get method for '%s' not found.";
    public static final String GETTER_NOT_FOUND_NO_NAME = "Get method missing";
    private static final long serialVersionUID = -4422078612363526217L;

    public MirrorGetterException(String fieldName, Throwable e) {
        super(String.format(GETTER_NOT_FOUND, fieldName), e);
    }
    
    public MirrorGetterException(Throwable e) {
        super(GETTER_NOT_FOUND_NO_NAME, e);
    }
}
