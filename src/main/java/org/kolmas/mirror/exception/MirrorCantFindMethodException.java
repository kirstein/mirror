package org.kolmas.mirror.exception;

public class MirrorCantFindMethodException extends RuntimeException {

    private static final long serialVersionUID = -5189097233456742912L;
    public static final String METHOD_NOT_FOUND = "Container method with @ContainMethod annotation and store or receive parameter was not found.";
    public static final String STORE_METHOD_NOT_ACCEPTABLE = "Container method with @ContainMethod(store=\"%s\") annotation has invalid parameters or is not public. Method must be public void and its parameters must be 'methodName(String key, Class<?> type, Object result)'";
    public static final String RETRIEVE_METHOD_NOT_ACCEPTABLE = "Container method with @ContainMethod(retrieve=\"%s\") annotation has invalid parameters or is not public. Method must be public object and its parameters must be 'methodName(String key)'";
    public MirrorCantFindMethodException(String ex, String methodname, Throwable e) {
        super(String.format(ex, methodname), e);
    }
    public MirrorCantFindMethodException(String methodname) {
        super(String.format(METHOD_NOT_FOUND, methodname));
    }
}
