# Spring Gateway Server

Spring Cloud Gateway aims to provide a simple, yet effective way to route to APIs and provide cross cutting concerns to them such as: security, monitoring/metrics, and resiliency.

This service will use the [Config Server](../spring-config-server) to store the configurations. `bootstrap.yml` will only be used to configure the config server url.

### Spring Gateway Service Maven Configuration

```
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-gateway</artifactId>
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
      name: gateway
```   

### Register with Discovery Server

Spring Cloud’s `@EnableDiscoveryClient` is used to register application to discovery server.

```java
@SpringBootApplication
@EnableDiscoveryClient
public class SpringGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringGatewayApplication.class, args);
	}

}

```
Update `gateway-<profile>.yml` file, available under config folder as defined in config server.  

- `eureka.client.service.url.defaultZone` - http://localhost:8101/eureka,http://localhost:8102/eureka. 

Above property holds the url(s) of discivery servers.

*Once the properties file of both nodes are updated, restart the config server.*

### Build the project

Execute `mvn clean install` to build the jar.

### Test the Application

In order to test the application, we need to run below command

`java -jar -Dspring.profiles.active=dev -Dserver.port=8003 spring-gateway-1.0.0.jar`  

*Note: Start discovery nodes and config server before gateway server.*

Once all the applications are running, we can test it by opening below url in browser.  
Node 1 - `http://localhost:8101/`    
Node 2 - `http://localhost:8102/`  

You will notice that gateway server has registered itself on both eureka nodes.
