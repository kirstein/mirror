package org.kolmas.mirror.mock.container;

import org.kolmas.mirror.annotation.ContainMethod;
import org.kolmas.mirror.container.Container;

public class CustomMethodCont implements Container {

    public String key;
    public Class<?> type;
    public Object obj;

    @ContainMethod(store = "differentMethodName")
    public void customStoreMethod(String key, Class<?> type, Object obj) {
        this.key = key;
        this.type = type;
        this.obj = obj;
    }

    @ContainMethod(retrieve = "differentMethodName")
    public Object customRetreiveMethod(String key) {
        if (key.equals(this.key)) {
            return "new data";
        }
        throw new RuntimeException();
    }

    public void store(String fieldName, Class<?> type, Object result) {
        throw new RuntimeException();
    }

    public Object retrieve(String fieldName) {
        throw new RuntimeException();
    }
    
    @ContainMethod(store = "methodDoesNotExist", retrieve = "methodDoesNotExist")
    public void fail() {
        
    }
}
