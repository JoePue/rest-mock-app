
# Rest Mock
* This application allows you to mock HTTP responses
* This project is based on Maven 3.3, Java 8 and Spring Boot, so before you can run this server you need to install and configure these tools

## Build and Start-up
* First you must must build it with "mvn clean package -DskipTests=true"
* Second copy "/target/restmock.jar" into the project directory.
* Finally start it with "java -jar restmock.jar". By default the server listen on port 18080

## Configuration
* By default the property "app.response.file.basedir" points to the directory "./responses" where you can find a file named "matcher-config.json", here you can define all your mocked responses.
* The configuration consist of three parts common configuration attributes, a request object and a response object. A request object defines rules when this configuration is applied to incomming requests. The response object defines the response behavior for matched requests.
```text
  {
    "name": "Login-Matcher",
    "description": "Define a mocked response for login requests",
    "request": {
      "method": "POST", # If request with HTTP method is equal to 'POST'
      "urlRegEx": "/v1/login.*" # ... and incomming URL is matching
      "bodyRegEx": ".*", # ... and incomming body is matching
      "bodyContains": "username" # ... and incoming body includes the string "username" ...
    },
    "response": {
      "statusCode": 200, # ... then return a HTTP Status code 200
      "filename": "/api/login.json" # ... and return content of file as body
    }
  }
```

## Endpoints
* http://localhost:18080/matcher/config : list configuration
* http://localhost:18080/matching : Test your configuration with sample input