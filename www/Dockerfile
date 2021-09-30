FROM nginx:1.21.3-alpine
COPY dist /www
COPY default.conf.template /etc/nginx/templates/
RUN rm -f /etc/localtime &&  ln -s ../usr/share/zoneinfo/Asia/Shanghai /etc/localtime 