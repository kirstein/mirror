package org.kolmas.mirror.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.kolmas.mirror.DirectMirror;
import org.kolmas.mirror.Mirror;
import org.kolmas.mirror.container.Container;
import org.kolmas.mirror.mock.container.CustomMethodCont;
import org.kolmas.mirror.mock.targetobj.TestDifferentMethods;
import org.kolmas.mirror.mock.targetobj.TestNormal;
import org.kolmas.mirror.mock.targetobj.TestNullable;

public class NormalTest {

    private TestNormal test;
    private Container mock = mock(Container.class);
    private Mirror mirror;
    private String testString;

    @Before
    public void setUp() {
        test = new TestNormal();
        mirror = new DirectMirror(test);
        testString = "mirror";
    }
    
    @Test
    public void noFields() {
        // Mirroring a class with no fields
        mirror = new DirectMirror(this);
        // No request should be happening
        verifyNoMoreInteractions(mock);
        mirror.store(mock);
    }
    
    @Test
    public void storeNull() {
        // Store value
        mirror.store(mock);
        verify(mock).store("string", String.class, null);
        
        // Rewrite value
        test.setString(testString);
        assertEquals(test.getString(), testString);
        
        // Retrieve value
        when(mock.retrieve("string")).thenReturn("changedvalue");
        mirror.retrieve(mock);
        
        // Check that values have been overridden
        assertEquals(test.getString(), "changedvalue");
        
        // Test last container
        test.setString("random");
        mirror.retrieve();
        assertEquals(test.getString(), "changedvalue");
    }    
    
    @Test
    public void nullableAndName() {
        TestNullable test = new TestNullable();
        mirror = new DirectMirror(test);
        
        // Test different value. Field name = nullable, store name = test
        test.setNullable("Value");
        mirror.store(mock);
        verify(mock).store("test", String.class, "Value");
                
        // Test nullable
        when(mock.retrieve("test")).thenReturn(null);
        mirror.retrieve(mock);
        assertEquals(test.getNullable(), "Value");
    }
    
    @Test
    public void differentMethodNames() {
        CustomMethodCont mock = new CustomMethodCont();
        TestDifferentMethods target = new TestDifferentMethods();
        mirror = new DirectMirror(target);
        // Test different store method
        mirror.store(mock);
        assertEquals(mock.key, "str");
        assertEquals(mock.type, String.class);
        assertEquals(mock.obj, null);
        
        // Rewrite data
        target.setStr("asd");
        assertEquals(target.getStr(), "asd");
        
        // Fetching from different method
        mirror.retrieve(mock);
        assertEquals(target.getStr(), "new data");
    }
 
    @Test
    public void setContainer() {
        TestNullable test = new TestNullable();
        mirror = new DirectMirror(test);
        mirror.setContainer(mock);
    }
    
    @Test
    public void changeContainer() {
        mirror.setContainer(mock);
        mirror.setContainer(new CustomMethodCont());
    }
}
