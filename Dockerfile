FROM openjdk:17

RUN microdnf install findutils

COPY . .

RUN ls -la

RUN ./gradlew clean build


ENTRYPOINT ["java","-jar","/app/build/libs/*.jar"]