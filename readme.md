
# Rest Mock
* This application allows you to mock HTTP responses with a local http server
* This project is based on Maven 3.3, Java 8 and Spring Boot, so before you can run this server you need to install and configure these tools

## Build and Start-up
* First you must must build it with "mvn clean package -DskipTests=true"
* Second copy "/target/restmock.jar" into the project directory.
* Finally start it with "java -jar restmock.jar". By default the server listen on port 18080

## Configuration
* By default the property "app.response.file.basedir" points to the directory "./responses" where you can find a file named "matcher-config.json", here you can define all your mocked responses.
* The configuration consist of three parts common configuration attributes, a request object and a response object. A request object defines rules when this configuration is applied to incoming requests. The response object defines the response behavior for matched requests.
```text
  {
    "name": "Login-Matcher",
    "description": "Define a mocked response for login requests",
    "request": {
      "method": "POST",             # If http method of request is equal to 'POST'
      "url": "regex::/v1/login.*"   # ... and incomming URL is matching
      "body": "regex::.*",          # ... and incomming body is matching
      "header": {
        "name": "myHeaderName",     # ... and incomming header name is matching
        "value": "regex::.*myHeaderValue.*" # ... and incomming header value is matching
       }
    },
    "response": {
      "statusCode": 200,                # ... then return a HTTP Status code 200
      "body": "file::/api/login.json"   # ... and return file content as body
      "contentType": "application/json" # ... and set value of content-type header
    }
  }
```

## Endpoints
* http://localhost:18080/matcher/config : list configuration
* http://localhost:18080/matching : Test your configuration with sample input