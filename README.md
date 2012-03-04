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

MVN Dependency
===============================================================================
	
	<dependency>
		<groupId>org.kolmas</groupId>
		<artifactId>mirror</artifactId>
		<version>1.0.0-beta-1-SNAPSHOT</version>
	</dependency>
	
MVN Repository
===============================================================================

	<repository>
	    <id>org.kolmas maven repository snapshots</id>
		<url>https://github.com/kirstein/maven-repo/tree/master/snapshots</url>
	</repository>

How it works
===============================================================================

Mirror has two main functions __to(container) and __from(container). 
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