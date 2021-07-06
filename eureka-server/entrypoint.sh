#!/bin/bash

echo "rm -rf /root/.m2/repository"
rm -rf /root/.m2/repository
echo "unzip /maven-repository/repository.zip -d /root/.m2/"
unzip /maven-repository/repository.zip -d /root/.m2/

echo "mv -f /spring-cloud-ergouzi/eureka-server/settings-docker.xml  /root/.m2/settings-docker.xml"
mv -f /spring-cloud-ergouzi/settings-docker.xml  /root/.m2/settings-docker.xml
echo "rm -rf src/main/resources/application.yml"
echo "cp -a /spring-cloud-ergouzi/application.yml /spring-cloud-ergouzi/eureka-server/src/main/resources/application.yml"
rm -rf src/main/resources/application.yml
mv /spring-cloud-ergouzi/eureka-server/application.yml src/main/resources/application.yml

echo "mvn -f pom.xml clean package"
mvn -f pom.xml clean package

echo "cd /spring-cloud-ergouzi/eureka-server/target"
cd target

java -jar eureka-server-0.0.1.jar