package org.kolmas.mirror.exception;

public class MirrorTargetNullException extends RuntimeException {

    private static final long serialVersionUID = -3973417452034131151L;
    public static final String TARGET_NULL = "Target class cant be null";
    public MirrorTargetNullException() {
        super(TARGET_NULL);
    }
}
