## Configure Spring Config Server

Spring Cloud Config provides server-side and client-side support for externalized configuration in a distributed system. With the Config Server, you have a central place to manage external properties for applications across all environments.

## <span id="Maven_Dependencies">Maven Dependencies</span>

<pre class="brush: xml; title: ; notranslate" title="">&lt;dependency&gt;
    &lt;groupId&gt;org.springframework.cloud&lt;/groupId&gt;
    &lt;artifactId&gt;spring-cloud-config-server&lt;/artifactId&gt;
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

## <span id="Start_Configuration_Server">Start Configuration Server</span>

You first need a Config Service to act as a sort of intermediary between the Spring applications and version-controlled repository of configuration files. We use Spring Cloud’s&nbsp;`@EnableConfigServer`&nbsp;to enable a config server that can communicate with other applications. 

<pre class="brush: java; title: ; notranslate" title="">@SpringBootApplication
@EnableConfigServer
public class SpringConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringConfigServerApplication.class, args);
	}
</pre>

The Config Server needs to know which repository to manage. For the sake of simplicity, we have used native file system to store the config file. 

Other sources are any JDBC compatible database, GIT, Subversion, Hashicorp Vault, Credhub.

## <span id="File_System_Repository">File System Repository</span>

To use file system to store the config file, we need to set profile = **_native_**.  
Configure below properties in `application.properties`

<pre class="brush: yaml; title: ; notranslate" title="">spring.profiles.active=native 
server.port=8001
config.server.native.search-locations = &lt;path of config files&gt;
</pre>

## <span id="Query_Configuration">Query Configuration</span>

When config server starts, it will exposes some endpoints for config clients to get the configuration. The HTTP service has resources in the following form:

<pre class="brush: plain; title: ; notranslate" title="">/{application}/{profile}[/{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties
</pre>

where&nbsp;`application`&nbsp;is injected as the&nbsp;`spring.config.name`&nbsp;in the&nbsp;`SpringApplication`&nbsp;(what is normally&nbsp;`application`&nbsp;in a regular Spring Boot app),&nbsp;`profile`&nbsp;is an active profile (or comma-separated list of properties), and&nbsp;`label`&nbsp;is an optional git label (defaults to&nbsp;`master`.)

Let us put a config file **_rest-dev.yml_** in the location provided above for _`config.server.native.search-locations`_

_where,  
rest &#8211; spring application name  
dev &#8211; Spring active profile_

**Content of rest-dev.yml**

<pre class="brush: yaml; title: ; notranslate" title="">rest:
  greeting: Hello User!!
</pre>

## <span id="Test_the_application">Test the application</span>

Once the server is running, try hitting url.

<pre class="wp-block-code"><code>http:&#47;&#47;localhost:8001/rest/dev</code></pre>

In response, you will get the content of rest-dev.yml file stored in the config folder.

We will be configuring [Spring Config Client](./spring-config-client.md) and [Spring Cloud Bus](./spring-cloud-bus.md) next.
