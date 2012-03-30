package org.kolmas.mirror;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.kolmas.mirror.annotation.Contain;
import org.kolmas.mirror.annotation.ContainMethod;
import org.kolmas.mirror.container.Container;
import org.kolmas.mirror.exception.MirrorCantFindMethodException;
import org.kolmas.mirror.exception.MirrorContainerNullException;
import org.kolmas.mirror.exception.MirrorGetterException;
import org.kolmas.mirror.exception.MirrorSetterException;
import org.kolmas.mirror.exception.MirrorTargetNullException;

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
    private Container container;
    private List<Field> annotatedFields;
    private List<Method> annotatedMethods;
    private Contain fieldAnnotation;

    public DirectMirror() {
    }

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
        if (target == null) {
            throw new MirrorTargetNullException();
        }
        this.target = target;
        annotatedFields = null;
        annotatedMethods = null;
    }

    public void setContainer(Container container) {
        if (container == null) {
            throw new MirrorContainerNullException();
        }
        this.container = container;
        getContainerAnnotations();
    }
    /*
     * (non-Javadoc)
     * 
     * @see org.kolmas.mirror.Mirror#to(org.kolmas.mirror.container.Container)
     */
    public Container store(Container container) {
        validateAndSetContainer(container);
        try {
            forcePrepare();
            for (Field field : annotatedFields) {
                fieldAnnotation = field.getAnnotation(Contain.class);

                Method getMethod = getGetMethod(field);
                Object result = getObjectFromTarget(getMethod, fieldAnnotation);
                String storageName = getStorageName(field.getName(), fieldAnnotation);
                store(field, storageName, result);
            }
        } catch(RuntimeException e) {
            throw e;
        }catch (Exception e) {
            throw new MirrorGetterException(e);
        }
        return container;
    }

    private void store(Field field, String storageName, Object result) {
        Class<?> type = field.getType();
        String storeMethodName = fieldAnnotation.method();
        if (storeMethodName.equals(Mirror.NOT_DEFINED)) {
            container.store(storageName, type, result);
        } else {
            Method method = getMethodWithAnnotation(storeMethodName, true);
            try {
                method.invoke(container, storageName, type, result);
            } catch (Exception e) {
                throw new MirrorCantFindMethodException(MirrorCantFindMethodException.STORE_METHOD_NOT_ACCEPTABLE, storeMethodName, e);
            }
        }
    }

    private Method getMethodWithAnnotation(String methodName, Boolean storage) {
        for (Method method : annotatedMethods) {
            ContainMethod ann = method.getAnnotation(ContainMethod.class);
            if (storage && ann.store().equals(methodName)) {
                return method;
            } else if (!storage && ann.retrieve().equals(methodName)) {
                return method;
            }
        }
        throw new MirrorCantFindMethodException(methodName);
    }
    
    private String getStorageName(String fieldName, Contain usedAnnotation) {
        String annotatedStorageName = usedAnnotation.name();
        return (annotatedStorageName.isEmpty() ? fieldName : annotatedStorageName);
    }

    private Object getObjectFromTarget(Method getMethod, Contain usedAnnotation) {
        Object result = null;
        try {
            result = getMethod.invoke(target);
        } catch (Exception e) {
            throw new MirrorGetterException(e);
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.kolmas.mirror.Mirror#from(org.kolmas.mirror.container.Container)
     */
    public void fetch(Container container) {
        validateAndSetContainer(container);
        forcePrepare();
        for (Field field : annotatedFields) {
            fieldAnnotation = field.getAnnotation(Contain.class);
            String storageName = getStorageName(field.getName(), fieldAnnotation);
            Object result = retrieve(field, storageName);
            setFieldData(field, result);
        }
    }

    private Object retrieve(Field field, String storageName) {
        String retrieveMethodName = fieldAnnotation.method();
        if (retrieveMethodName.equals(Mirror.NOT_DEFINED)) {
            return container.retrieve(storageName);
        } else {
            Method method = getMethodWithAnnotation(retrieveMethodName, false);
            try {
               return method.invoke(container, storageName);
            } catch (Exception e) {
                throw new MirrorCantFindMethodException(MirrorCantFindMethodException.RETRIEVE_METHOD_NOT_ACCEPTABLE, retrieveMethodName, e);
            }
        }
    }

    private void setFieldData(Field field, Object result) {
        try {
            Method setMethod = getSetMethod(field);
            if (result != null || (result == null && fieldAnnotation.nullable())) {
                setMethod.invoke(target, result);
            }
        } catch (Exception e) {
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

    private void validateAndSetContainer(Container container) {
        if (container == null) {
            throw new MirrorContainerNullException();
        }
        if (target == null) {
            throw new MirrorTargetNullException();
        }
        this.container = container;
    }

    /**
     * If Mirror has not gathered information about fields, then initiate
     * prepare method.
     */
    private void forcePrepare() {
        if (!isPrepared() && target != null) {
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
        if (target == null) {
            throw new MirrorTargetNullException();
        }
        if (container == null) {
            throw new MirrorContainerNullException();
        }
        
        getTargetAnnotations();
        getContainerAnnotations();
    }

    public void fetch() {
        fetch(container);
    }

    private void getTargetAnnotations() {
        annotatedFields = new ArrayList<Field>();
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Contain.class)) {
                annotatedFields.add(field);
            }
        }
    }

    private void getContainerAnnotations() {
        annotatedMethods = new ArrayList<Method>();
        Method[] methods = container.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(ContainMethod.class)) {
                annotatedMethods.add(method);
            }
        }
    }
}
