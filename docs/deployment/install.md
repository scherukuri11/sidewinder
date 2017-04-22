## Install
Sidewinder can be installed in one of several different ways depending on the environment your are deploying.

### Pre-requisite
Sidewinder requires Java 8 (Oracle or OpenJDK) which can be installed using:

1. sudo yum install java (Centos 7)

2. sudo apt-get install openjdk-8-jre (Debian)

3. Oracle JRE: http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html


### Centos 6/7
Latest Sidewinder RPMs can be downloaded from Maven central e.g. http://search.maven.org/remotecontent?filepath=com/srotya/sidewinder/sidewinder-core/0.x.x/sidewinder-core-0.x.x.rpm

To install simply run ```sudo rpm -ivf sidewinder-core-0.x.x.rpm```

To upgrade simply run ```sudo rpm -Uv sidewinder-core-0.x.x.rpm```

### Debian
Latest Sidewinder RPMs can be downloaded from Maven central e.g. http://search.maven.org/remotecontent?filepath=com/srotya/sidewinder/sidewinder-core/0.0.18/sidewinder-core-0.0.18.rpm

To install simply run ```sudo dpkg -i sidewinder-core-0.x.x.deb```

### Docker
Official docker image for Sidewinder can be located on Docker Hub: https://hub.docker.com/r/srotya/sidewinder/tags/

## Config
When using package installation, configuration for Sidewinder is located in the /etc/sidewinder folder with the config.yaml file and sidewinder.properties file with necessary configuration files.

## Run
The latest Sidewinder packages (RPM & DEB) also install a Linux service for it which can be run using the

```sudo service sidewinder start``` and logs to ```/var/log/sidewinder.log```
