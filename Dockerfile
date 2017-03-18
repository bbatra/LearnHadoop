FROM sequenceiq/hadoop-docker:2.7.0

MAINTAINER bbatra

RUN mkdir -p /io
RUN mkdir -p /io/output-bc

COPY io /io
COPY ./WordCount.java /usr/local/hadoop
COPY ./BigramCount.java /usr/local/hadoop
COPY ./Result.java /usr/local/hadoop
COPY ./tasks.sh /


