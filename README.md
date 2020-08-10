# Alma Registration via Shibboleth

This standalone Spring Boot Application is designed to use a Shibboleth Service Provider to create a user account in the cloud based integrated library system Alma from ExLibris.

## Prerequisites

This application is tested to run on Java 11 (openjdk 11) with Maven installed. It requires the package alma-shared (https://git.uni-due.de/ub/alma-shared or https://github.com/ETspielberg/alma-shared) to be made available in the local repository. For Maven:

```
git clone https://git.uni-due.de/ub/alma-shared
cd alma-shared
mvn install 
```

## Requirements

This application runs an embedded tomcat server, which is to be connected to a Web Frontend Server like Apache vie the AJP connector. A fully configured Apache web server with SSL encryption and a working shibboleth-setup are therefore required. Additional Information can be found in the Wiki.

## Configuration

The application needs some additional properties, which can be supplied as environment variables. In particular the following parameters need to be defined:

```
TOMCAT_AJP_PORT=<port used by the AJP connector (8009 by default)>
TOMCAT_AJP_SECRET=<secret as defined in the apache ajp connector config>
LIBINTEL_DATA_DIR=<path to the data directory, e.g. /home/<user>/.almaregister>
ALMA_API_USER_KEY=<the API key for Alma with the right to create users.>
ALMA_REDIRECT_URL=<the rediurect url supplied as button after succesfull registration>
ALMA_REGISTER_MASTER_TEMPLATE=<server where the master template is taken from>
ALMA_REGISTER_MASTER_TEMPLATE_PATH=<path on the template server>
``` 

## To Be Done

At the moment an im memory database is defined as dummy datasource. This might become useful if other user data are to aggregated or if data such as are to be provided.  