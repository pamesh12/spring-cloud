## Configure Discovery Server (HA Setup)

We will set up a **Netflix Eureka** service registry, to which a client will both registers itself and uses it to resolve its own host. A service registry is useful because it enables client-side load-balancing and decouples service providers from consumers without the need for DNS.

## <span id="Prerequisites"><strong>Prerequisites</strong></span>

Before we begin, it’s recommended to have already completed [Spring Config Server](./spring-config-server.md) and [Spring Config Client](./spring-config-client.md).

## <span id="Maven_Configuration">Maven Configuration</span>

<pre class="brush: xml; title: ; notranslate" title="">&lt;dependency&gt;
  &lt;groupId&gt;org.springframework.cloud&lt;/groupId&gt;  
  &lt;artifactId&gt;spring-cloud-starter-netflix-eureka-server&lt;/artifactId&gt;  
&lt;/dependency&gt;
&lt;dependency&gt;
  &lt;groupId&gt;org.springframework.cloud&lt;/groupId&gt;
  &lt;artifactId&gt;spring-cloud-starter-config&lt;/artifactId&gt;
&lt;/dependency&gt;
</pre>

## <span id="Start_a_Eureka_Service_Registry">Start a Eureka Service Registry</span>

Spring Cloud’s `@EnableEurekaServer` is used to stand up a registry with which other applications can communicate.

<pre class="brush: java; title: ; notranslate" title="">@SpringBootApplication
@EnableEurekaServer
public class SpringDiscoveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringDiscoveryApplication.class, args);
    }
}
</pre>

## <span id="Register_with_Config_Server">Register with Config Server</span>

Configure `spring.cloud.config.uri` with config server url in bootstrap.yml.

<pre class="brush: yaml; title: ; notranslate" title="">spring:
  application:
    name: discoveryserver
  cloud:
    config:
      uri: ${config.url:http://localhost:8001}
      name: discoveryserver
</pre>

#### <span id="Setup_External_Configuration_file_in_config_server">Setup External Configuration file in config server</span>

Update the files with below naming convention, available under config folder as defined in config server.  
`discoveryserver-<profile>.yml`

## <span id="Setup_Multiple_Discovery_Nodes">Setup Multiple Discovery Nodes</span>

We will be setting up 2 instances (HA) of discovery server.<figure class="wp-block-table">

|Node | Port| Profile|
|------|-------|-------|
|Node1 | 8101| node1|
|Node2| 8102| node2|

### <span id="Setup_Node_1">Setup Node 1</span>

For **_node1_** property file name will be `discoveryserver-node1.yml`

Add _node2_ **_URL_** for below property. (In our case _node2_ is running on port 8102)

`eureka.client.service.url.defaultZone =http://localhost:8102/eureka/` 

<pre class="brush: yaml; title: ; notranslate" title="">eureka:
  client:
    service-url:
      defaultZone: http://localhost:8102/eureka/
  region: default
spring:
  profiles: node1
</pre>

### <span id="Setup_Node_2">Setup Node 2</span>

For **_node2_** property file name will be `discoveryserver-node2.yml`

Add _node_1 **_URL_** for below property. (In our case _node1_ is running on port 8101)

`eureka.client.service,url.defaultZone=https://localhost:8101/eureka/`

<pre class="brush: yaml; title: ; notranslate" title="">eureka:
  client:
    service-url:
      defaultZone: http://localhost:8101/eureka/
  region: default
spring:
  profiles: node2
</pre>

_**Once the properties file of both nodes are updated, restart the config server.**_

## <span id="Build_the_project">Build the project</span>

Execute `mvn clean install` to build the jar.

## <span id="Test_the_Application">Test the Application</span>

In order to test the application, we need to run two instances of jar file.

**_Run Node1_**  
java -jar -Dspring.profiles.active=node1 -Dserver.port=8101 spring-discovery-1.0.0.jar

**_Run Node2_***  
java -jar -Dspring.profiles.active=node2 -Dserver.port=8102 spring-discovery-1.0.0.jar

_**Note: There might be some exceptions initially till both servers are up as they will try to connect to each other. Those can be ignored.**_

Once both the applications are running, we can test it by opening below url in browser.  
Node 1 &#8211; `http://localhost:8101/`  
Node 2 &#8211; `http://localhost:8102/`

You will notice that `node1` has registered itself as replica in `node2` and vice-versa.
