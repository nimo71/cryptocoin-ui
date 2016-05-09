#!/bin/bash
TAG=$1
docker save -o cryptocoin-ui-$TAG.docker nimo71/cryptocoin-ui:$TAG
tar -cvzf cryptocoin-ui-$TAG.docker.tgz cryptocoin-ui-$TAG.docker
scp -v -i ~/.ssh/NickAws.pem cryptocoin-ui-$TAG.docker.tgz ec2-user@$AWS_HOST:~/
ssh -v -i ~/.ssh/NickAws.pem ec2-user@$AWS_HOST "bash -s" -- < ./run-docker.sh $TAG

