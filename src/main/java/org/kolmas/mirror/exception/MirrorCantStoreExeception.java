package org.kolmas.mirror.exception;

public class MirrorCantStoreExeception extends RuntimeException {

    private static final long serialVersionUID = 4077541875636937155L;
    public static final String CANT_STORE_DATA = "Cant store data to Container.";
    
    public MirrorCantStoreExeception(Throwable e) {
        super(CANT_STORE_DATA, e);
    }
    public MirrorCantStoreExeception() {
        super(CANT_STORE_DATA);
    }
}
