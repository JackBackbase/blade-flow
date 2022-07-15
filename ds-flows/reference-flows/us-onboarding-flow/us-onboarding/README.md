## US Onboarding BE Reference

This project could be run standalone without use of any other backbase projects. But to make this
project run smooth, minimal IPS is required, as gateway and registry are used by default
to make the connection between case-manager and the Flow core.

## Building the flow

### Prerequisites:
- Java 11
- Maven

Start by running in this root folder the command to build the project.
```
mvn clean install
```
## Running the flow

Navigate to the service directory for the details

* [us-onboarding-service](./us-onboarding-service/README.md)