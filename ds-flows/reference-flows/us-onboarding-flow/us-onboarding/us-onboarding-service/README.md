## On-boarding US

## Building

```shell
mvn clean install
```

## Running

### Standalone

```shell
mvn spring-boot:run
```

H2 embedded database will be used.

### IPS profile

The US-onboarding journey uses IPS gateway and registry. Please start IPS platform by running the
following command in the platform folder:

To start **MySQL, Active MQ and NGINX** run:

```shell
docker-compose up
```

To start **Backbase 6 Platform Services**, open another terminal in the same folder and run:

```shell
mvn blade:run
```

To start **Edge Service**, open another terminal in the same folder and run:

```shell
mvn -pl edge package -Prun-edge
```

Once the ips is up and running you can start the project.

### Start project

In onboarding-us/us-onboarding-service-reference run the following command to run the project:

```shell
mvn spring-boot:run -Dspring-boot.run.profiles=eureka
```
H2 embedded database will be used.

### MySql

If you wish to use MySql, use the docker images provided by IPS. Go into the provided platform
folder and start the docker running following command

```shell
docker-compose up
```

After MySql is up and running go into folder us-onboarding-service and run:

```shell
mvn clean -Pclean-database
```
to create the schema.

WARNING: if mysql schema is already present it will be replace with a new empty one.

Start up the application:

```shell
mvn spring-boot:run -Dspring-boot.run.profiles=eureka,local-mysql
```

or for Windows

```shell
mvn spring-boot:run -D"spring-boot.run.profiles=eureka,local-mysql"
```

### Running on Windows

If you are running on Windows you might get the following error depending on the location of Java installation:

```shell
[ERROR] Failed to execute goal org.springframework.boot:spring-boot-maven-plugin:2.3.1.RELEASE:run (default-cli) on project us-onboarding-service-reference: Could not exec java: Cannot run program "C:\Program Files\Java\jdk1.8.0_241\jre\bin\java.exe" (in directory "\onboarding-us-flow\us-onboarding-be-reference\us-onboarding-service-reference"): CreateProcess error=206, The filename or extension is too long
```

In case of such error after compiling, go to the `us-onboarding-service` folder and execute the following command:

```shell
java -jar -D"spring.profiles.active=eureka,local-mysql" target\us-onboarding-service-reference-<version_number>-runnable.jar
```

The `-D"spring.profiles.active=eureka,local-mysql"` portion is optional, and it receives a comma-separated list of arguments that determines which profiles are going to be used when the application is ran, just as in the case of using maven, described above.

The `<version_number>` will depend on the version of the application that you compiled. Check the generated file first to make sure that you are running the correct `runnable` file.

If you run with the `eureka` profile and you get the following error:

```shell
Caused by: com.backbase.buildingblocks.jwt.core.exception.TokenKeyException: Undefined environment variable name: SIG_SECRET_KEY
```

you need to add the `SIG_SECRET_KEY` to the environmental variables in Windows, or, alternatively, in the command line before the previous shown command like this:

```shell
set SIG_SECRET_KEY=JWTSecretKeyDontUseInProduction! & java -jar -D"spring.profiles.active=eureka,local-mysql" target\us-onboarding-service-reference-<version_number>-runnable.jar
```
