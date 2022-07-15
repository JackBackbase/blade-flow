# Commands to bring up the envirionment and reset database

```
colima stop; colima start --cpu 6 --memory 12 --mount $HOME:w
docker-compose up -d


cd ds-flows/reference-flows/us-onboarding-flow/us-onboarding/us-onboarding-service;
mvn clean install -Pclean-database;
mvn clean blade:run
bash run_edge.sh
```

# Web consoles 

- http://localhost:8080/registry/
- http://localhost:7777/api/auth/login




