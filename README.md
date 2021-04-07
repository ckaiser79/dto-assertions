# About

This is a small utility to use *property based testing* in JUnit. The library is capable to deserialize objects from JSON files and use them in JUnit.

The use case in mind was to 

## Overview

```
   +----------------------------+
   | Provider (Team 1)          |
   +----------------------------+
   |                            |
   | - contract-examples:1.5.3  |
   | - contract-api:1.5         |
   | - dto-assertions           |
   |                            |
   +-------------------+--------+
                       |
   +-------------------+--------+
   | contract-examples          |
   +-------------------+--------+             
                       |
   +-------------------+--------+
   | Consumer (Team 2)          |
   +----------------------------+
   |                            |
   | - contract-examples:1.5.3  |
   | - contract-api:1.2         |
   | - dto-assertions           |
   |                            |
   +----------------------------+
```

The consumer uses an older API. By having all parties using the latest examples both team ensure the data from the latest `contract-api` (here as an example version 1.5) is compatible to all parties.

# Compile

```
mvn clean install
```

Then use in your project:

```xml
	<dependencies>

		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>de.servicezombie.assertions</groupId>
			<artifactId>dto-assertions</artifactId>
			<version>[1.0.0,2.0.0)</version>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>com.acme.contracts</groupId>
			<artifactId>contract-under-test-examples</artifactId>
			<version>[1,2)</version> <!-- latest release available -->
			<scope>test</scope>
		</dependency>

	</dependencies>
```

# Integrate

Feel free to declare the `dto-assertions` as a test dependency or just copy the classes to you project test utility project.

# Criticism

Before starting this, I thought about getting familiar with [Pact](https://docs.pact.io/). This does something similar. But maybe I am just to stupid to understand pact completely. I think a solution based on JUnit and Java plain only is much faster to adopt.
