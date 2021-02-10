FROM openjdk:8

WORKDIR /app

COPY ./chaos-platform-web/target/chaos-platform-web-0.0.2-alpha.jar .

ENTRYPOINT ["java", "-Duser.timezone=Asia/Shanghai", "-jar", "chaos-platform-web-0.0.2-alpha.jar"]
