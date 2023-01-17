FROM openjdk:20-ea-17-jdk
ARG JAR_FILE=build/libs/prolog-1.0.0.jar
COPY ${JAR_FILE} /prolog.jar
ENTRYPOINT ["java", "-jar", "/prolog.jar"]