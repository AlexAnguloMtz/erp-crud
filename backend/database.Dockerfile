FROM postgres:16.2

WORKDIR /usr/local/bin

COPY ./scripts/postgres-healthcheck.sh /usr/local/bin/postgres-healthcheck.sh

RUN chmod +x /usr/local/bin/postgres-healthcheck.sh