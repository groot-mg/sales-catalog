FROM prom/prometheus:v2.35.0

MAINTAINER Mauricio Generoso

# Copy rules
COPY rules/ /etc/prometheus/rules/

# Copy tests
COPY tests/common/ /etc/prometheus/test/

# Copy config
COPY config/prometheus-*.yml /etc/prometheus/

# Checking syntax for configs
WORKDIR /etc/prometheus
RUN promtool check config prometheus-*.yml

# Checking syntax for rules & alerts
WORKDIR /etc/prometheus/rules
RUN promtool check rules common/*_alerts.yml

# Running tests against our rules
WORKDIR /etc/prometheus/test
RUN promtool test rules *.yml