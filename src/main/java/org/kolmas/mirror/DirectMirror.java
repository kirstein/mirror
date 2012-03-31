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
import org.kolmas.mirror.exception.MirrorGetterException;
import org.kolmas.mirror.exception.MirrorSetterException;

/**
 * <p>
 * Mirror gathers all fields on target class that are annotated by
 * {@link Contain} annotation. After gathering fields it will copy data of those
 * fields into given {@link Container}.
 * </p>
 * 
 * Given container must use {@link Container} interface.
 * 
 * @author Mikk Kirstein
 * @see Mirror
 * 
 */
public class DirectMirror implements Mirror {

    public static final String CONTAINER_NULL = "Container cannot be null.";
    public static final String TARGET_NULL = "Target class cannot be null.";

    private Object target;
    private Container container;
    private List<Field> annotatedFields;
    private List<Method> annotatedMethods;
    private Contain fieldAnnotation;
    private Class<? extends Container> containerClass;
    private Class<?> targetClass;

    public DirectMirror() {
    }

    /**
     * Sets target object
     * 
     * @see #setTarget(Object)
     * @param target
     *            target object. Class from where all fields that are annotated
     *            by {@link Contain} annotation will be saved.
     * @throws NullPointerException
     *             Optional exception. Will be thrown when target object is
     *             null.
     */
    public DirectMirror(final Object target) throws NullPointerException {
        setTarget(target);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.kolmas.mirror.Mirror#setTarget(java.lang.Object)
     */
    public void setTarget(final Object target) throws NullPointerException {
        if (target == null) {
            throw new NullPointerException(TARGET_NULL);
        }
        if (this.targetClass != target.getClass()) {
            getTargetAnnotations(target);
        }

        this.target = target;
        this.targetClass = target.getClass();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.kolmas.mirror.Mirror#setContainer(org.kolmas.mirror.container.Container
     * )
     */
    public void setContainer(final Container container) throws NullPointerException {
        if (container == null) {
            throw new NullPointerException(CONTAINER_NULL);
        }

        if (container.getClass() != this.containerClass) {
            getContainerAnnotations(container);
        }

        this.containerClass = container.getClass();
        this.container = container;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.kolmas.mirror.Mirror#from(org.kolmas.mirror.container.Container)
     */
    public void retrieve(Container container) throws NullPointerException, MirrorSetterException,
            MirrorCantFindMethodException {
        setContainer(container);
        setTarget(this.target);
        try {
            for (Field field : annotatedFields) {
                fieldAnnotation = field.getAnnotation(Contain.class);
                String storageName = (fieldAnnotation.name().equals(Mirror.NOT_DEFINED) ? field.getName()
                        : fieldAnnotation.name());
                Object result = retrieveFieldValues(storageName);
                setFieldData(field, result);
            }
        } catch (MirrorCantFindMethodException e) {
            throw e;
        } catch (MirrorSetterException e) {
            throw e;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.kolmas.mirror.Mirror#to(org.kolmas.mirror.container.Container)
     */
    public Container store(final Container container) throws NullPointerException, MirrorGetterException,
            MirrorCantFindMethodException {
        setContainer(container);
        setTarget(this.target);
        try {
            for (Field field : annotatedFields) {
                fieldAnnotation = field.getAnnotation(Contain.class);

                Method getMethod = getGetMethod(field);
                Object result = getObjectFromTarget(getMethod);
                String storageName = (fieldAnnotation.name().equals(Mirror.NOT_DEFINED) ? field.getName()
                        : fieldAnnotation.name());
                storeFieldValues(field, storageName, result);
            }
        } catch (MirrorCantFindMethodException e) {
            throw e;
        } catch (Exception e) {
            throw new MirrorGetterException(e);
        }
        return container;
    }

    /**
     * <p>
     * Stores field values to {@link Container}. Will check if field has
     * {@link Contain#method()} annotation.
     * </p>
     * 
     * <p>
     * If {@link Contain#method()} annotation is present then will try to store
     * data in {@link Container} calling method that is annotated with
     * {@link ContainMethod} annotation that has the same
     * {@link ContainMethod#store()} value.
     * <p>
     * if there are no such methods or method cannot be invoked, then it will
     * throw {@link MirrorCantFindMethodException}
     * </p>
     * </p>
     * 
     * @param field
     *            Given field what to store
     * @param storageName
     *            Key for storing value
     * @param result
     *            fields value
     */
    private void storeFieldValues(Field field, String storageName, Object result) throws MirrorCantFindMethodException {
        Class<?> type = field.getType();
        String storeMethodName = fieldAnnotation.method();
        if (storeMethodName.equals(Mirror.NOT_DEFINED)) {
            container.store(storageName, type, result);
        } else {
            Method method = getMethodWithAnnotation(storeMethodName, true);
            try {
                method.invoke(container, storageName, type, result);
            } catch (Exception e) {
                throw new MirrorCantFindMethodException(MirrorCantFindMethodException.STORE_METHOD_NOT_ACCEPTABLE,
                        storeMethodName, e);
            }
        }
    }

    /**
     * Gets method from annotatedMethods list what has given
     * {@link ContainMethod} store() or retrieve() value.
     * 
     * @param methodName
     *            store() or retrieve() value to search for.
     * @param storage
     *            <ul>
     *            <li>true - searching for store() methods</li>
     *            <li>false - seraching for retrieve() methods.</li>
     *            </ul>
     * @return found method
     * @throws MirrorCantFindMethodException
     *             if no method with given annotation can be found.
     */
    private Method getMethodWithAnnotation(String methodName, Boolean storage) throws MirrorCantFindMethodException {
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

    /**
     * Invokes get method
     * 
     * @param getMethod
     *            getter method
     * @return value from method
     * 
     * @throws MirrorGetterException
     *             when getter method cannot be invoked
     */
    private Object getObjectFromTarget(Method getMethod) throws MirrorGetterException {
        Object result = null;
        try {
            result = getMethod.invoke(target);
        } catch (Exception e) {
            throw new MirrorGetterException(e);
        }
        return result;
    }

    /**
     * <p>
     * Retrieves field values from {@link Container}. Checks if field has set
     * {@link Contain#method()} value. If the value is set will try retrieve
     * value from {@link Container} method which has been annotated with
     * {@link ContainMethod#retrieve()} annotation and which retrieve value is
     * the same as {@link Contain#method()}
     * </p>
     * 
     * @param storageName
     *            key for storage
     * @return value gotten from {@link Container}
     * @throws MirrorCantFindMethodException
     *             when method cannot be found or invoked
     */
    private Object retrieveFieldValues(String storageName) throws MirrorCantFindMethodException {
        String retrieveMethodName = fieldAnnotation.method();
        if (retrieveMethodName.equals(Mirror.NOT_DEFINED)) {
            return container.retrieve(storageName);
        } else {
            Method method = getMethodWithAnnotation(retrieveMethodName, false);
            try {
                return method.invoke(container, storageName);
            } catch (Exception e) {
                throw new MirrorCantFindMethodException(MirrorCantFindMethodException.RETRIEVE_METHOD_NOT_ACCEPTABLE,
                        retrieveMethodName, e);
            }
        }
    }

    /**
     * <p>
     * Sets field with data gotten from {@link Container}.
     * </p>
     * <p>
     * Checks if field is nullable. If field is not nullable and data gotten
     * from {@link Container} is null will not call setter method.
     * </p>
     * 
     * @param field
     *            field which data to set.
     * @param result
     *            data what to set as field values.
     * @throws MirrorSetterException
     *             Will be thrown when fields setter method cannot be found or
     *             invoked.
     */
    private void setFieldData(Field field, Object result) throws MirrorSetterException {
        try {
            Method setMethod = getSetterGetter(field);
            if (result != null || (result == null && fieldAnnotation.nullable())) {
                setMethod.invoke(target, result);
            }
        } catch (Exception e) {
            throw new MirrorSetterException(e);
        }
    }

    /**
     * Gets setter method for a field.
     * 
     * @param field
     *            field which method to find
     * @return setter method if there is one.
     * @throws IntrospectionException
     *             when given method cannot be found.
     */
    private Method getSetterGetter(Field field) throws IntrospectionException {
        Method setMethod = new PropertyDescriptor(field.getName(), target.getClass()).getWriteMethod();
        return setMethod;
    }

    /**
     * Gets getter method for a field.
     * 
     * @param field
     *            field which method to find
     * @return getter method if there is one.
     * @throws IntrospectionException
     *             when given method cannot be found.
     */
    private Method getGetMethod(Field field) throws IntrospectionException {
        Method getMethod = new PropertyDescriptor(field.getName(), target.getClass()).getReadMethod();
        return getMethod;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.kolmas.mirror.Mirror#retrieve()
     */
    public void retrieve() {
        retrieve(container);
    }

    /**
     * <p>
     * Goes through target class and searches for all fields that are annotated
     * with {@link Contain} annotation. Saves found annotations in
     * annotatedFields list.
     * </p>
     * 
     * @param target
     */
    private void getTargetAnnotations(Object target) {
        annotatedFields = new ArrayList<Field>();
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Contain.class)) {
                annotatedFields.add(field);
            }
        }
    }

    /**
     * <p>
     * Goes through {@link Container} class and searches for all methods that
     * are annotated with {@link ContainMethod} annotation. Saves found methods
     * in annotatedMethods list.
     * </p>
     * <p>
     * Only searches for annotations with either {@link ContainMethod#store()}
     * or {@link ContainMethod#retrieve()} name set.
     * </p>
     * 
     * @param container
     *            Container to search.
     */
    private void getContainerAnnotations(Container container) {
        annotatedMethods = new ArrayList<Method>();
        Method[] methods = container.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(ContainMethod.class)) {
                ContainMethod annotation = method.getAnnotation(ContainMethod.class);
                if (!annotation.store().equals(Mirror.NOT_DEFINED) || !annotation.retrieve().equals(Mirror.NOT_DEFINED)) {
                    annotatedMethods.add(method);
                }
            }
        }
    }
}
