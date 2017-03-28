# Apache Struts 2 Extras - custom results plugin

[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

This plugin provides additional result types.

## Supported versions

This plugin can be used with the Apache Struts versions 2.5.x .

## Result Type categories

### SSL offloading support

These result types are made to support redirecting under the https protocol while being behind a SSL offloading proxy.

#### Result Types

##### sslOffloadRedirect

This Result Type should replace the `redirect` Result Type.
It takes into account 2 ways of detecting the offloading:
- `X-Forwarded-Proto` header (de-facto standard header)
- `proto` attribute of the `Forwarded` header ( [RFC7239](https://tools.ietf.org/html/rfc7239) )

##### sslOffloadRedirectAction

This Result Type should replace the `redirectAction` Result Type.
It takes into account 2 ways of detecting the offloading:
- `X-Forwarded-Proto` header (de-facto standard header)
- `proto` attribute of the `Forwarded` header ( [RFC7239](https://tools.ietf.org/html/rfc7239) )

#### struts packages

##### ssl-offload

Only thing this package does is defining the result types so that they can be used.

##### ssl-offload-default

Merely combines the `struts-default` package and the `ssl-offload` package.

#### How to use

The requirement for using these Result Types is that they are defined.

This could be done in a couple ways:

*Defining the types manualy in your own package*

The Result Types can be registered in your own package as follows:

```xml
    <package name="myPackage" extends="struts-default">
        <result-types>
            <result-type name="sslOffloadRedirect" class="org.apache.struts2.result.SslOffloadAwareServletRedirectResult"/>
            <result-type name="sslOffloadRedirectAction" class="org.apache.struts2.result.SslOffloadAwareServletActionRedirectResult"/>
        </result-types>
        ...
    </package>
```

*Using the ssl-offload package as a parent*

You can also just specify the `ssl-offload` package as a parent your package extends from.

```xml
    <package name="myPackage" extends="struts-default, ssl-offload">
        ...
    </package>
```

*Using the ssl-offload-default as a parent*

You can also just specify the `ssl-offload-default` package as a parent your package extends from. This should have the same effect as extending both `struts-default` and `ssl-offload`

```xml
    <package name="myPackage" extends="ssl-offload-default">
        ...
    </package>
```
