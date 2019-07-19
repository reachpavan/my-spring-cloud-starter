FROM java:8-jre

ADD ./target/fibonacci-service.jar /app/
CMD ["java", "-Xmx2G", "-jar", "/app/fibonacci-service.jar"]

EXPOSE 8080