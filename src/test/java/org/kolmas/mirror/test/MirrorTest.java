package org.kolmas.mirror.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.kolmas.mirror.DirectMirror;
import org.kolmas.mirror.Mirror;
import org.kolmas.mirror.container.Container;
import org.kolmas.mirror.exception.MirrorGetterException;
import org.kolmas.mirror.exception.MirrorSetterException;
import org.kolmas.mirror.mock.TestClass;
import org.kolmas.mirror.mock.TestContainer;

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
        testClass.setEmptyString(null);
        testClass.setFile(testFile);
        testClass.setString(testString);
        to();
    }

    @Test
    public void retreive() {
        store();
        from();
        assertEquals(testClass.getFile(), testFile);
        assertEquals(testClass.getString(), testString);
    }

    @Test
    public void nullable() {
        final String val = "not empty";
        to();
        testClass.setNotNullable(val);
        assertNotNull(testClass.getNotNullable());
        assertNull(testContainer.retrieve("notNullable"));
        from();
        assertNotNull(testClass.getNotNullable());
        assertEquals(testClass.getNotNullable(), val);
    }

    @Test
    public void to() {
        Exception caught = null;
        try {
            Container container = mirror.to(testContainer);
            assertEquals(container, testContainer);
        } catch (MirrorGetterException e) {
            caught = e;
        } catch (Exception e) {
            fail(e.toString());
        }
        assertNotNull(caught);
        assertSame(MirrorGetterException.class, caught.getClass());
    }

    @Test
    public void from() {
        Exception caught = null;
        try {
            mirror.from(testContainer);
        } catch (MirrorSetterException e) {
            caught = e;
        } catch (Exception e) {
            fail(e.toString());
        }
        assertNotNull(caught);
        assertSame(MirrorSetterException.class, caught.getClass());
    }
    
}
