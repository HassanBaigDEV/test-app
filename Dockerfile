FROM markhobson/maven-chrome:latest

WORKDIR /app
COPY . .

# Install MySQL
RUN apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y default-mysql-server && \
    mkdir -p /var/run/mysqld && \
    chown -R mysql:mysql /var/run/mysqld

# Initialize MySQL and create database
RUN service mysql start && \
    mysql -e "CREATE DATABASE IF NOT EXISTS selenium_test_db;" && \
    mysql -e "ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';"

# Create startup script
RUN echo '#!/bin/bash\n\
service mysql start\n\
mvn test' > /app/run-tests.sh && \
chmod +x /app/run-tests.sh

CMD ["/app/run-tests.sh"] 