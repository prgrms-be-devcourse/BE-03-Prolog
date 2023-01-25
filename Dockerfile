FROM openjdk:17.0.2
ARG JAR_FILE=build/libs/prolog-1.0.0.jar
ENV SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL} \
SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME} \
SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD} \
JWT_ISSUER=${JWT_ISSUER} \
JWT_SECRET_KEY=${JWT_SECRET_KEY} \
CLIENT_ID=${CLIENT_ID} \
CLIENT_SECRET=${CLIENT_SECRET} \
REDIRECT_URI=${REDIRECT_URI}
COPY ${JAR_FILE} prolog.jar
ENTRYPOINT ["java", "-jar", "/prolog.jar"]
