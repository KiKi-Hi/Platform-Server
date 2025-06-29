#!/bin/bash

CERT_PATH=/usr/share/spring/config/certs/ca/ca.crt
TRUSTSTORE=$JAVA_HOME/lib/security/cacerts
ALIAS=elasticsearch-ca
PASSWORD=${TRUSTSTORE_PASSWORD}

if [ -f "$CERT_PATH" ]; then
  echo ">>> Registering $CERT_PATH into JVM TrustStore..."
  keytool -delete -alias $ALIAS -keystore $TRUSTSTORE -storepass "$PASSWORD" -noprompt 2>/dev/null || true
  keytool -importcert -trustcacerts -file $CERT_PATH -alias $ALIAS \
    -keystore $TRUSTSTORE -storepass "$PASSWORD" -noprompt
  echo ">>> Certificate registration completed."
else
  echo ">>> Certificate not found at $CERT_PATH"
fi

exec java -jar /app/app.jar
