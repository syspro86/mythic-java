FROM openjdk:17 as builder

WORKDIR /app
COPY . /app
RUN chmod +x mvnw
RUN ./mvnw package -DskipTests

FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/mythic-web-0.0.1-SNAPSHOT.war /app/mythic-web.jar
ENV TZ=Asia/Seoul
EXPOSE 8080
ENTRYPOINT [ "java", "-Dspring.config.location=/config/application.yml", "-jar", "/app/mythic-web.jar" ]
