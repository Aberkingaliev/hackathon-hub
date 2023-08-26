FROM openjdk:17
RUN microdnf install findutils


WORKDIR .

COPY gradlew .
COPY gradle gradle
COPY build.gradle .

RUN ./gradlew build --no-daemon

COPY ./src ./src

RUN ./gradlew bootJar --no-daemon

EXPOSE 0

CMD ["java", "-jar", "./build/libs/service-auth-0.0.1-SNAPSHOT.jar"]