
# Github-repository-service
This project retrieves repository information, and related branches, for a given username.

# Required dependencies
- Maven
- Git
- JDK 1.8

# How to clone this project
To clone the project, just run the following command in the command-line:
- git clone https://github.com/master2501/github-repository-service.git

# How to build the project
To build the project, please use the following commands at the base project folder:
- mvn clean install

# How to run
To run this application, should use the following commands:
- java -jar target/github-repository-service-1.0.0.jar

# How to build docker image
To build the docker image, please use the following command:
- docker build -t github-repository-service .

# How to run the docker image
To run the docker image, run the following command:
- docker run -p 8080:8080 github-repository-service

# How to make a request, via postman, to retrieve all the repositories for a given user
To make a request using postman, please use the following command:

1) Define the headers:
- Accept: application/json
- username: _username_
- token: _token_

2) Define the http method to 'GET'
3) Define the url as 'localhost:8080/api/v1/repository'
4) press send and check the response

# How to make a request, via wget, to retrieve all the repositories for a given user
wget --no-check-certificate --quiet \
  --method GET \
  --timeout=0 \
  --header 'Accept: application/json' \
  --header 'username: _username_' \
  --header 'token: _token_' \
   'localhost:8080/api/v1/repository'

# How to make a request, via curl, to retrieve all the repositories for a given user
curl --location 'localhost:8080/api/v1/repository' \
--header 'Accept: application/json' \
--header 'username: _username_' \
--header 'token: _token_'


