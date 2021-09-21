# microservices-example
https://dzone.com/articles/step-by-step-a-simple-spring-boot-microservices-ba

## Run as a docker compose
```bash
mvn clean package dockerfile:build
docker-compose up
minikube ip
# Open Eureka Dashboard: http://[ip]:9001/,http://[ip]:9001/eureka/apps
# Query books: http://[ip]:9901/books
# Query librarys: http://[ip]:9902/librarys
# Query read: http://[ip]:9903/read/Book2
```
