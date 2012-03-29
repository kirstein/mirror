package org.kolmas.mirror.mock;

import java.io.File;
import java.util.List;

import org.kolmas.mirror.annotation.Contain;

public class TestClass {

    @Contain
    private String string;
    @Contain
    private File file;
    @Contain
    private String emptyString;
    @Contain(nullable = false)
    private String notNullable;
    @Contain(storeCollection=true)
    private List<TestClass> list;
    
    
    
    
    
    
    
    @Contain
    private String exception;
    
    public String getEmptyString() {
        return emptyString;
    }
    public void setEmptyString(String emptyString) {
        this.emptyString = emptyString;
    }
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
    public String getNotNullable() {
        return notNullable;
    }
    public void setNotNullable(String notNullable) {
        this.notNullable = notNullable;
    }
    public List<TestClass> getList() {
        return list;
    }
    public void setList(List<TestClass> list) {
        this.list = list;
    }
}