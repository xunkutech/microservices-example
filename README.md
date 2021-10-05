# The full stack example for Springcloud in Kubernetes

https://dzone.com/articles/step-by-step-a-simple-spring-boot-microservices-ba

## Step 0: Preparation

```bash
brew install --cask docker # Enable kubernetes in Docker Desktop
# Or
brew install hyperkit
brew install docker
brew install docker-compose
brew install kubectl
brew install minikube
minikube start --driver=hyperkit --container-runtime=docker
eval $(minikube docker-env)

docker info
brew install helm

# Install dashboard
helm repo add kubernetes-dashboard https://kubernetes.github.io/dashboard/
helm install --create-namespace --namespace kubernetes-dashboard kubernetes-dashboard kubernetes-dashboard/kubernetes-dashboard

# disable dashboard login token
kubectl -n kubernetes-dashboard patch deployment kubernetes-dashboard --type 'json' -p '[{"op": "add", "path": "/spec/template/spec/containers/0/args/-", "value": "--enable-skip-login"}]'

helm repo add metrics-server https://kubernetes-sigs.github.io/metrics-server/
helm upgrade --install --namespace kube-system metrics-server metrics-server/metrics-server

kubectl delete  -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
kubectl -n kube-system patch deployment metrics-server --type 'json' -p '[{"op": "add", "path": "/spec/template/spec/containers/0/args/-", "value": "--kubelet-insecure-tls"}]'

# open dashboard
kubectl proxy
open http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:443/proxy/
```

## Step 1: Clean up

```bash
helm uninstall --namespace sanyi-erp sanyi
docker-compose down
docker image ls |grep none |tr -s ' ' |cut -f3 -d' ' |xargs docker rmi --force
docker image ls |grep xunkutech |tr -s ' ' |cut -f3 -d' ' |xargs docker rmi --force
```

## Step 2: Build the docker images from souce code

```bash
docker-compose -f build-compose.yml run build-java
docker-compose -f build-compose.yml run build-frontend-helloworld
docker-compose -f docker-compose.yml build

```

## Step 3. Test in the docker compose

```bash
docker-compose up
# Open Eureka Dashboard:
open http://`minikube ip`:8761/
# Query books: 
open http://`minikube ip`:9000/book-service/books
# Query librarys: 
open http://`minikube ip`:9000/library-service/librarys
# Query read:
open http://`minikube ip`:9000/read-service/read/Book2
```

## Step 4. Run on kubernetes

```bash
kubectl get nodes
kubectl get all

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
kubectl -n sanyi-erp get svc
kubectl -n sanyi-erp get endpoints

# log zuul
kubectl logs --follow -n sanyi-erp `kubectl get pods -n sanyi-erp|grep springcloud|tr -s ' ' |cut -f1 -d' '` sanyi-springcloud-zuul
# log eureka
kubectl logs --follow -n sanyi-erp `kubectl get pods -n sanyi-erp|grep springcloud|tr -s ' ' |cut -f1 -d' '` sanyi-springcloud-eureka
# log config
kubectl logs --follow -n sanyi-erp `kubectl get pods -n sanyi-erp|grep springcloud|tr -s ' ' |cut -f1 -d' '` sanyi-springcloud-config
# log read service
kubectl -n sanyi-erp logs --follow `kubectl -n sanyi-erp get pods|grep read|tr -s ' ' |cut -f1 -d' ' |head -n 1`
# log library service
kubectl -n sanyi-erp logs --follow `kubectl -n sanyi-erp get pods|grep library|tr -s ' ' |cut -f1 -d' ' |head -n 1`
# log book service
kubectl -n sanyi-erp logs --follow `kubectl -n sanyi-erp get pods|grep book|tr -s ' ' |cut -f1 -d' ' |head -n 1`
# get shell in zuul
kubectl exec -n sanyi-erp -ti `kubectl get pods -n sanyi-erp|grep springcloud|tr -s ' ' |cut -f1 -d' '` -c sanyi-springcloud-zuul -- /bin/sh
# get shell in book service
kubectl exec -n sanyi-erp -ti `kubectl get pods -n sanyi-erp|grep book |tr -s ' ' |cut -f1 -d' ' |head -n 1` -- /bin/sh
# get shell in read service
kubectl exec -n sanyi-erp -ti `kubectl get pods -n sanyi-erp|grep read |tr -s ' ' |cut -f1 -d' ' |head -n 1` -- /bin/sh

# Open Eureka Server
kubectl -n sanyi-erp port-forward `kubectl -n sanyi-erp get pods |grep springcloud |tr -s ' ' |cut -f1 -d' '` 8761:8761
open http://localhost:8761

# Test java service
kubectl -n sanyi-erp port-forward service/sanyi-server-zuul 9000:`kubectl -n sanyi-erp get service |grep server-zuul |tr -s ' '|cut -f5 -d' ' |cut -f1 -d'/'|cut -f1 -d'/'`
open http://localhost:9000/service-book/books
open http://localhost:9000/service-read/read/Book2

# Test static service
kubectl -n sanyi-erp port-forward service/sanyi-frontend-helloworld  8000:`kubectl -n sanyi-erp get service |grep frontend-helloworld |tr -s ' '|cut -f5 -d' ' |cut -f1 -d'/'|cut -f1 -d'/'`
open http://localhost:8000
```
