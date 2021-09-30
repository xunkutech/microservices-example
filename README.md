# microservices-example
https://dzone.com/articles/step-by-step-a-simple-spring-boot-microservices-ba

## Run as a docker compose
```bash
docker image ls |grep none |tr -s ' ' |cut -f3 -d' ' |xargs docker rmi
docker image ls |grep xunkutech |tr -s ' ' |cut -f3 -d' ' |xargs docker rmi
mvn clean package dockerfile:build -DskipTests
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

helm lint ./chart
helm template ./chart
helm install --create-namespace --namespace sanyi-erp --debug --dry-run sanyi ./chart
helm install --create-namespace --namespace sanyi-erp sanyi ./chart
helm uninstall --namespace sanyi-erp sanyi
minikube service list
kubectl get svc -n sanyi-erp
minikube service -n sanyi-erp sanyi-zuul-server
helm list -a
kubectl get pods -n sanyi-erp
# log zuul
kubectl logs --follow -n sanyi-erp `kubectl get pods -n sanyi-erp|grep springcloud|tr -s ' ' |cut -f1 -d' '` sanyi-springcloud-zuul
# log eureka
kubectl logs --follow -n sanyi-erp `kubectl get pods -n sanyi-erp|grep springcloud|tr -s ' ' |cut -f1 -d' '` sanyi-springcloud-eureka
# log config
kubectl logs --follow -n sanyi-erp `kubectl get pods -n sanyi-erp|grep springcloud|tr -s ' ' |cut -f1 -d' '` sanyi-springcloud-config
# log read service
kubectl logs --follow -n sanyi-erp `kubectl get pods -n sanyi-erp|grep read-service|tr -s ' ' |cut -f1 -d' '`
# log library service
kubectl logs --follow -n sanyi-erp `kubectl get pods -n sanyi-erp|grep library-service|tr -s ' ' |cut -f1 -d' '`
# get shell in zuul
kubectl exec -n sanyi-erp -ti `kubectl get pods -n sanyi-erp|grep springcloud|tr -s ' ' |cut -f1 -d' '` --container sanyi-springcloud-zuul -- /bin/sh
# get shell in library
kubectl exec -n sanyi-erp -ti `kubectl get pods -n sanyi-erp|grep library-service |tr -s ' ' |cut -f1 -d' '` -- /bin/sh

```