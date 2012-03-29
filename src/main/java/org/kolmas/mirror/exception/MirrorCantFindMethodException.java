package org.kolmas.mirror.exception;

public class MirrorCantFindMethodException extends RuntimeException {

    private static final long serialVersionUID = -5189097233456742912L;
    public static final String METHOD_NOT_FOUND = "Cant find needed method.";
    public MirrorCantFindMethodException(Throwable e) {
        super(METHOD_NOT_FOUND, e);
    }
}
