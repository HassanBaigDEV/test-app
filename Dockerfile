FROM markhobson/maven-chrome:latest

WORKDIR /app
COPY . .

# Start MySQL service (if you're running MySQL in the same container)
RUN apt-get update && \
    apt-get install -y default-mysql-server && \
    service mysql start && \
    mysql -e "CREATE DATABASE selenium_test_db;"

# Run tests
CMD service mysql start && mvn test 