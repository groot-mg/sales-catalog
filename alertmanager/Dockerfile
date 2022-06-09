FROM prom/alertmanager:v0.24.0

MAINTAINER Mauricio Generoso

ARG ENVIRONMENT

COPY config/alertmanager-$ENVIRONMENT.yml /etc/alertmanager/alertmanager.yml
COPY templates/* /etc/alertmanager/
