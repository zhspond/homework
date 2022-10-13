# Getting Started

## Build and run
Prerequisites: 
* Docker engine
* Docker compose
* Internet access
  * dockerhub
  * maven repo
* Unused 8080 port (customize in docker compose file as needed)

Build and run using docker compose :
```
docker-compose up
```
## API
The service has 2 web operations:
* 10 asteroids passing closest to earth in a given time interval (max 7 days)
```
GET http://localhost:8080/v1/asteroid/closest?start=2022-10-13&end=2022-10-13
```
* Characteristics of the largest asteroid passing the earth in a given period (max 7 days)
```
GET http://localhost:8080/v1/asteroid/largest?start=2022-10-13&end=2022-10-13
```

## Developer remarks
Focused on simple, clean code and structure.

A number of shortcut are taken in other aspects due to time constraints. 
Tried to address most those concerns as code comments / todos.

Some things not addressed in code comments:
* Not deployed to cloud
  * The app is dockerized. It should be straightforward to run using an orchestration engine like Kubernetes or ECS.
* Layered architecture but implemented in single module