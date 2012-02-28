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
    public Container to(Container container);

    /**
     * Fill given class from container.
     * 
     * @param container
     */
    public void from(Container container);

    /**
     * Checks to see if target class is ready for data transfer or not.
     * 
     * @return
     */
    public boolean isPrepared();

    /**
     * Prepares target class for data transfer.
     */
    public void prepare();
    
    public void setTarget(Object target);

}
