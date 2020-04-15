### Spring REST API - Sample Rest API

Simple rest application with /hello endpoint.This application will register with discovery server and will be behind Spring gateway.

### Service Maven Configuration

```
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-config</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```    
### Register with Config server

Configure `spring.cloud.config.uri` with config server url in bootstrap.yml.

```
spring:
  cloud:
    config:
      uri: ${config.url:http://localhost:8001}
      name: rest
```   

### Register with Discovery Server

Spring Cloud’s `@EnableDiscoveryClient` is used to register application to discovery server.

```java
@SpringBootApplication
@EnableDiscoveryClient
public class SpringRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestApplication.class, args);
	}
}

```
Update `rest-<profile>.yml` file, available under config folder as defined in config server.  

- `eureka.client.service.url.defaultZone` - http://localhost:8101/eureka,http://localhost:8102/eureka. 

Above property holds the url(s) of discivery servers.

*Once the properties file of both nodes are updated, restart the config server.*

### Register for Spring Cloud Bus

We will register this application to Spring Bus in order to have dynamic property update from config server.

```
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-bus</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-bus-kafka</artifactId>
</dependency>
```
We need to put `@RefreshScope` on the properties we want to update dynamically.

```java
@Bean
@ConfigurationProperties(prefix = "rest")
@RefreshScope
public ApplicationProperties applicationProperties() {
  return new ApplicationProperties();
}
```

In the code above, all the properties defined in `ApplicatioProperties` file will be refreshed (if updated from config server)

### Configure Gateway Service

In order for gateway server to send request to REST-SERVICE, we need to add the service to gateway server. 

Navigate to `gateway-<profile>.yml` file and update it as below. 

*`spring.application.name` of registering app is used with lb*

```
spring:
  cloud:
    gateway:
      routes:
      - id: hello-service
        uri: lb://REST-SERVICE
        predicates:
        - Path=/hello 
```
### Build the project

Execute `mvn clean install` to build the jar.

### Test the Application

In order to test the application, we need to run below command

`java -jar -Dspring.profiles.active=dev -Dserver.port=8004 spring-rest-1.0.0.jar`  

*Note: Start discovery nodes and config server before starting this server.*

Once all the applications are running, we can test it by opening below url in browser.  
Node 1 - `http://localhost:8101/`    
Node 2 - `http://localhost:8102/`  

You will notice that REST-SERVICE has registered itself on both eureka nodes.

To test the endpoint hit `http://localhost:8003/hello`. Notice that we are hitting 8003 port of gateway service and not the backend rest service directly.

### Test Spring Cloud Bus

In order to test dynamic property update, perform below steps.
* Update the property `rest.greeting` to a new value in rest-<profile>.yml file under config location.
* Hit http://localhost:8003/hello again.
* Latest updated value should be available in the response.	

