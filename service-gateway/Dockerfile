FROM arm64v8/openjdk:17-ea-3-jdk-slim
ARG VERSION=0.0.1
ADD /build/libs/service-gateway-${VERSION}-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-D java.security.egd=file:/dev/./urandom","-jar","/app.jar"]