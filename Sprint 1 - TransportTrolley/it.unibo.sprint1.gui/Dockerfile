FROM gradle:7.3-jdk11

EXPOSE 8081/tcp
EXPOSE 8081/udp

COPY src/ /app/web/src/
COPY build.gradle.kts /app/web/
COPY settings.gradle.kts /app/web/
COPY Californium.properties /app/web/
COPY unibolibs/ /app/web/unibolibs/

WORKDIR /app/web

CMD ["gradle", "bootRun"]