FROM amazoncorretto:20
WORKDIR /app
COPY build/libs/rcsp5-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 80
CMD ["java", "-D spring.datasource.url=jdbc:postgresql://host.docker.internal:5432/rcsp5", "-jar", "app.jar"]
