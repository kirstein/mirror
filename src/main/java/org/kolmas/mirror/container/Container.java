package org.kolmas.mirror.container;

public interface Container {

	void store(String fieldName, Object result);
	Object retrieve(String fieldName);
	
}
