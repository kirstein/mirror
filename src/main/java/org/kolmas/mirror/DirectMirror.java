package org.kolmas.mirror;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    private Map<Field, Method[]> annotatedFields;

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
            Iterator<Field> it = annotatedFields.keySet().iterator();
            while (it.hasNext()) {
                Field field = it.next();
                Contain usedAnnotation = field.getAnnotation(Contain.class);
                Method[] methods = annotatedFields.get(field);
                Method getMethod = methods[0];
                Object result = getObjectFromTarget(getMethod, usedAnnotation);
                String storageName = getStorageName(field.getName(), usedAnnotation);
                container.store(storageName, result);
            }
        } catch (IllegalArgumentException e) {
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
            Object initialResult = getMethod.invoke(target);
            String storageCommand = usedAnnotation.storageMethod();
            if (storageCommand.isEmpty()) return initialResult;
            Method method = initialResult.getClass().getMethod(storageCommand);
            result = method.invoke(initialResult);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
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
            Iterator<Field> it = annotatedFields.keySet().iterator();
            while (it.hasNext()) {
                Field field = it.next();
                Contain usedAnnotation = field.getAnnotation(Contain.class);
                Method[] methods = annotatedFields.get(field);
                Method setMethod = methods[1];
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
        }
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
        try {
            annotatedFields = new HashMap<Field, Method[]>();
            Field[] fields = target.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Contain.class)) {
                    String fieldName = field.getName();
                    Method getMethod = new PropertyDescriptor(fieldName, target.getClass()).getReadMethod();
                    Method setMethod = new PropertyDescriptor(fieldName, target.getClass()).getWriteMethod();
                    Method[] temp_methods = new Method[] { getMethod, setMethod };
                    annotatedFields.put(field, temp_methods);
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }
}
