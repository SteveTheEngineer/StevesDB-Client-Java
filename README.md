# StevesDB-Client-Java
A [StevesDB](https://github.com/SteveTheEngineer/StevesDB-Server) client written in Java

# Installation
Maven:
```xml
<project>
    <repositories>
        <repository>
            <id>stev-gq</id>
            <name>stev.gq</name>
            <url>https://repo.stev.gq/repository/generic/</url>
        </repository>
    </repositories>
    <dependencies>
        <dependency>
            <groupId>me.ste</groupId>
            <artifactId>stevesdbclient</artifactId>
            <version>VERSION</version>
            <type>jar</type>
        </dependency>
    </dependencies>
</project>
```
Gradle:
```gradle
repositories {
    maven {
        name = "stev-gq"
        url = "https://repo.stev.gq/repository/generic/"
    }
}
dependencies {
    compileOnly "me.ste:stevesdbclient:VERSION"
}
```
Find the latest version on [jenkins](https://jenkins.stev.gq/job/StevesDB-Client-Java/lastSuccessfulBuild/), the build number is the version
# Usage
Connecting to a StevesDB Server and logging in:
```java
public class Application {
    public Application() {
        StevesDBClient client = new StevesDBClient();
        client.connect(new InetSocketAddress("127.0.0.1", 2540), true).thenAccept(success -> { // Connect to the server
            if(success) {
                client.login("stevesdb", "password").thenAccept(success -> {
                    if(success) {
                        // The connection has been fully established
                    } else {
                        System.err.println("Invalid username or password"); // Print a message if the credentials are invalid
                    }
                });
            } else {
                System.err.println("Unable to connect to the server"); // Print a message if the connection fails
            }
        });
    }
}
```
The javadoc for the client is available [here](https://jenkins.stev.gq/job/StevesDB-Client-Java/javadoc/index.html)