package org.kolmas.mirror.mock.targetobj;

import org.kolmas.mirror.annotation.Contain;

public class TestDifferentMethods {
    
    @Contain(method = "differentMethodName")
    private String str;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

}
