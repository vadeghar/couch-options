FROM openjdk:11
EXPOSE 8081
RUN echo "Asia/Kolkata" > /etc/timezone
RUN dpkg-reconfigure -f noninteractive tzdata
COPY target/couch-options-1.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]