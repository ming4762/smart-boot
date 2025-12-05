FROM openjdk:21-oracle

WORKDIR /app
VOLUME /tmp

LABEL org.opencontainers.image.authors="zhongming4762"

COPY build/app.jar app.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar ${JAVA_OPTS2}"]