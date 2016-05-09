#!/bin/bash
TAG=$1
tar -xzvf cryptocoin-ui-$TAG.docker.tgz
docker load -i cryptocoin-ui-$TAG.docker
rm cryptocoin-ui-$TAG.docker
docker stop $(docker ps -a -q)
docker run -d -p 80:80 nimo71/cryptocoin-ui:$TAG
