## Configure Spring Cloud Bus

Spring Cloud Bus links nodes of a distributed system with a lightweight message broker. This can then be used to broadcast state changes (e.g. configuration changes) or other management instructions.  
  
In this example, we will be using Kafka as broker.

## <span id="Prerequisites"><strong>Prerequisites</strong></span>

Before we begin, it’s recommended to have already completed&nbsp;[Spring Config Server](./spring-config-server.md) and [Spring Config Client](./spring-config-client.md).

## <span id="Configure_Config_Server">Configure Config Server</span>

In order to enable Cloud bus in Config Server, we need to provide below configuration in [Config Server](./spring-config-server).

### <span id="Maven_Configuration">Maven Configuration </span>

<pre class="brush: xml; title: ; notranslate" title="">&lt;dependency&gt;
    &lt;groupId&gt;org.springframework.cloud&lt;/groupId&gt;
    &lt;artifactId&gt;spring-cloud-config-monitor&lt;/artifactId&gt;
&lt;/dependency&gt;
&lt;dependency&gt;
    &lt;groupId&gt;org.springframework.cloud&lt;/groupId&gt;
    &lt;artifactId&gt;spring-cloud-starter-bus-kafka&lt;/artifactId&gt;
&lt;/dependency&gt;
</pre>

### <span id="Adding_Actuator"><strong>Adding Actuator </strong></span>

  1. Enable endpoints in actuator module.
  2. Add a unique bus id for each application under property **_`spring.cloud.bus.id`_**. This is very important to process the events from cloud bus.  
    

<pre class="brush: yaml; title: ; notranslate" title="">spring:
  application:
    name: config-server
  profiles:
    active: native
  cloud:
    bus:
      id: ${spring.application.name}:${spring.profiles.active}:${random.uuid}
      enabled: true
management:
  endpoints:
    web:
      exposure:
        include:
        - bus-refresh 
        - bus-env
</pre>

**_Kafka Installation is required in order to run this project. If broker is not available, disable the cloud bus. To disable cloud bus, update \`spring.cloud.bus.enabled=false&#8217;_**

## <span id="Configure_Config_Client">Configure Config Client</span>

In order to enable Cloud bus in Config Client, we need to provide below configuration in [Config Client](/configure-config-client).

### <span id="Maven_Dependencies">Maven Dependencies</span>

<pre class="brush: xml; title: ; notranslate" title="">&lt;dependency&gt;
	&lt;groupId&gt;org.springframework.cloud&lt;/groupId&gt;
	&lt;artifactId&gt;spring-cloud-bus&lt;/artifactId&gt;
&lt;/dependency&gt;

&lt;dependency&gt;
	&lt;groupId&gt;org.springframework.cloud&lt;/groupId&gt;
	&lt;artifactId&gt;spring-cloud-starter-bus-kafka&lt;/artifactId&gt;
&lt;/dependency&gt;

</pre>

### <span id="Enable_Actuator_and_Bus">Enable Actuator and Bus</span>

<pre class="brush: yaml; title: ; notranslate" title="">spring:
  application:
    name: rest
  cloud:
    bus:
      id: ${spring.application.name}:${spring.profiles.active}:${random.uuid} 
      enabled: true
      refresh:
        enabled: true
      env:
        enabled: true  
    config:
      uri: ${config.url:http://localhost:8001}
      name: rest
  profiles:
    active: dev
    
management:
  endpoint:
    info:
      enabled: true
  endpoints:
    enabled-by-default: true
    web:
      exposure:
        include:
        - bus-refresh
        - bus-env

</pre>

### <span id="Refresh_Scope">Refresh Scope</span>

In order to get the latest config changes from server, client must annotate the beans handling the configs with <a href="https://cloud.spring.io/spring-cloud-static/spring-cloud.html#_refresh_scope" target="_blank" rel="noreferrer noopener">@RefreshScope</a> annotation.

<pre class="brush: java; title: ; notranslate" title="">@Bean
@ConfigurationProperties(prefix = "rest")
@RefreshScope
public ApplicationProperties applicationProperties() {
	return new ApplicationProperties();
}
</pre>

## <span id="Test_Application">Test Application</span>

Start both the config server and client application.  
**Config Server** : http://localhost:8001  
**Client App** : http://localhost:8080

Hit the below url and check response.  
**Url:** `http://localhost:8080/hello`  
**Response**: Hello User!!

Now, go to config folder and update the property **`rest.greeting`** in `rest-dev.yml` 

`rest.greeting = Hello New User!!`

As we save the file, a refresh event will be sent to Kafka Broker and will be served to all the connected instance along with application name and profile. All applications who have the matching **`spring.cloud.bus.id`** will process that event and configs will be refreshed.

Hit the below url again and check response.  
**Url:** `http://localhost:8080/hello`  
**Response**: Hello New User!!
