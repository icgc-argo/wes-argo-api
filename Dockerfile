FROM openjdk:11-jdk as builder
WORKDIR /usr/src/app
ADD . .
RUN ./mvnw clean package

FROM openjdk:11-jre-slim
COPY --from=builder /usr/src/app/target/wes-argo-api-*.jar /usr/bin/wes-argo-api.jar
RUN adduser --disabled-password --disabled-login --quiet --gecos '' search
USER search
CMD ["java", "-ea", "-jar", "/usr/bin/wes-argo-api.jar"]
EXPOSE 8080/tcp
