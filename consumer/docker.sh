#!/usr/bin/env bash

docker build --platform=linux/arm64 -t consumer:1.0 .
docker tag consumer:1.0 jbenitezg/kafka-consumer:1.0
docker push jbenitezg/kafka-consumer:1.0