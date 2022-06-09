FROM prom/alertmanager:v0.24.0

MAINTAINER Mauricio Generoso

COPY config/alertmanager-*.yml /etc/alertmanager/
#COPY templates/* /etc/alertmanager/

# Checking syntax for configs
WORKDIR /etc/alertmanager
RUN amtool check-config alertmanager-*.yml
