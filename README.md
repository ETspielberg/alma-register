# Alma Registration via Shibboleth

This standalone Spring Boot Application is designed to use a Shibboleth Service Provider to create a user account in the cloud based integrated library system Alma from ExLibris.

## Requirements

This application runs an embedded tomcat server, which is to be connected to a Web Frontend Server like Apache vie the AJP connector. A fully configured Apache web server with SSL encryption and a working shibboleth-setup are therefore required. Additional Information can be found in the Wiki.

## Configuration

The application needs some additional properties, which can be supplied as environment variables. In particular the following parameters need to be defined:

```
TOMCAT_AJP_SECRET=<secret as defined in the apache ajp connector config>
ALMA_API_USER_KEY=<the API key for Alma with the right to create users.>
``` 

## To Be Done

At the moment an im memory database is defined as dummy datasource. This might become useful if other user data are to aggregated or if data such as are to be provided.  