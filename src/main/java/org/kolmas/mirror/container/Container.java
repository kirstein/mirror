package org.kolmas.mirror.container;

public interface Container {

    String COLLECTION_SET_METHOD = "storeCollection";
    String COLLECTION_GET_METHOD = "retrieveCollection";

	void store(String fieldName, Class<?> type, Object result);
	Object retrieve(String fieldName);
	
}
