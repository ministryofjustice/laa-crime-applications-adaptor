FROM amazoncorretto:17-alpine
RUN mkdir -p /opt/laa-crime-applications-adaptor/
WORKDIR /opt/laa-crown-court-contribution/
COPY ./build/libs/crime-applications-adaptor.jar /opt/laa-crime-applications-adaptor/app.jar
RUN addgroup -S appgroup && adduser -u 1001 -S appuser -G appgroup
USER 1001
EXPOSE 8088 8099
ENTRYPOINT ["java","-jar","app.jar"]
