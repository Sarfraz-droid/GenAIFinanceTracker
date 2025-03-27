FROM openjdk:17

COPY . .

WORKDIR /app

RUN ./gradlew build

RUN ls -la

ENTRYPOINT ["java","-jar","/app/build/libs/*.jar"]