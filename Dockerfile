ARG JDK_IMAGE=eclipse-temurin:17-jdk-alpine
ARG JRE_IMAGE=eclipse-temurin:17-jre-alpine

FROM ${JDK_IMAGE} as build

COPY / /build
WORKDIR /build

RUN chmod +x ./mvnw && \
  ./mvnw clean package -B

FROM ${JRE_IMAGE}

RUN apk add --no-cache curl && \
  mkdir /app && \
  adduser -S -D -h /app appuser && \
  chown -R appuser /app

COPY --from=build /build/target/asteroidhunt.jar /app/

USER appuser

CMD ["java", "-jar", "/app/asteroidhunt.jar"]
