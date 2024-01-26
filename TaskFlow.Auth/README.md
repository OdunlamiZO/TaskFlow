# TaskFlow.Auth

TaskFlow authorization server built with the Spring Authorization Server project.

## Getting Started

To start a development server, please follow these simple steps.

### Prerequisites

Here is what you need to be able to run TaskFlow.Auth

* [Java](https://www.oracle.com/java/technologies/downloads/) (Version: >= 17.x)
* [Maven](https://maven.apache.org/download.cgi) (Version: >= 3.8.x)
* [MYSQL](https://dev.mysql.com/downloads/installer/) (Version: >= 8.x)
* [Maildev](https://github.com/maildev/maildev)

### Database Setup

Having installed MYSQL, you need to make ready the database by doing the following:

1. Run the following commands at the mysql prompt:

```
mysql> create database taskflow;
mysql> create user 'spring'@'%' identified by 'Your password';
mysql> grant all on taskflow.* to 'spring'@'%';
```

This creates a user and a database named spring and taskflow respectively, and grants all permissions on taskflow to user spring.

2. Create the tables oauth2_authorization, oauth2_registered_client and oauth2_authorization_consent using their respective schemas, by running the following at the mysql prompt:

```
mysql> source oauth2-registered-client-schema.sql
mysql> source oauth2-authorization-schema.sql
mysql> source oauth2-authorization-consent-schema.sql
```

### Set Environment Variables

* SPRING_PASSWORD - Password for MYSQL user created in earlier step

### Running the Application

Start [Maildev](https://github.com/maildev/maildev) server first, since it's a dependency. Then, start the spring boot server using the following:

```
mvn spring-boot:run
```

Access application via http://127.0.0.1:9000