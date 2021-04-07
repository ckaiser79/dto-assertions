# About

This is a small utility to use *property based testing* in JUnit. The library is capable to deserialize objects from JSON files and use them in JUnit.

The use case in mind was to exchange dto objects between two teams - a provider and a consumer without doing breaking changes.

## Overview

```
   +--------------------------------+
   | Provider (Team 1)              |
   +--------------------------------+
   |                                |
   | - contract-examples:latest-rel |
   | - contract-api:1.5             |
   | - dto-assertions               |
   |                                |
   +-------------------+------------+
                       |
   +-------------------+------------+
   | contract-examples (Both teams) |
   +-------------------+------------+             
                       |
   +-------------------+------------+
   | Consumer (Team 2)              |
   +--------------------------------+
   |                                |
   | - contract-examples:latest-rel |
   | - contract-api:1.2             |
   | - dto-assertions               |
   |                                |
   +--------------------------------+
```

The consumer uses an older API. By having all parties using the latest examples both team ensure the data from the latest `contract-api` (here as an example version 1.5) is compatible to all parties.

# Compile

```
mvn clean install
```

Then declare them as test dependencies in your project:

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

Declare the `dto-assertions` as a test dependency or just copy the classes to you project test utility project.

See the [class XkcdComicInfoContractTest](dummy-api/src/test/java/de/servicezombie/samples/xkcd_transfer/XkcdComicInfoContractTest.java) for examples to check for mandatory, outdated fields or regular constraints.

# Criticism

Before starting this, I thought about getting familiar with [Pact](https://docs.pact.io/). This does something similar. But maybe I am just to stupid to understand pact completely. I think a solution based on JUnit and Java plain only is much faster to adopt.
