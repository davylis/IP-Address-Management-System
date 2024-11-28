# IP-Address-Management-System

## IP Pools Main Page

![IP Pools main screen](./assets/ippools.png)

## IP Addresses Page

![IP Addresses screen](./assets/ipaddresses.png)

## IP Services Page

![IP Addresses screen](./assets/ipservices.png)

## Prerequisites

Before running the application, ensure that you have the following software installed on your system:

- **Maven**
- **Docker**
- **Java**

## Environment Configuration

To configure the application, you need to create a `.env` file in the root directory of the repository. This file will contain the necessary environment variables for the web application and database connection.

### Example `.env` File

```dotenv
WEB_APP_PORT="3000"
DB_USERNAME="davylis"
DB_PASSWORD="password"
DB_URL="jdbc:mysql://localhost:3306/database?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"
```

# Deployment Phases

The deployment process consists of two main phases:

1. **Database Setup**
2. **Application Deployment**

## 1. Database Setup

## Start the Database Container

To deploy the database, navigate to the `ip-management-system` folder and execute the following command in your terminal:

```bash
docker-compose -f mysqldb.yaml up -d
```

This command will start the MySQL database container in detached mode.

## 2. Application Deployment

Once the database is up and running, you can deploy the application and connect it to the database.

### Build the Application

In the same folder run the following command to build the application:

```bash
mvn clean package -DskipTests
```

This command will compile the application and package it into a JAR file, skipping the tests for faster execution.

### Run the Application

After the JAR file is built and placed in the `./target` directory, you can run the application using the following command:

```bash
java -jar ./target/ip-management-system-0.0.1-SNAPSHOT.jar
```

## Conclusion

By following these steps, you will successfully deploy the application and connect it to the MySQL database running in a Docker container. If you encounter any issues, please refer to the logs for troubleshooting or consult the project documentation for further assistance.


