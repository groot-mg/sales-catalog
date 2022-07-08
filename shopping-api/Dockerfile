FROM amazoncorretto:17-alpine

MAINTAINER Mauricio Generoso

RUN mkdir /apps

COPY ./build/libs/shopping-api.jar /apps/shopping-api.jar

ENTRYPOINT exec java ${JAVA_OPTS} -jar /apps/shopping-api.jar
