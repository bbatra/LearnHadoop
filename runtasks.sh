#!/bin/bash

# remove any existing network with this name
sudo docker network rm hadoop-batra-altieri

# create a network for our nodes to interact
sudo docker network create --driver=bridge hadoop-batra-altieri

# remove the existing image
docker rm -f learn-hadoop

# build the image using the Dockerfile present in this directory
docker build -t learn-hadoop .

# remove any previously running master container
sudo docker rm -f hadoop-master &> /dev/null

# start master cintainer, exposing required ports and using the created network
sudo docker run -itd \
                --net=hadoop-batra-altieri \
                -p 50070:50070 \
                -p 8088:8088 \
                --name hadoop-master \
                --hostname hadoop-master \
                learn-hadoop &> /dev/null



echo "started hadoop-master container"
i=1

#we are going to start 4 client containers, removing any preexisting ones before running new ones
for i in {1..4}
    do
        sudo docker rm -f hadoop-client$i &> /dev/null
        echo "started hadoop-client$i container"
        sudo docker run -itd \
                        --net=hadoop-batra-altieri \
                        --name hadoop-client$i \
                        --hostname hadoop-client$i \
                        learn-hadoop &> /dev/null
        i=$(( $i + 1 ))
    done

echo "Task 1 complete!!! Hadoop Cluster Started"

# Execute the task script inside master container to run the remaining 2 tasks
sudo docker exec -it hadoop-master bash /tasks.sh