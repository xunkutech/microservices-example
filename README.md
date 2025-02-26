# The full stack example for Springcloud in Kubernetes

<https://dzone.com/articles/step-by-step-a-simple-spring-boot-microservices-ba>

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
# Get dashboard token
kubectl -n kubernetes-dashboard describe secrets `kubectl -n kubernetes-dashboard get secrets |grep kubernetes-dashboard-token |cut -f1 -d' '`
# Or
# Disable dashboard login token
kubectl -n kubernetes-dashboard patch deployment kubernetes-dashboard --type 'json' -p '[{"op": "add", "path": "/spec/template/spec/containers/0/args/-", "value": "--enable-skip-login"}]'

# Install nginx ingress
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm install --create-namespace --namespace ingress-nginx ingress-nginx ingress-nginx/ingress-nginx

# Install metrics
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
kubectl -n kube-system patch deployment metrics-server --type 'json' -p '[{"op": "add", "path": "/spec/template/spec/containers/0/args/-", "value": "--kubelet-insecure-tls"}]'
kubectl get --raw /apis/metrics.k8s.io/v1beta1/nodes

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

## Step 2: Compile the from source code

```bash
# Compile the backend
PROJECT_PATH=$PWD MAVEN_CONF=$PWD/settings.xml docker-compose -f build-java.yml run build
# Compile the frontend
PROJECT_PATH=$PWD/frontend-helloworld docker-compose -f build-npm.yml run build
PROJECT_PATH=$PWD/frontend-helloworld YARNRC=$PWD/yarnrc docker-compose -f build-yarn.yml run build
# Start the Sonarqube, then setup the project to get the login key.
docker-compose -f sonar-server.yml up -d
# Scan the java project
PROJECT_PATH=$PWD SONAR_PROJECT=java SONAR_LOGIN=b826bae927c48de56e0a77d911f7f4af25520dc4 SONAR_OPTS="" docker-compose -f sonarscanner.yml run scan
# Scan the js project
PROJECT_PATH=$PWD/frontend-helloworld SONAR_PROJECT=js SONAR_LOGIN=b826bae927c48de56e0a77d911f7f4af25520dc4 SONAR_OPTS="" docker-compose -f sonarscanner.yml run scan
```
## Step3: Build the docker images

```bash
cat <<EOF |xargs -L 1 -I {} sh -c "git log -1 --pretty=format:\"COMMIT=%h docker-compose -f docker-compose.yml build {};\" {}" |bash
server-eureka
server-config
server-zuul
service-book
service-library
service-read
frontend-helloworld
EOF

docker image ls
```

## Step 4. Test in the docker compose

```bash
# Start service
cat <<EOF |xargs -L 1 -I {} sh -c "git log -1 --pretty=format:\"COMMIT=%h docker-compose -f docker-compose.yml up --no-deps -d {};\" {}" |bash
server-eureka
server-config
server-zuul
service-book
service-library
service-read
frontend-helloworld
EOF

# Test dockers
docker ps

# Open Eureka Dashboard:
open http://`minikube ip`:8761/
# Query books: 
open http://`minikube ip`:9000/book-service/books
# Query librarys: 
open http://`minikube ip`:9000/library-service/librarys
# Query read:
open http://`minikube ip`:9000/read-service/read/Book2

# Stop service
cat <<EOF |xargs -L 1 -I {} sh -c "git log -1 --pretty=format:\"COMMIT=%h docker-compose -f docker-compose.yml stop {};\" {}" |bash
server-eureka
server-config
server-zuul
service-book
service-library
service-read
frontend-helloworld
EOF
```

## Step 5. Run on kubernetes

```bash
kubectl get nodes
kubectl get all

helm lint ./helm-chart
helm template ./helm-chart
helm install --create-namespace --namespace sanyi-erp --debug --dry-run sanyi ./helm-chart
helm upgrade --install --create-namespace --namespace sanyi-erp sanyi ./helm-chart
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
