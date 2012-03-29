package org.kolmas.mirror;

import org.kolmas.mirror.container.Container;

public interface Mirror {
    /**
     * Copy initiated class data to given container. All class fields that are
     * annotated with predefined annotation (annotation: Contain.class) will be
     * copied to given container.
     * 
     * @param container
     *            - container to copy things to.
     * @return filled container.
     */
    Container to(Container container);

    /**
     * Fill given class from container.
     * 
     * @param container
     */
    void from(Container container);

    /**
     * Checks to see if target class is ready for data transfer or not.
     * 
     * @return
     */
    boolean isPrepared();

    /**
     * Prepares target class for data transfer.
     */
    void prepare();
    
    /**
     * Set target class
     * 
     * @param target - target class from which to gather information.
     */
    void setTarget(Object target);

}
