FROM maven:3.6.2-jdk-11-slim
LABEL Name=xlrt-common-service Version=0.0.1-SNAPSHOT

# Container configuration
RUN mkdir /xlrt-common-service
COPY . /xlrt-common-service
WORKDIR /xlrt-common-service
VOLUME /xlrt-common-service
RUN mvn clean install
