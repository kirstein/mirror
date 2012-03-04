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

Mirror has two main functions:

	mirror.to(container) - copies all fields marked with @Contain annotation to
	predefined container. 
	
	mirror.from(container) - tries to copy all the data from container to given 
	fields in targeted class.
	
For gathering information about fields Mirror is using Java reflections API.
Every field that is marked with Contains annotation in target class must have its 
getter and setter methods (methods can be made at runtime).

Fields data will be gathered after Mirrors target is set. 


Maven
===============================================================================

Dependencey:

	<dependency>
		<groupId>org.kolmas</groupId>
		<artifactId>mirror</artifactId>
		<version>1.0.0-beta-1-SNAPSHOT</version>
	</dependency>
	
Repository:

	<repository>
	    <id>org.kolmas maven repository snapshots</id>
		<url>https://github.com/kirstein/maven-repo/tree/master/snapshots</url>
	</repository>


Install
===============================================================================

After getting the repository from github install mirror using Maven.

	mvn clean install

Demo
===============================================================================

https://github.com/kirstein/demo-mirror 