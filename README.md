# What is wes-argo-api?
  A microservice that provides queries to retrieve argo data and workflow data.

# Technical Components:
Two components:
* GraphQL layer: exposes queries to fetch argo data.
* GraphQL gateway layer: pulls upstream GraphQL schemas and redirects query requests to upstream graphql services.

# Who Will Be Consuming wes-argo-api?
Workflow Orchestrator and argo aggregator 

# Requirements
In order to decide whether or not to kick off a workflow, Workflow Orchestrator needs to know workflow run status based on donor information.
* if a donor has no run, a new workflow is ready to start. 
* if a donor has run with status "RUNNING", a new workflow should not be started.
* If a donor has run with with status "COMPLETE", a new workflow is ready to start.

#Data Source
Wes-argo-api consumes 2 elastic indices: file_centric_1.0, donor_centric.
Maestro(https://github.com/overture-stack/maestro) provides both indices.

# Major technologies
* Spring Boot
* GraphQL
* GraphQL Java
* Elasticsearch

# Build
With maven:
`mvn clean package`

# Run
`java -jar wes-argo-api-0.0.1-SNAPSHOT.jar`

# Interact with GraphQL queries:
* With playground:
Once the server is up, GraphQL will be running at `localhost:8080/graphql`. If you have playground
installed locally, enter `http://localhost:8080/graphql` to interact with the queries. 

* Without playground:
Hit `http://localhost:8080/playground` in the browser to get data!