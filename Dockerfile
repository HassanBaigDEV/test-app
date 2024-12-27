FROM markhobson/maven-chrome:latest

WORKDIR /app
COPY . .

CMD ["mvn", "test"] 