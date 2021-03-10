FROM openjdk:8

WORKDIR /app

COPY ./chaosblade-box-web/target/chaosblade-box-web-0.3.1.jar .

ENTRYPOINT ["java", "-Duser.timezone=Asia/Shanghai", "-jar", "chaosblade-box-web-0.3.1.jar"]
