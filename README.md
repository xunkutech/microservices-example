# microservices-example
https://dzone.com/articles/step-by-step-a-simple-spring-boot-microservices-ba

## Run as a docker compose

```bash
docker image ls |grep none |tr -s ' ' |cut -f3 -d' ' |xargs docker rmi --force
docker image ls |grep xunkutech |tr -s ' ' |cut -f3 -d' ' |xargs docker rmi --force
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

helm lint ./helm-chart
helm template ./helm-chart
helm install --create-namespace --namespace sanyi-erp --debug --dry-run sanyi ./helm-chart
helm install --create-namespace --namespace sanyi-erp sanyi ./helm-chart
helm uninstall --namespace sanyi-erp sanyi
minikube service list
kubectl -n sanyi-erp get svc
minikube service -n sanyi-erp sanyi-zuul-server
helm list -a
kubectl -n sanyi-erp get pods
# log zuul
kubectl logs --follow -n sanyi-erp `kubectl get pods -n sanyi-erp|grep springcloud|tr -s ' ' |cut -f1 -d' '` sanyi-springcloud-zuul
# log eureka
kubectl logs --follow -n sanyi-erp `kubectl get pods -n sanyi-erp|grep springcloud|tr -s ' ' |cut -f1 -d' '` sanyi-springcloud-eureka
# log config
kubectl logs --follow -n sanyi-erp `kubectl get pods -n sanyi-erp|grep springcloud|tr -s ' ' |cut -f1 -d' '` sanyi-springcloud-config
# log read service
kubectl -n sanyi-erp logs --follow `kubectl -n sanyi-erp get pods|grep read|tr -s ' ' |cut -f1 -d' '`
# log library service
kubectl -n sanyi-erp logs --follow `kubectl -n sanyi-erp get pods|grep library|tr -s ' ' |cut -f1 -d' '`
# log book service
kubectl -n sanyi-erp logs --follow `kubectl -n sanyi-erp get pods|grep book|tr -s ' ' |cut -f1 -d' '`
# get shell in zuul
kubectl exec -n sanyi-erp -ti `kubectl get pods -n sanyi-erp|grep springcloud|tr -s ' ' |cut -f1 -d' '` -c sanyi-springcloud-zuul -- /bin/sh
# get shell in book service
kubectl exec -n sanyi-erp -ti `kubectl get pods -n sanyi-erp|grep book |tr -s ' ' |cut -f1 -d' '` -- /bin/sh
# get shell in read service
kubectl exec -n sanyi-erp -ti `kubectl get pods -n sanyi-erp|grep read |tr -s ' ' |cut -f1 -d' '` -- /bin/sh
# Forward Eureka Server
kubectl -n sanyi-erp port-forward `kubectl -n sanyi-erp get pods |grep springcloud |tr -s ' ' |cut -f1 -d' '` 8761:8761
open http://localhost:8761
# Open Kubernetes Dashboard
kubectl proxy
open http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/

# Test service
open http://localhost:`kubectl -n sanyi-erp get svc |grep zuul |tr -s ' '|cut -f5 -d' ' |cut -f1 -d'/'|cut -f2 -d':'`/service-read/read/Book2

```
