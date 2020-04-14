
Link to modules in the application.

* [Spring Config Server](./spring-config-server)
* [Spring Discovery Server](./spring-discovery-server)
* [Spring Gateway Server](./spring-gateway)
* [Spring Rest](./spring-rest)

All these modules can be deployed to **docker** using compose file. 
1.  **.env file** - Default environment file for docker compose. This file allows you to declare environment variables for your containers. Properties defined in this file will be used to replace placeholders defined in docker-compose file.
2. **env-dev** - Environment file specific to profile. The values in this file will be used during container runtime. *Properties in this file will not replace the placeholders. *
3. **docker-compose.yml**- Docker compose file.
4. **docker-compose.dev.yml** - Docker compose file to override/extend services defined in docker-compose file as per env.

To deploy the project to docker use below command.

`docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d`

