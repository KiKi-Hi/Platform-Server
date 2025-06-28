FROM eclipse-temurin:17-jdk

WORKDIR /app

ARG JAR_FILE=build/libs/KIKIHI_BE-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# CA 인증서 JDK truststore에 추가
COPY ./config/certs/ca/ca.crt /tmp/ca.crt
RUN keytool -importcert -noprompt \
    -alias elasticsearch-ca \
    -file /tmp/ca.crt \
    -keystore $JAVA_HOME/lib/security/cacerts \
    -storepass changeit

ENTRYPOINT ["java", "-jar", "app.jar"]
