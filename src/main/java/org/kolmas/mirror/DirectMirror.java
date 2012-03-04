package org.kolmas.mirror;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.kolmas.mirror.annotation.Contain;
import org.kolmas.mirror.container.Container;

/**
 * Mirror gathers all fields on target class that are annotated by Container
 * annotation. After gathering fields it will copy data of those fields into
 * given container.
 * 
 * Given container must use Container interface.
 * 
 * @author Mikk Kirstein
 * 
 */
public class DirectMirror implements Mirror {

    private Object target;
    private List<Field> annotatedFields;

    /**
     * @param target
     */
    public DirectMirror(Object target) {
        setTarget(target);
    }

    public DirectMirror() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.kolmas.mirror.Mirror#setTarget(java.lang.Object)
     */
    public void setTarget(Object target) {
        this.target = target;
        annotatedFields = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.kolmas.mirror.Mirror#to(org.kolmas.mirror.container.Container)
     */
    public Container to(Container container) {
        try {
            forcePrepare();
            for (Field field : annotatedFields) {
                Contain usedAnnotation = field.getAnnotation(Contain.class);
                Method getMethod = getGetMethod(field);
                Object result = getObjectFromTarget(getMethod, usedAnnotation);
                String storageName = getStorageName(field.getName(), usedAnnotation);
                container.store(storageName, result);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return container;
    }

    private String getStorageName(String fieldName, Contain usedAnnotation) {
        String annotatedStorageName = usedAnnotation.name();
        return (annotatedStorageName.isEmpty() ? fieldName : annotatedStorageName);
    }

    private Object getObjectFromTarget(Method getMethod, Contain usedAnnotation) {
        Object result = null;
        try {
            result = getMethod.invoke(target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.kolmas.mirror.Mirror#from(org.kolmas.mirror.container.Container)
     */
    public void from(Container container) {
        try {
            forcePrepare();
            for (Field field : annotatedFields) {
                Contain usedAnnotation = field.getAnnotation(Contain.class);
                Method setMethod = getSetMethod(field);
                String storageName = getStorageName(field.getName(), usedAnnotation);
                Object result = container.retrieve(storageName);
                
                setMethod.invoke(target, result);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }
    
    private Method getSetMethod(Field field) throws IntrospectionException {
        Method setMethod = new PropertyDescriptor(field.getName(), target.getClass()).getWriteMethod();
        return setMethod;
    }
    
    private Method getGetMethod(Field field) throws IntrospectionException {
        Method getMethod = new PropertyDescriptor(field.getName(), target.getClass()).getReadMethod();
        return getMethod;
    }

    /**
     * If Mirror has not gathered information about fields, then initiate
     * prepare method.
     */
    private void forcePrepare() {
        if (!isPrepared()) {
            prepare();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.kolmas.mirror.Mirror#isPrepared()
     */
    public boolean isPrepared() {
        return (annotatedFields != null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.kolmas.mirror.Mirror#prepare()
     */
    public void prepare() {
        annotatedFields = new ArrayList<Field>();
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Contain.class)) {
                annotatedFields.add(field);
            }
        }
    }
}
