# IP-Address-Management-System

## Introduction
This web-based application are designed to streamline the management of IP addresses and associated network recources. With this application you can monitor, and maintain IP address pools, track device assigments, and manage related network configurations.

## Features
- **User Authentication**: Secured user authentication to access the application.
- **Role-Based Access Control**:User roles(Admin, User) with speciific permissions for actions like adding, editing, or deleting IP addresses.
- **IP Management**: Create, view, update and delete IP pools, including defining start and end IP ranges and gateways.
- **IP Address Management**:Add, edit, delete, and assign individual IP addresses to devices or services.
- **Services Association**:Link IP addresses with specific network services and track their current usage and status.
- **Service Status Monitoring**: View and update service statuses associated with IP addresses.
- **Responsive Design**:Mobile-friendly interface.

## IP Pools Main Page
![IP Pools main screen](./assets/ippools.png)

## IP Addresses Page
![IP Addresses screen](./assets/ipaddresses.png)

## IP Services Page
![IP Addresses screen](./assets/ipservices.png)

```
username: user
password: user
```

## Technologies Used
- **Spring Boot**
- **Spring Security**
- **MySQL**

# Deployment 

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

## Deployment Phases
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
### 🔴 NOTICE
After running the application for the first time, remember to comment out the lines of code below in the `IpManagementSystemApplication.java` file
```java
// Save ipPools(comment these lines!)
ipPoolRepository.save(ipPool1);
ipPoolRepository.save(ipPool2);
// Save ipAddresses(comment these lines!)
ipAddressRepository.save(ip1);
ipAddressRepository.save(ip2);
ipAddressRepository.save(ip3);
// Save Services(comment these lines!)
serviceRepository.save(service1);
serviceRepository.save(service12);
serviceRepository.save(service2);
serviceRepository.save(service3);
// Save Users(comment these lines!)
userRepository.save(user1);
userRepository.save(user2);
```
This is necessary because the data has already been saved to your database. You don't want to save the same items repeatedly, don't you?

