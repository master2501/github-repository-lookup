
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
