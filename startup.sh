#!/bin/bash

docker rm -f learn-hadoop

docker build -t learn-hadoop .

docker run -i -t --name learn-hadoop learn-hadoop /etc/bootstrap.sh -bash