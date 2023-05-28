FROM maven:3.6.3-openjdk-17

WORKDIR /demo
COPY . .
RUN mvn clean install

CMD mvn spring-boot:run