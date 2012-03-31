package org.kolmas.mirror;

import org.kolmas.mirror.annotation.Contain;
import org.kolmas.mirror.container.Container;
import org.kolmas.mirror.exception.MirrorCantFindMethodException;
import org.kolmas.mirror.exception.MirrorGetterException;
import org.kolmas.mirror.exception.MirrorSetterException;

/**
 * <p>
 * Mirror uses java reflections to search for all the fields in target class
 * {@link #setTarget(Object)} that are annotated with {@link Contain}
 * annotation.
 * </p>
 * 
 * <p>
 * Given fields will be stored and when {@link #store(Container)} method is
 * called fields data will be gathered and stored to {@link Container}
 * </p>
 * 
 * <p>
 * When {@link #retrieve()} or {@link #retrieve(Container)} method is called
 * then {@link Container#retrieve(String)} will be called. After that all mirror
 * will ask for {@link Container} for fields data. Data will be asked by field
 * names.
 * <p>
 * After that fields in target class will be filled by data given from {@link Container}
 * </p>
 * </p>
 * 
 * @author Mikk Kirstein
 * 
 * @see <a href="http://www.github.com/kirstein/mirror">Mirror project on
 *      github</a>
 */
public interface Mirror {

    public static final String NOT_DEFINED = "";

    /**
     * <p>
     * Stores data from Target class to given Container. Will search for all
     * fields annotated with {@link Contain} annotation.
     * </p>
     * 
     * <p>
     * Before using store method the target class must be defined. Defining
     * target class takes place within constructor or with
     * {@link #setTarget(Object)} method.
     * </p>
     * 
     * <p>
     * Given {@link Container} must not be null.
     * </p>
     * 
     * <p>
     * Target class must be a public class.
     * </p>
     * 
     * <p>
     * Uses getter method for given field. Getter method must be non-static and
     * public.
     * </p>
     * 
     * <p>
     * Field will be stored in {@link Container} using its
     * {@link Container#store(String, Class, Object)} method.
     * </p>
     * 
     * @param container
     *            {@link Container} to store values to. {@link Container} must
     *            not be null.
     * 
     * @return reference to container used for data transfer.
     * 
     * @throws NullPointerException
     *             Optional exception. Will be thrown when target object or
     *             {@link Container} is null.
     * @throws MirrorGetterException
     *             Optional exception. Will be thrown when getter method of a
     *             field cannot be accessed or is missing.
     * @throws MirrorCantFindMethodException
     *             Optional exception. Will be thrown when storage method cannot
     *             be accessed or is missing.
     */
    Container store(final Container container) throws NullPointerException, MirrorGetterException,
            MirrorCantFindMethodException;

    /**
     * <p>
     * Goes through all annotated fields in target class and retrieves data from
     * {@link Container} by their field names. Fills target object fields that
     * are annotated with {@link Contain} annotation.
     * </p>
     * 
     * <p>
     * Retrieve using {@link Container#retrieve(String)} method where given
     * String is fields key.
     * </p>
     * 
     * <p>
     * Before using retrieve method the target class must be defined. Defining
     * target class takes place within constructor or with
     * {@link #setTarget(Object)} method.
     * </p>
     * 
     * <p>
     * Given {@link Container} must not be null.
     * </p>
     * 
     * <p>
     * {@link Container} may be different than the container used to store
     * values to.
     * </p>
     * 
     * @param container
     *            {@link Container} from where to retrieve values from. Uses
     *            {@link Container#retrieve(String)} method.
     * @throws NullPointerException
     *             Optional exception. Will be thrown when {@link Container} or
     *             target object is null.
     * @throws MirrorSetterException
     *             Optional exception. Will be thrown when setter method of a
     *             field cannot be accessed or is missing.
     * @throws MirrorCantFindMethodException
     *             Optional exception. Will be thrown when retrieve method
     *             cannot be accessed or is missing.
     * 
     * @see #setContainer(Container)
     * @see #setTarget(Object)
     */
    void retrieve(final Container container) throws NullPointerException, MirrorSetterException,
            MirrorCantFindMethodException;

    /**
     * Retrieves data from last used Container.
     * 
     * @see #retrieve(Container)
     * @throws NullPointerException
     *             Optional exception. Will be thrown when {@link Container} or
     *             target object is null.
     * @throws MirrorSetterException
     *             Optional exception. Will be thrown when setter method of a
     *             field cannot be accessed or is missing.
     * @throws MirrorCantFindMethodException
     *             Optional exception. Will be thrown when retrieve method
     *             cannot be accessed or is missing.
     */
    void retrieve() throws NullPointerException, MirrorSetterException, MirrorCantFindMethodException;

    /**
     * <p>
     * Sets target object for mirror. Target object is a object from where
     * Mirror gathers its field values for storage.
     * </p>
     * 
     * <p>
     * Mirror will go through all the fields and search for fields that are
     * annotated with {@link Contain} annotation.
     * </p>
     * 
     * @param target
     *            class to search for fields annotated {@link Contain}
     *            annotation.
     * @throws NullPointerException
     *             Optional exception. Will be thrown when target object is
     *             null.
     */
    void setTarget(final Object target) throws NullPointerException;

    /**
     * Sets container used by Mirror. Container is a class that implements
     * {@link Container}. Container will be used to store field values and from
     * where to retrieve those values by their keys.
     * 
     * @param container
     *            {@link Container} where to store and retrieve values from.
     * @throws NullPointerException
     *             Optional exception. Will be thrown when {@link Container} is
     *             null.
     */
    void setContainer(final Container container) throws NullPointerException;
}
