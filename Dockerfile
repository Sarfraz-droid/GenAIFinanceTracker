FROM openjdk:17

COPY . /app

WORKDIR /app

RUN ./gradlew build

ENTRYPOINT ["java","-jar","/app/build/libs/*.jar"]