#!/bin/bash
TAG=$1
docker save -o cryptoquants-ui-$TAG.docker nimo71/cryptoquants-ui:$TAG
tar -cvzf cryptoquants-ui-$TAG.docker.tgz cryptoquants-ui-$TAG.docker
scp -v -i ~/.ssh/NickAws.pem cryptoquants-ui-$TAG.docker.tgz ec2-user@$AWS_HOST:~/
ssh -v -i ~/.ssh/NickAws.pem ec2-user@$AWS_HOST "bash -s" -- < ./run-docker.sh $TAG
git tag $TAG

