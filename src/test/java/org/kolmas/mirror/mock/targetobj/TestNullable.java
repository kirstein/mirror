package org.kolmas.mirror.mock.targetobj;

import org.kolmas.mirror.annotation.Contain;

public class TestNullable {
    
    @Contain(name="test", nullable=false)
    private String nullable;

    public String getNullable() {
        return nullable;
    }

    public void setNullable(String nullable) {
        this.nullable = nullable;
    }
    

}
