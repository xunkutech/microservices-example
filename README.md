# microservices-example
https://dzone.com/articles/step-by-step-a-simple-spring-boot-microservices-ba

## Run as a docker compose
```bash
mvn clean package dockerfile:build
docker-compose up
minikube ip
# Open Eureka Dashboard: http://[ip]:9001/,http://[ip]:9001/eureka/apps
# Query books: http://[ip]:9900/book-service/books
# Query librarys: http://[ip]:9900/library-service/librarys
# Query read: http://[ip]:9900/read-service/read/Book2
```
## Run as a k8s
```bash
brew install docker
brew install docker-compose
brew install kubectl
brew install minikube
minikube start --driver=hyperkit --container-runtime=docker
minikube kubectl get nodes
kubectl get all
eval $(minikube docker-env)
docker info

helm lint
helm template helm-charts
helm install sanyi-sf --debug --dry-run helm-charts
helm install sanyi-sf  helm-charts
helm list -a

```