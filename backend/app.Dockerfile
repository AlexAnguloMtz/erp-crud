# Build
FROM amazoncorretto:22-alpine-jdk as builder

WORKDIR /opt/app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Set executable permissions for mvnw
RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline

COPY src/ src/

RUN ./mvnw clean package -DskipTests

# Run
FROM amazoncorretto:22-alpine-jdk

WORKDIR /opt/app

EXPOSE 8080

COPY --from=builder /opt/app/target/*.jar /opt/app/app.jar

ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]
