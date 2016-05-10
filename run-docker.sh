#!/bin/bash
TAG=$1
tar -xzvf cryptoquants-ui-$TAG.docker.tgz
docker load -i cryptoquants-ui-$TAG.docker
rm cryptoquants-ui-$TAG.docker
docker stop $(docker ps -a -q)
docker run -d -p 80:80 nimo71/cryptoquants-ui:$TAG
