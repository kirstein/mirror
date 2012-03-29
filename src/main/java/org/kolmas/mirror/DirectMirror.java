package org.kolmas.mirror;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.kolmas.mirror.annotation.Contain;
import org.kolmas.mirror.container.Container;
import org.kolmas.mirror.exception.MirrorCantFindMethodException;
import org.kolmas.mirror.exception.MirrorCantStoreExeception;
import org.kolmas.mirror.exception.MirrorGetterException;
import org.kolmas.mirror.exception.MirrorSetterException;

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
    private Contain annotation;
    private Container container;

    /**
     * @param target
     */
    public DirectMirror(Object target) {
        setTarget(target);
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
            this.container = container;
            for (Field field : annotatedFields) {
                annotation = field.getAnnotation(Contain.class);

                Method getMethod = getGetMethod(field);
                Object result = getObjectFromTarget(getMethod, annotation);
                String storageName = getStorageName(field.getName(), annotation);
                store(field, storageName, result);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            throw new MirrorGetterException(e);
        }
        return container;
    }

    private void store(Field field, String storageName, Object result) {

        try {
            Class<?> type = field.getType();
            if (!annotation.storeCollection()) {
                container.store(storageName, type, result);
            } else {
                Method method = getContainerMethod(annotation.setCollection(), String.class, Class.class, Collection.class);
                method.invoke(container, storageName, type, result);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Method getContainerMethod(String methodName, Class<?>... params) {
        try {
            return container.getClass().getDeclaredMethod(methodName, params);
        } catch (Exception e) {
            throw new MirrorCantFindMethodException(e);
        }
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
            this.container = container;
            for (Field field : annotatedFields) {
                annotation = field.getAnnotation(Contain.class);
                String storageName = getStorageName(field.getName(), annotation);
                Object result = retrieve(field, storageName);
                set(field, result);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private Object retrieve(Field field, String storageName) {
        Object ret = null;
        try {
            if (!annotation.storeCollection()) {
                return container.retrieve(storageName);
            } else {
                Method method = getContainerMethod(annotation.getCollection(), String.class);
                ret = method.invoke(container, storageName);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private void set(Field field, Object result) {
        try {
            Method setMethod = getSetMethod(field);
            if (result != null || (result == null && annotation.nullable())) {
                setMethod.invoke(target, result);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            throw new MirrorSetterException(e);
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
