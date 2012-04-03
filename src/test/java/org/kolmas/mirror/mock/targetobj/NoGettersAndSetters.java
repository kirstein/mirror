package org.kolmas.mirror.mock.targetobj;

import org.kolmas.mirror.annotation.Contain;

public class NoGettersAndSetters {
    @Contain
    private String exception;

    private String getException() {
        return exception;
    }

    private void setException(String exception) {
        this.exception = exception;
    }
    
  
}
