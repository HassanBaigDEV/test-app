FROM markhobson/maven-chrome

WORKDIR /app
COPY . .
CMD ["mvn", "test"] 