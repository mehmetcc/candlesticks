FROM gradle:jdk19-alpine AS BUILD_STAGE
COPY --chown=gradle:gradle . /home/gradle
RUN gradle build -x test || return 1

FROM amazoncorretto:19 AS RUNNING_STAGE
# TODO ADD CONFIGURATION FILE HERE
COPY --from=BUILD_STAGE /home/gradle/build/libs/candlesticks-web-0.0.1-SNAPSHOT.jar app/

WORKDIR /app
ENTRYPOINT ["java","-jar","candlesticks-web-0.0.1-SNAPSHOT.jar"]
