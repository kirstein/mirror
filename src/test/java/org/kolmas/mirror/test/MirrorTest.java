package org.kolmas.mirror.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.kolmas.mirror.DirectMirror;
import org.kolmas.mirror.Mirror;
import org.kolmas.mirror.container.Container;
import org.kolmas.mirror.mock.TestClass;

public class MirrorTest {

    private Mirror mirror;
    private TestClass testClass;
    private TestContainer testContainer;
    
    private String testString;
    private File testFile;

    @Test
    @Before
    public void setup() {
        testContainer = new TestContainer();
        testClass = new TestClass();
        testString = new String();
        testFile = new File(testString);
        mirror = new DirectMirror(testClass);
    }

    @Test
    public void store() {
        testClass.setFile(testFile);
        testClass.setString(testString);
        mirror.to(testContainer);
    }
    
    @Test
    public void retreive() {
        store();
        mirror.from(testContainer);
        assertEquals(testClass.getFile(), testFile);
        assertEquals(testClass.getString(), testString);
    }

    class TestContainer implements Container {

        private Map<String, Object> mockHolder;
        
        public TestContainer() {
            mockHolder = new HashMap<String, Object>();
        }
        
        @Test
        public void store(String fieldName, Object result) {
            assertNotNull(fieldName);
            assertNotNull(result);
            mockHolder.put(fieldName, result);
        }

        @Test
        public Object retrieve(String fieldName) {
            assertNotNull(fieldName);
            Object obj = mockHolder.get(fieldName);
            assertNotNull(obj);
            
            if (obj instanceof String) {
                return obj;
            }
            if (obj instanceof File) {
                return obj;
            }
            fail();
            return null;
        }
    }
}
