package org.kolmas.mirror.container;

/**
 * 
 * @author Mikk Kirstein
 * 
 */
public interface Container {
    /**
     * Data store method.
     * 
     * @param fieldName
     *            name from where data came from.
     * @param type
     *            type of data
     * @param result
     *            result
     */
    void store(String fieldName, Class<?> type, Object result);

    /**
     * Asks for data.
     * 
     * @param fieldName
     *            Field name
     * @return Object result.
     */
    Object retrieve(String fieldName);

}
