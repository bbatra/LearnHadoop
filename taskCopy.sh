#!/bin/bash

sudo docker network rm hadoop

sudo docker network create --driver=bridge hadoop

sudo docker rm -f learn-hadoop

sudo docker build -t learn-hadoop .

sudo docker rm -f hadoop-master &> /dev/null

echo "start hadoop-master container..."

sudo docker run -itd \
                --net=hadoop \
                -p 50070:50070 \
                -p 8088:8088 \
                --name hadoop-master \
                --hostname hadoop-master \
                learn-hadoop &> /dev/null


# start hadoop slave container
i=1
for i in {1..4}
    do
        sudo docker rm -f hadoop-slave$i &> /dev/null
        echo "start hadoop-slave$i container..."
        sudo docker run -itd \
                        --net=hadoop \
                        --name hadoop-slave$i \
                        --hostname hadoop-slave$i \
                        learn-hadoop &> /dev/null
        i=$(( $i + 1 ))
    done

echo "Started Hadoop Cluster"
echo "Docker Nodes "

docker ps

# get into hadoop master container
sudo docker exec -it hadoop-master bash