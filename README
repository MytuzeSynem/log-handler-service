# Read Me First
The following was discovered as part of building this project:

* The original package name 'com.example.logs-handler-service' is invalid and this project uses 'com.example.logshandlerservice' instead.

# Description
The application is realized in producer consumer pattern. The shared object is BlockingQueue.
Shared BlockingQueue is limited to 1k elements to not grow and take to memory.

- the producer reads input file, map lines to pojos, match two objects with same id and send it to queue.
  File is read by line. There is small "cache" on input to match incoming lines (rawLogEventsStringLineCache).
  When eof producers send poison pill to consumers.
  
- the consumer reads paired log evens map it to db entity and save it. There are multiple consumers threads.
  Number of Threads is equal to Runtime.getRuntime().availableProcessors(). Threads are fired as a Runnable by
  Executor service. The Future are blocked for app to know if it finished on shutdown. In demo this is fine should 
  be done different in real app.
  
App has hsqldb embedded server witch works fine with small amount of data, but it grows with the data stored. Data
from data base are in ${workingdir}/hsqldb/. Program will create table in DB. Application in default starts connection to embedded server, but from reason
mentioned before, it better to start the server standalone and change connection details in application.properties.
Biggest file tested was about 2,5GB and there was avg memory usage around 100-150 Mb CPU ~ 10%. The memory usage does not grow 
noticeably with large files.

Logback is configured to write Info on stdout and debug to file ${workingdir}/application_logs.

there are no tests included, I did not make it on time.

The folder contains primitive bash script to generate test files. It creates simplified version of log because it copy
three pairs with changed id. To generate desired file size, one must change upper boundary in for loop. 1M loops is about
450MB file   

# Usage

mvn spring-boot:run -Dspring-boot.run.arguments="<absolute/path/to/file/on/disk>"

!!!!!!!!!! if path will be invalid app will hang it due the Future blocking, as mentioned, it should be changed, kill it with Ctr + c !!!!!!!!!!!!

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/maven-plugin/reference/html/#build-image)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

