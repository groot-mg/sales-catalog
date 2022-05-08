FROM amazoncorretto:11-alpine-jdk

MAINTAINER Mauricio Generoso

RUN mkdir /apps

COPY ./build/libs/spring-crud-api-0.0.1-SNAPSHOT.jar /apps/spring-crud-api-0.0.1-SNAPSHOT.jar

ENTRYPOINT exec java ${JAVA_OPTS} -jar /apps/spring-crud-api-0.0.1-SNAPSHOT.jar
