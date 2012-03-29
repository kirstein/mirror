package org.kolmas.mirror.exception;

public class MirrorSetterException extends RuntimeException{

    public static final String SETTER_NOT_FOUND = "Set method missing. Make sure all fields annotated with @Contain are private and have set methods.";
    private static final long serialVersionUID = -4422078612363526217L;

    public MirrorSetterException(Throwable e) {
        super(SETTER_NOT_FOUND, e);
    }

}
