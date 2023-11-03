FROM amazoncorretto:17-alpine

LABEL MAINTAINER="Mauricio Generoso"

RUN mkdir /apps

COPY build/libs/sales-catalog-app.jar /apps/sales-catalog.jar

RUN apk add --update \
    curl \
    && rm -rf /var/cache/apk/*

ENTRYPOINT exec java -jar /apps/sales-catalog.jar
