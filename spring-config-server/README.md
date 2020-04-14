Spring Cloud Config provides server-side and client-side support for externalized configuration in a distributed system. With the Config Server, you have a central place to manage external properties for applications across all environments.

#### Config Server 

For the sake of simplicty, we have used native file system to store the config file.  (profile = ***native*** )  
`config.server.native.search-locations = <path where config files are stored>`

To enable ConfigServer, we should add  `@EnableConfigServer` on SpringBoot main class.

To start the server, build the project and run below command  
`./mvnw spring-boot:run`

Application will by default run on **8001** port. (server.port defined in application.yml)

The HTTP service has resources in the following form:

    /{application}/{profile}[/{label}]
    /{application}-{profile}.yml
    /{label}/{application}-{profile}.yml
    /{application}-{profile}.properties
    /{label}/{application}-{profile}.properties

Once the server is running, try hitting url.
http://localhost:8001/rest/dev

In response, you will get the content related to rest-dev.yml file stored in the config folder.

#### Config Client

In order to add Config client to project, add below dependecy

    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>

When a config client starts, it binds to the Config Server (through the **spring.cloud.config.uri** bootstrap configuration property) and initializes Spring Environment with remote property sources.

    spring.cloud.config.uri=${config.url:http://localhost:8001}
