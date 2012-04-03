package org.kolmas.mirror;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.kolmas.mirror.container.Container;
import org.kolmas.mirror.exception.MirrorCantFindMethodException;
import org.kolmas.mirror.exception.MirrorGetterException;
import org.kolmas.mirror.exception.MirrorSetterException;
import org.kolmas.mirror.mock.container.CustomMethodCont;
import org.kolmas.mirror.mock.targetobj.NoGettersAndSetters;
import org.kolmas.mirror.mock.targetobj.TestDifferentMethodsExcep;

public class ExceptionsTest {

    private Container mock = mock(Container.class);
    private Mirror mirror;

    @Test(expected = NullPointerException.class)
    public void noContainer() {
        mirror = new DirectMirror(this);
        mirror.store(null);
    }
    
    @Test(expected = NullPointerException.class)
    public void noContainerSet() {
        mirror = new DirectMirror(this);
        mirror.setContainer(null);
    }


    @Test(expected = NullPointerException.class)
    public void noTarget() {
        mirror = new DirectMirror();
        mirror.store(mock);
    }
    
    @Test(expected = NullPointerException.class)
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
        mirror.retrieve(mock);
    }

    @Test(expected = MirrorCantFindMethodException.class)
    public void customMethodsStore() {
        mirror = new DirectMirror(new TestDifferentMethodsExcep());
        mirror.store(new CustomMethodCont());
    }
    @Test(expected = MirrorCantFindMethodException.class)
    public void customMethodsFetch() {
        mirror = new DirectMirror(new TestDifferentMethodsExcep());
        mirror.retrieve(new CustomMethodCont());
    }
}

