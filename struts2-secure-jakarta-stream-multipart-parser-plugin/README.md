# Apache Struts 2 Extras - Secure Jakarta Stream Multipart parser plugin

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.apache.struts/struts2-secure-jakarta-stream-multipart-parser-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.apache.struts/struts2-secure-jakarta-stream-multipart-parser-plugin/)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

This plugin provides a safe implementation of the Jakarta Stream Multipart parser from the Struts Core. It can be used
to mitigate vulnerability described in the [S2-045](http://struts.apache.org/docs/s2-045.html) Security Bulletin.
 
You should use this plugin in case you are not able to migrate to the latest Struts version.

## Supported versions

This plugins can be used with the Apache Struts versions 2.3.20 till 2.5.5, if you are running the Apache Struts 2.5.8+
you must migrate to the latest version which is [Struts 2.5.10.1](http://struts.apache.org/announce.html#a20170307).

## How to use it

Just drop the jar into `WEB-INF/libs` folder and add the bellow definition into your `struts.xml`:

- if you are running the Apache Struts 2.3.8 - 2.3.31
 ```xml
      <bean type="org.apache.struts2.dispatcher.multipart.MultiPartRequest"
            class="org.apache.struts.extras.SecureJakartaStreamMultiPartRequest"
            name="secure-jakarta-stream"
            scope="default"/>
    
      <constant name="struts.multipart.parser" value="secure-jakarta-stream"/>
 ```

- if you are running the Apache Struts 2.5 - 2.5.5
 ```xml
      <bean type="org.apache.struts2.dispatcher.multipart.MultiPartRequest"
            class="org.apache.struts.extras.SecureJakartaStreamMultiPartRequest"
            name="secure-jakarta-stream"
            scope="prototype"/>
    
      <constant name="struts.multipart.parser" value="secure-jakarta-stream"/> 
 ```

and then restart your application, you can use one of the existing PoCs to test if everything is ok.

If you are using Maven to build your project, please add the following dependency into your pom:
  
```xml
<dependency>
    <groupId>org.apache.struts</groupId>
    <artifactId>struts2-secure-jakarta-stream-multipart-parser-plugin</artifactId>
    <version>1.1</version>            
</dependency>
```

If you are not building with Maven or you simply need the Jar to drop it into an existing Struts 2 based application deployment, 
you can [download it directly from Maven Central](http://search.maven.org/remotecontent?filepath=org/apache/struts/struts2-secure-jakarta-stream-multipart-parser-plugin/1.1/struts2-secure-jakarta-stream-multipart-parser-plugin-1.1.jar).

## Remarks

Please be aware that this is just a temporary solution, you should consider migration to the latest version anyway.
