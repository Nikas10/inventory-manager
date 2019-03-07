# inventory-manager
enterprise-inventory-manager-demo

# Getting started
cmd -> git clone -b develop https://github.com/Nikas10/inventory-manager
replace develop with the branch you need.
build -> mvm clean install -Dmaven.test.skip=false

# Workflow
All unsafe changes (feature developement, bugfixes and stuff) are merged to 'develop' branch.
All branches are made from 'develop'
With the period of two weeks, 'develop' branch is merged to 'master', if no conflicts are present.
Features are done in separate branches. Pull requests are approved only after code review.

# Project technology stack
## Build
Maven 3.0

## Spring
Spring Boot 2.1.3
Spring Data JPA
Spring Security OAuth2
Spring Web

## Utility
Lombok
Apache Commons
Log4J
Swagger

## Integrations
Jasper
Camunda
Postgre SQL 9.6


