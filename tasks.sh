#!/bin/bash

# run the bootsrap script provided in the hadoop conainer to start Hadoop
/bin/bash /etc/bootstrap.sh

# move into the hadoop directory
cd /usr/local/hadoop

# bring the files from local file system into HDFS
bin/hadoop fs -moveFromLocal /io/ io
bin/hadoop fs -rmdir ./io/output-bc
bin/hadoop fs -rmdir ./io/output-wc

# environment variables to be used for java compilation in hadoop
export JAVA_HOME=/usr/java/default

export PATH=${JAVA_HOME}/bin:${PATH}

export HADOOP_CLASSPATH=${JAVA_HOME}/lib/tools.jar

# compile the jave files in hadoop
bin/hadoop com.sun.tools.javac.Main WordCount.java

# make a jar using the class files
jar cf wc.jar WordCount*.class

# compile the java files in hadoop
bin/hadoop com.sun.tools.javac.Main BigramCount.java

#make a jar using the class files
jar cf bc.jar BigramCount*.class

echo "Running Word Count Example"

# Execute the wordcount JAR
bin/hadoop jar wc.jar WordCount ./io/input ./io/output-wc

echo "Results of Distributed Word Count (Task 2) "

# read output of wordcount from hdfs
bin/hadoop fs -cat ./io/output-wc/part-r-00000

echo "Running Bigram Count"

# Execute the Bigram Count JAR
bin/hadoop jar bc.jar BigramCount ./io/input ./io/output-bc

echo "Results of Distributed Bigram Count (Task 3 part 1) "

# read output of BigramCount from hdfs
bin/hadoop fs -cat ./io/output-bc/part-r-00000

# copy output of BigramCount to local filesystem for processing
bin/hadoop fs -get ./io/output-bc/part-r-00000 ./answer

# calculates the most common Bigram etc.
javac Result.java

echo "Getting the result (Task 3 part 2 & 3) "

java Result


