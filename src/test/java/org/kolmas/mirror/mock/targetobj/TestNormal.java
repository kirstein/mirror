package org.kolmas.mirror.mock.targetobj;

import org.kolmas.mirror.annotation.Contain;

public class TestNormal {

    @Contain
    private String string;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
    
}