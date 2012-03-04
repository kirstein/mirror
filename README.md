Mirror
===============================================================================
	
Mirror is an api that should make collecting and storing data easier.
Mirror was made thinking about form saving applications.

Contains DirectMirror implementation.

```Java
// Field to contain in container.
@Contain
private String dataM;

Container container = new Container();
Mirror mirror = new DirectMirror(this);

// Mirror from class to container
mirror.to(container);

// Mirror from container to class
mirror.from(container);
```

Install
===============================================================================

After getting the repository from github install mirror using Maven:
	mvn clean install

Demo
===============================================================================

https://github.com/kirstein/demo-mirror 

Demo that uses tapestry5.3, hibernate + HSQLDB, spring