package org.kolmas.mirror.exception;

public class MirrorGetterException extends RuntimeException {

    public static final String GETTER_NOT_FOUND = "Get method missing. Make sure all fields annotated with @Contain are private and have get methods.";
    private static final long serialVersionUID = -4422078612363526217L;
    
    public MirrorGetterException(Throwable e) {
        super(GETTER_NOT_FOUND, e);
    }
}
