package org.kolmas.mirror.container;

public interface Container {

	public void store(String fieldName, Object result);
	public Object retrieve(String fieldName);
	
}
