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

In addition it requires a database backend to store the student's data. Currently, a PostgreSQL database is used.

## Configuration

The application needs some additional properties, which can be supplied as environment variables. In particular the following parameters need to be defined:

```
TOMCAT_AJP_PORT=<port used by the AJP connector (8009 by default)>
TOMCAT_AJP_SECRET=<secret as defined in the apache ajp connector config>

ALMA_API_USER_KEY=<the API key for Alma with the right to create users.>
ALMA_REDIRECT_URL=<the rediurect url supplied as button after succesfull registration>
ALMA_REGISTER_MASTER_TEMPLATE=<server where the master template is taken from>
ALMA_REGISTER_MASTER_TEMPLATE_PATH=<path on the template server>

SPRING_DATASOURCE_HIS_PASSWORD=<the password for the database to store the his export data>

LIBINTEL_PROFILE=<the profile to be used, e.g. dev, prod, test.>
LIBINTEL_DATA_DIR=<defines where the downloaded files, templates, and locale files are stored, usually /home/<user>/.almaregister>
LIBINTEL_ADDRESS_WS_USERNAME=<for the adress soap web service>
LIBINTEL_ADDRESS_WS_URL=<the url for the address soap web service>
LIBINTEL_ADDRESS_WS_PASSWORD=<the password for the address soap web service>

HIS_DATA_SERVER=<the server to download the students data from>
HIS_DATA_USERNAME=<the username for the students data server>
HIS_DATA_PASSWORD=<the password for the students data server>

LIBINTEL_ALMA_REGISTER_HEADER_NAME=<the header or request parameter name to use the REST adress API>
LIBINTEL_ALMA_REGISTER_HEADER_VALUE=<the value of the api-key to use the REST adress API>
``` 

## External Locales

The files for the locales are stored under ````LIBINTEL_DATA_DIR/locales```` as messages_XX.properties files. They can be separately managed by the version management. Currently the locales branch of this repository is used.   