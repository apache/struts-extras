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

