Mirror
===============================================================================
	
Mirror is an api that should make collecting and storing data easier.
Mirror was made thinking about form saving applications.

Contains DirectMirror implementation.

```Java
// Field to contain in container.
@Contain
private String data;

Container container = new Container();
Mirror mirror = new DirectMirror(this);

// Mirror from class to container
mirror.store(container);

// Mirror from container to class
mirror.retrieve(container);
```

How it works
===============================================================================

Mirror has two main functions:

	mirror.store(container) - copies all fields marked with @Contain annotation to
	predefined container. 
	
	mirror.fetch(container) - tries to copy all the data from container to given 
	fields in targeted class.
	
For gathering information about fields Mirror is using Java reflections API.
Every field that is marked with Contains annotation in target class must have its 
getter and setter methods (methods can be made at runtime).

Fields data will be gathered after Mirrors target is set. 


Install
===============================================================================

    git clone git@github.com:kirstein/mirror.git
    cd mirror
	mvn clean install

Demo
===============================================================================

https://github.com/kirstein/demo-mirror 
