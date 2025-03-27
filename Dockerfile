FROM openjdk:17

COPY . .

RUN ls -la

RUN ./gradlew build


ENTRYPOINT ["java","-jar","/app/build/libs/*.jar"]