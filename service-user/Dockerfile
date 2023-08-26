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
EXPOSE 55000

CMD ["java", "-jar", "./build/libs/service-user-0.0.1-SNAPSHOT.jar"]