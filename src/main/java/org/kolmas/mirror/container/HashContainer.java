package org.kolmas.mirror.container;

import java.util.HashMap;
import java.util.Map;


public class HashContainer implements Container {

    private Map<String, Object> map = new HashMap<String, Object>();
    
    public void store(String fieldName, Object result) {
        map.put(fieldName, result);
    }

    public Object retrieve(String fieldName) {
        return map.get(fieldName);
    }

}
