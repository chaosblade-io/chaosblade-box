FROM openjdk:8

WORKDIR /app

# install helm
RUN wget https://get.helm.sh/helm-v3.5.3-linux-amd64.tar.gz \
    && tar -zxvf helm-v3.5.3-linux-amd64.tar.gz \
    && mv linux-amd64/helm /usr/local/bin/helm \
    && rm -rf helm-v3.5.3-linux-amd64.tar.gz

COPY ./chaosblade-box-web/target/chaosblade-box-web-0.4.2.jar .

ENTRYPOINT ["java", "-Duser.timezone=Asia/Shanghai", "-jar", "chaosblade-box-web-0.4.2.jar"]
