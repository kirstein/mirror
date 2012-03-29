package org.kolmas.mirror.mock;

import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.kolmas.mirror.container.Container;

public class TestContainer implements Container {

    private Map<String, Object> mockHolder;

    public TestContainer() {
        mockHolder = new HashMap<String, Object>();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mockHolder == null) ? 0 : mockHolder.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        TestContainer other = (TestContainer) obj;
        if (mockHolder == null) {
            if (other.mockHolder != null) return false;
        } else if (!mockHolder.equals(other.mockHolder)) return false;
        return true;
    }

    public void store(String fieldName, Class<?> type, Object result) {
        assertNotNull(type);
        assertNotNull(fieldName);
        mockHolder.put(fieldName, result);
    }

    public Object retrieve(String fieldName) {
        assertNotNull(fieldName);
        return mockHolder.get(fieldName);
    }
    
    public void storeCollection(String fieldName, Class<?> type, Collection<?> result) {
        
    }
    
    public Object retrieveCollection(String fieldName) {
        return null;
    }
}