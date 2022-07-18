FROM amazoncorretto:17-alpine

MAINTAINER Mauricio Generoso

RUN mkdir /apps

COPY build/libs/gateway.jar /apps/gateway.jar

RUN apk add --update \
    curl \
    && rm -rf /var/cache/apk/*

ENTRYPOINT exec java -jar /apps/gateway.jar
