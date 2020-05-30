## Configure Spring Cloud Gateway Server

Spring Cloud Gateway aims to provide a simple, yet effective way to route to APIs and provide cross cutting concerns to them such as: security, monitoring/metrics, and resiliency.

## <span id="Prerequisites"><strong>Prerequisites</strong></span>

Before we begin, it’s recommended to have already completed&nbsp;[Spring Config Server](./spring-config-server.md), [Spring Config Client](./spring-config-client.md) and [Spring Discovery Server](./spring-discovery-server.md).

## <span id="Maven_Configuration">Maven Configuration</span>

<pre class="brush: plain; title: ; notranslate" title="">&lt;dependency&gt;
  &lt;groupId&gt;org.springframework.cloud&lt;/groupId&gt;
  &lt;artifactId&gt;spring-cloud-starter-gateway&lt;/artifactId&gt;
&lt;/dependency&gt;

&lt;dependency&gt;
  &lt;groupId&gt;org.springframework.cloud&lt;/groupId&gt;
  &lt;artifactId&gt;spring-cloud-starter-config&lt;/artifactId&gt;
&lt;/dependency&gt;

&lt;dependency&gt;
  &lt;groupId&gt;org.springframework.cloud&lt;/groupId&gt;
  &lt;artifactId&gt;spring-cloud-starter-netflix-eureka-client&lt;/artifactId&gt;
&lt;/dependency&gt;
</pre>

## <span id="Register_with_Config_server">Register with Config server</span>

Configure `spring.cloud.config.uri` with [config server](./spring-config-server.md) url in `bootstrap.yml`.

<pre class="brush: plain; title: ; notranslate" title="">spring:
  cloud:
    config:
      uri: ${config.url:http://localhost:8001}
      name: gateway
</pre>

## <span id="Register_with_Discovery_Server">Register with Discovery Server</span>

Add Spring Cloud’s `@EnableDiscoveryClient` to register the application to discovery server.

<pre class="brush: java; title: ; notranslate" title="">@SpringBootApplication
@EnableDiscoveryClient
public class SpringGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringGatewayApplication.class, args);
    }

}
</pre>

Add [discovery server](./spring-discovery-server.md) url in the `gateway-<profile>.yml` file, available under config folder of config server.

  * `eureka.client.service.url.defaultZone` = http://localhost:8101/eureka,http://localhost:8102/eureka.

_Once the properties file of both nodes are updated, restart the config server._

## <span id="Build_the_project">Build the project</span>

Execute `mvn clean install` to build the jar.

## <span id="Test_the_Application">Test the Application</span>

In order to test the application, we need to run below command

`java -jar -Dspring.profiles.active=dev -Dserver.port=8003 spring-gateway-1.0.0.jar`

_**Note: Start discovery nodes and config server before gateway server.**_

Once all the applications are running, we can test it by opening below url in browser.  
Node 1 &#8211; `http://localhost:8101/`  
Node 2 &#8211; `http://localhost:8102/`

You will notice that gateway server has registered itself on both eureka nodes.
