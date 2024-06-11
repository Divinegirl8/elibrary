FROM maven:3.8.7 as build
COPY src/test/java/com/eLibrary .
RUN mvn -B clean Package -DskipTests
FROM openjdk:17
COPY --from=build ./target/*.jar app.jar
EXPOSE 9491
ENTRYPOINT ["java", "-jar", "app.jar"]


