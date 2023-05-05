FROM openjdk:11
VOLUME /tmp
ADD openAiProject-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c "touch /app.jar"
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]