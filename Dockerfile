FROM openjdk:17 
EXPOSE 8080
ADD target/local-cicd.jar local-cicd.jar 
ENTRYPOINT ["java", "-jar", "local-cicd.jar"]
