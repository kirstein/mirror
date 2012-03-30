package org.kolmas.mirror.exception;

public class MirrorContainerNullException extends RuntimeException {

    private static final long serialVersionUID = 5795162897663594498L;
    public static final String CONTAINER_NOT_FOUND = "Mirror container can't be null";
    public MirrorContainerNullException() {
        super(CONTAINER_NOT_FOUND);
    }
}
