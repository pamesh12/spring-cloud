## Configure Spring Cloud Config Client

In order for an Spring application to use [Spring Cloud Config Server](./spring-config-server.md), we can build an Spring application that depends on _**spring-cloud-config-client**_ 

## <span id="Prerequisites"><strong>Prerequisites</strong></span>

Before we begin, it&#8217;s recommended to have already completed [Configure Spring Cloud Config Server](./spring-config-server.md). We&#8217;re going to take an existing cloud config server for this post.

## <span id="Maven_Dependencies">Maven Dependencies</span>

In order to add Config client to project, add below dependencies.

<pre class="brush: xml; title: ; notranslate" title="">&lt;dependency&gt;
  &lt;groupId&gt;org.springframework.cloud&lt;/groupId&gt;
  &lt;artifactId&gt;spring-cloud-starter-config&lt;/artifactId&gt;
&lt;/dependency&gt;

&lt;dependencyManagement&gt;
	&lt;dependencies&gt;
		&lt;dependency&gt;
			&lt;groupId&gt;org.springframework.cloud&lt;/groupId&gt;
			&lt;artifactId&gt;spring-cloud-dependencies&lt;/artifactId&gt;
			&lt;version&gt;${spring-cloud.version}&lt;/version&gt;
			&lt;type&gt;pom&lt;/type&gt;
			&lt;scope&gt;import&lt;/scope&gt;
		&lt;/dependency&gt;
	&lt;/dependencies&gt;
&lt;/dependencyManagement&gt;
</pre>

## <span id="Configure_Config_Client">Configure Config Client</span>

The properties to configure the Config Client must necessarily be read in&nbsp;_before_&nbsp;the rest of the application’s configuration is read from the Config Server, during the&nbsp;_bootstrap_&nbsp;phase. 

When a config client starts, it binds to the Config Server (through the **spring.cloud.config.uri** bootstrap configuration property) and initializes Spring Environment with remote property sources. Provide below properties in _`bootstrap.properties`_

<pre class="brush: yaml; title: ; notranslate" title="">spring.cloud.config.uri=${config.url:http://localhost:8001}
spring.application.name=rest
</pre>

## <span id="Rest_Controller">Rest Controller</span>

<pre class="brush: java; title: ; notranslate" title="">@RestController
public class MyRestController {

	@Autowired
	private ApplicationProperties properties;

	@GetMapping("/hello")
	public ResponseEntity&lt;String&gt; hello(){
		return new ResponseEntity&lt;String&gt;("Hi !!!"+ properties.getGreeting(), HttpStatus.OK);
	}
}
</pre>

ApplicationProperties file holds all the properties of application and uses [ConfigurationProperties](/spring-boot-configuration-properties).

<pre class="brush: plain; title: ; notranslate" title="">@Bean
@ConfigurationProperties(prefix = "rest")
@RefreshScope
public ApplicationProperties applicationProperties() {
	return new ApplicationProperties();
}
</pre>

## <span id="Test_Application">Test Application</span>

**_Let us suppose the active profile for application is dev_**.

Now when the application starts, it will try to connect to config server url and read a file with name _**`rest-dev.yml`**_ or `<em><strong>rest-dev.properties</strong></em>`. _`(spring.application.name)-(spring.profiles.active)`_

Hit the url  
`<em>http://localhost:8080/hello</em>`  
  
Response :  
**_Hello User!!_**  
  
The value of property `rest.greeting` is injected in the controller via config server.

In the next article we will configure [Spring Cloud Bus](./spring-cloud-bus.md). It helps in _refreshing_&nbsp;the configuration to reflect changes to the Config Server on-demand, without restarting the JVM.&nbsp;
