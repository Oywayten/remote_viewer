# Remote Viewer
> \- simple REST API application to get list of images from ftp server and get image by address.

## About the application
+ The application has four layers: Controller, Service, Persistence, Configuration;
+ When accessing endpoint "/photos", it connects via FTP to the server and searches for files with the specified prefix (e.g. “GRP327_…”) from certain folders. As a result of the execution, it displays a list of found files with their full paths. The prefix and name of the search folders are specified in the application.properties file.
+ There is also an endpoint that allows you to get the image itself, e.g. /photo?path=/Users/Test/Фотозона/фотографии/GRP327_test.jpg

## Technology stack:
```text
+ Java 17,
+ Spring boot 2,
+ Maven 4.
```
## Build the project and run the application
```shell
mvn spring-boot:run
   ```

### Contacts
+ email: [oywayten+git@gmail.com](mailto:oywayten+git@gmail.com)
+ telegram: [@VitaliyJVM](https://t.me/VitaliyJVM/ "go to t.me/VitaliyJVM")