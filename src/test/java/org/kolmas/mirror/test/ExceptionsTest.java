package org.kolmas.mirror.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.kolmas.mirror.DirectMirror;
import org.kolmas.mirror.Mirror;
import org.kolmas.mirror.container.Container;
import org.kolmas.mirror.exception.MirrorCantFindMethodException;
import org.kolmas.mirror.exception.MirrorContainerNullException;
import org.kolmas.mirror.exception.MirrorGetterException;
import org.kolmas.mirror.exception.MirrorSetterException;
import org.kolmas.mirror.exception.MirrorTargetNullException;
import org.kolmas.mirror.mock.container.CustomMethodCont;
import org.kolmas.mirror.mock.target.NoGettersAndSetters;
import org.kolmas.mirror.mock.target.TestDifferentMethodsExcep;

public class ExceptionsTest {

    private Container mock = mock(Container.class);
    private Mirror mirror;

    @Test(expected = MirrorContainerNullException.class)
    public void noContainer() {
        mirror = new DirectMirror(this);
        mirror.store(null);
    }
    
    @Test(expected = MirrorContainerNullException.class)
    public void noContainerSet() {
        mirror = new DirectMirror(this);
        mirror.setContainer(null);
    }


    @Test(expected = MirrorTargetNullException.class)
    public void noTarget() {
        mirror = new DirectMirror();
        mirror.store(mock);
    }
    
    @Test(expected = MirrorTargetNullException.class)
    public void setTargetNull() {
        mirror = new DirectMirror();
        mirror.setTarget(null);
    }

    @Test(expected = MirrorGetterException.class)
    public void noGetter() {
        mirror = new DirectMirror(new NoGettersAndSetters());
        mirror.store(mock);
    }
    
    @Test(expected = MirrorSetterException.class)
    public void noSetter() {
        mirror = new DirectMirror(new NoGettersAndSetters());
        when(mock.retrieve("exception")).thenReturn(null);
        mirror.fetch(mock);
    }

    @Test(expected = MirrorCantFindMethodException.class)
    public void customMethodsStore() {
        mirror = new DirectMirror(new TestDifferentMethodsExcep());
        mirror.store(new CustomMethodCont());
    }
    @Test(expected = MirrorCantFindMethodException.class)
    public void customMethodsFetch() {
        mirror = new DirectMirror(new TestDifferentMethodsExcep());
        mirror.fetch(new CustomMethodCont());
    }
    
    @Test(expected = MirrorTargetNullException.class)
    public void prepareNullTarget() {
        mirror = new DirectMirror();
        mirror.prepare();
    }
    @Test(expected = MirrorContainerNullException.class)
    public void prepareNullContainer() {
        mirror = new DirectMirror(this);
        mirror.prepare();
    }
}

