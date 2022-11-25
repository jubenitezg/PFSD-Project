#!/usr/bin/env bash

docker build --platform=linux/arm64 -t producer:1.0 .
docker tag producer:1.0 jbenitezg/kafka-producer:1.0
docker push jbenitezg/kafka-producer:1.0