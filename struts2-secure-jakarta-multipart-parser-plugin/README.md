# Apache Struts 2 Extras - secure Jakarta Multipart parser plugin

This plugin provides a safe implementation of the Jakarta Multipart parser from the Struts Core. It can be used
to mitigate vulnerability described in the [S2-045](http://struts.apache.org/docs/s2-045.html) Security Bulletin.
 
You should use this plugin in case you are not able to migrated to the latest Struts version.

## Supported versions

This plugins can be used with the Apache Struts versions 2.5.8 till 2.5.5, if you are running the Apache Struts 2.5.8+
you must migrate to the latest version which is [Struts 2.5.10.1](http://struts.apache.org/announce.html#a20170307).

## How to use it

Just drop the jar into `WEB-INF/libs` folder and restart your application, you can use on of the existing PoCs
to test if everything is ok.

If you are using Maven to build your project, please add the following dependency into your pom:
  
```xml
<dependency>
    <groupId>org.apache.struts</groupId>
    <artifactId>struts2-secure-jakarta-multipart-parser-plugin</artifactId>
    <version>[VERSION]</version>            
</dependency>
```

## Remarks

Please be aware that this is just a temporary solution, you should consider migration to the latest version anyway.
