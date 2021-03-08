FROM openjdk:8

WORKDIR /app

COPY ./chaosbox-web/target/chaosbox-web-0.3.0.jar .

ENTRYPOINT ["java", "-Duser.timezone=Asia/Shanghai", "-jar", "chaosbox-web-0.3.0.jar"]
