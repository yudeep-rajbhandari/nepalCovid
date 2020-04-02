FROM openjdk:8
ADD target/covidJar.jar covidJar.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "covidJar.jar"]