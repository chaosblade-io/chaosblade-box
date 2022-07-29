FROM openjdk:8

WORKDIR /app

# install helm
#RUN wget https://get.helm.sh/helm-v3.5.3-linux-amd64.tar.gz \
#    && tar -zxvf helm-v3.5.3-linux-amd64.tar.gz \
#    && mv linux-amd64/helm /usr/local/bin/helm \
#    && rm -rf helm-v3.5.3-linux-amd64.tar.gz


COPY ./chaosblade-box-starter/target/chaosblade-box-1.0.2.jar .

ENTRYPOINT ["java", "-Duser.timezone=Asia/Shanghai", "-jar", "chaosblade-box-1.0.2.jar"]
