# upload_file
## An Example for upload heavy file with grpc 
### To add project to your spring project 

Adding Repository to pom.xml
```
 <repositories>
    ...
    <repository>
      <id>repsy</id>
      <name>My Private Maven Repository on Repsy</name>
      <url>https://repo.repsy.io/mvn/phamminhtan/upload_file</url>
    </repository>
    ...
  </repositories>
 ```
  
  Adding package below for maven
 ```
 <dependency>
  <groupId>com.phamtan</groupId>
  <artifactId>upload_file</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```
 Or ... for grandle
 ```
 implementation 'com.phamtan:upload_file:0.0.1-SNAPSHOT'
 ```
  
