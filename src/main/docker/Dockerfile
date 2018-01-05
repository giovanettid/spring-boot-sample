FROM java:9

COPY maven/spring-boot-sample*.jar /spring-boot-sample.jar
COPY healthcheck.sh /healthcheck.sh

EXPOSE 8080

HEALTHCHECK --interval=3s --timeout=3s --retries=10 CMD /healthcheck.sh

ENTRYPOINT bash -c "java -jar /spring-boot-sample.jar --spring.config.location=file:/properties/application.properties 2>&1 | tee /var/log/restapp.log"