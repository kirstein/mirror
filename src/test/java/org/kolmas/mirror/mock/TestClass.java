package org.kolmas.mirror.mock;

import java.io.File;

import org.kolmas.mirror.annotation.Contain;

public class TestClass {

    @Contain
    private String string;
    @Contain
    private File file;
    
    public String getString() {
        return string;
    }
    public void setString(String string) {
        this.string = string;
    }
    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }
}