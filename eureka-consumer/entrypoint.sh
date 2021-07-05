#!/bin/bash


echo "rm -rf /root/.m2/repository"
rm -rf /root/.m2/repository
echo "unzip /maven-repository/repository.zip -d /root/.m2/"
unzip /maven-repository/repository.zip -d /root/.m2/


#echo "rm -rf src/main/resources/application.yml"
#rm -rf src/main/resources/application.yml
#echo "mv bootstrap.properties src/main/resources/bootstrap.properties"
#mv bootstrap.properties src/main/resources/bootstrap.properties

cd ..


cd eureka-consumer
echo "mvn -f pom.xml clean package"
mvn -f pom.xml clean package

echo "cd target"
cd target

echo "java -jar eureka-consumer-0.0.1.jar"
java -jar eureka-consumer-0.0.1.jar