## Tech Stack:
- Spring Boot + React frontend
- Eureka Server (Spring Cloud Netflix): for service discovery
- Spring Cloud Gateway
- Keycloak: for security: synchronise users from a authentication server/ authorization server that exist into your application if you have any profile service/ user service that exist in your architecture
- RabbitMQ (Spring AMQP)
- PostgreSQL & MongoDB
- Google Gemini API
- Spring Cloud Config Server: centralized spring cloud config server which is much needed these days 

## Architecture:
![fitness-microservices drawio](https://github.com/user-attachments/assets/064fe4a9-1f1e-42d4-9700-941360a414cb)

- All the microservices is using different databases which indicates they are not dependent on each other. EG: when team user is having upgrading, other microservices will not be impacted in the network.
- There are 2 communications happen in the system:
  - Asynchronous communication
    - Activity to AI service (Activity publish saved information to the message queue and the AI services will listen to the message queue.
    - It consume the data and generate the AI generated recommendation from the Google Gemini Server and saved it in MongoDB.
  - Synchronous communication
    - Activity to user (to check whether user is valid in database)
- RabbitMQ - make use of message queue
- Config server - keep track of all the configurations that exist amongst all the microservices (central repository for all the configurations)
- Eureka server - make use for service discovery
