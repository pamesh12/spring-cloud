# Spring Discovery Server (High Availability Setup)

We will set up a **Netflix Eureka** service registry, to which a client will both registers itself and uses it to resolve its own host. A service registry is useful because it enables client-side load-balancing and decouples service providers from consumers without the need for DNS.

This service will use the [Config Server](../spring-config-server) to store the configurations. `bootstrap.yml` will only be used to configure the config server url.

### Spring Discovery Service Maven Configuration

```
<dependency>
  <groupId>org.springframework.cloud</groupId>  
  <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>  
</dependency>
```    
### Start a Eureka Service Registry

Spring Cloud’s `@EnableEurekaServer` is used to stand up a registry with which other applications can communicate.

```java
@SpringBootApplication
@EnableEurekaServer
public class SpringDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDiscoveryApplication.class, args);
	}

}
```

Configure `spring.cloud.config.uri` with config server url in bootstrap.yml.
```
spring:
  application:
    name: discoveryserver
  cloud:
    config:
      uri: ${config.url:http://localhost:8001}
      name: discoveryserver
```      

#### Setup External Configuration file in config server.
Update the files with below naming convention, available under config folder as defined in config server.  
`discoveryserver-<profile>.yml`

We will be setting up 2 instances (HA) of discovery server.

**Node1 - port 8101**  
**Node2 - port 8102**  

####### Node1
 For *node1* property file name will be `discoveryserver-node1.yml`
 
 Update below properties.
  - `eureka.client.service,url.defaultZone` - Url of node2 server. 
  
 As, node2 is running at `8102`, it will be `localhost:8102/eureka/`
 
 ```
 eureka:
  client:
    service-url:
      defaultZone: ${peer.server.url:http://localhost:8102/eureka/}
  region: default
spring:
  profiles: node1
```

####### Node2
 For *node2* property file name will be `discoveryserver-node2.yml`
 
 Update below properties.
  - `eureka.client.service,url.defaultZone` - Url of node1 server. 
  
 As, node1 is running at `8101`, it will be `localhost:8101/eureka/`
 
 ```
 eureka:
  client:
    service-url:
      defaultZone: ${peer.server.url:http://localhost:8101/eureka/}
  region: default
spring:
  profiles: node2
```

*Once the properties file of both nodes are updated, restart the config server.*

### Build the project

Execute `mvn clean install` to build the jar.

### Test the Application

In order to test the application, we need to run two instances of jar file.

***Run Node1***  
java -jar -Dspring.profiles.active=node1 -Dserver.port=8101 spring-discovery-1.0.0.jar  

***Run Node2****  
java -jar -Dspring.profiles.active=node2 -Dserver.port=8102 spring-discovery-1.0.0.jar  

*Note: There might be some exceptions initially till both servers are up as they will try to connect to each other. Those can be ignored.*

Once both the applications are running, we can test it by opening below url in browser.  
Node 1 - `http://localhost:8101/`    
Node 2 - `http://localhost:8102/`  

You will notice that `node1` has registered itself as replica in `node2` and vice-versa. 
