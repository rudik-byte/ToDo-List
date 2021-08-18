FROM maven:3.8.1-jdk-11-slim AS MAVEN_BUILD

COPY pom.xml /build/
COPY src /build/src

WORKDIR /build/
RUN mvn package

FROM openjdk:11-jre-slim

WORKDIR /app

COPY --from=MAVEN_BUILD /build/target/discount-1.0-SNAPSHOT.jar /app/

ENTRYPOINT ["java","-jar", "discount-1.0-SNAPSHOT.jar"]
#CMD [ "sh", "-c", "java -Dserver.port=$PORT -Xmx280m -jar discount-1.0-SNAPSHOT.jar"] for Heroku