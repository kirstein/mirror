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
mirror.to(container);

// Mirror from container to class
mirror.from(container);
```

How it works
===============================================================================

Mirror has two main functions _to(container) and _from(container). 
To function means that mirror will try to copy each field marked with Contain annotation
to container.

From functions mean that mirror will try to get that field from container and store it in 
given target class.


Install
===============================================================================

After getting the repository from github install mirror using Maven.

	mvn clean install

Demo
===============================================================================

https://github.com/kirstein/demo-mirror 

Demo that uses tapestry5.3, hibernate + HSQLDB, spring