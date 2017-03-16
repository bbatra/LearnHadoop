#move BigramCount.java & WordCount.java into docker at /usr/local/hadoop aka $HADOOP_PREFIX

docker run -it sequenceiq/hadoop-docker:2.7.0 /etc/bootstrap.sh -bash

#inside Docker
export JAVA_HOME=/usr/java/default
export PATH=${JAVA_HOME}/bin:${PATH}
export HADOOP_CLASSPATH=${JAVA_HOME}/lib/tools.jar
cd $HADOOP_PREFIX

bin/hadoop fs -moveFromLocal /io/ io

bin/hadoop com.sun.tools.javac.Main BigramCount.java
#bin/hadoop com.sun.tools.javac.Main BigramCountSort.java

jar cf bc.jar BigramCount*.class
#jar cf bcs.jar BigramCountSort*.class

#bin/hadoop jar bc.jar BigramCount ./io/input ./io/output1
bin/hadoop jar bcs.jar BigramCountSort ./io/input ./io/output

bin/hadoop fs -cat ./io/output/part-r-00000