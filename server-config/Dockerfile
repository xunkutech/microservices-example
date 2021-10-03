FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
RUN rm -f /etc/localtime &&  ln -s ../usr/share/zoneinfo/Asia/Shanghai /etc/localtime 
ENTRYPOINT [ \
"java", \
"-noverify", \
"-XX:TieredStopAtLevel=1", \
"-XX:+UnlockExperimentalVMOptions", \
"-XX:+UseCGroupMemoryLimitForHeap", \
"-DSERVER_PORT=${SERVER_PORT}", \
"-DEUREKA_URI=http://${EUREKA_HOSTNAME}:${EUREKA_PORT}/eureka", \
"-cp","app:app/lib/*", \
"com.xunkutech.tuturial.springcloud.server.config.ConfigServerApplication" \
]