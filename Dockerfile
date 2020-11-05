FROM openjdk:11-jdk
EXPOSE 8080
COPY ${JAR_FILE} ${JAR_FILE}
ADD target/metis-social-lending-service-*.jar metis-social-lending-service.jar
ENTRYPOINT ["java","-jar","/metis-social-lending-service.jar"]
