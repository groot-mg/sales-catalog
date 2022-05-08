FROM amazoncorretto:11-alpine-jdk

MAINTAINER Mauricio Generoso

RUN mkdir /apps

COPY ./build/libs/spring-crud-api.jar /apps/spring-crud-api.jar

ENTRYPOINT exec java ${JAVA_OPTS} -jar /apps/spring-crud-api.jar
