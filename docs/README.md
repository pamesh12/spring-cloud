# Spring Cloud

   Project based on **Spring Boot** and **Spring Cloud** framework.
   
* [Spring Config Server](./spring-config-server.md) - Server to store the config for all the applications.
* [Spring Config Client](./spring-config-client.md) - Client configuration to utilize Config Server.
* [Spring Cloud Bus](./spring-cloud-bus.md) - Spring Cloud Bus to refresh properties updated in Config server at runtime.
* [Spring Discovery Server](./spring-discovery-server.md) - (High Availability setup) - Discovery server for all the applications to register itself to.  
* [Spring Gateway Server](./spring-gateway.md) - Spring API Gateway Server.  
* [Spring Rest](./spring-rest.md) - Sample Rest API backend.

* **docker compose** file is also provided to deploy this setup on container environment

All these modules can be deployed to **docker** using compose file. 
1.  **.env file** - Default environment file for docker compose. This file allows you to declare environment variables for your containers. Properties defined in this file will be used to replace placeholders defined in docker-compose file.
2. **env-dev** - Environment file specific to profile. The values in this file will be used during container runtime. *Properties in this file will not replace the placeholders. *
3. **docker-compose.yml**- Docker compose file.
4. **docker-compose.dev.yml** - Docker compose file to override/extend services defined in docker-compose file as per env.

To deploy the project to docker use below command.

`docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d`

