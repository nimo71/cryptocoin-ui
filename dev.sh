#!/bin/bash
docker run -i -t -v $1:/root/workspace -p 3449:3449 nimo71/clojure-dev:1.0.0
