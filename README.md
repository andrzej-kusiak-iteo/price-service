# Price Service

> Implementation of a service that will provide a REST API for calculating a price for a given product and its amount.

<!-- TOC -->
* [Features](#features)
* [Api description](#api-description)
* [Technologies used](#technologies-used)
* [Running application](#running-application)
* [Docker image](#docker-image)
* [Test data](#test-data)
<!-- TOC -->

## Features

* calculating a price for a given product and its amount.
* possibility of applying discounts based on two policies
* amount based (the more pieces of the product are ordered, the bigger the discount is)
* percentage based

## Api description

```shell
curl --location --request POST 'localhost:8080/v1/price' \
--header 'Content-Type: application/json' \
--data-raw '[
    {
        "productId": "53005805-c051-4656-b78a-50f9365a7e3a",
        "quantity": 1
    },
    {
        "productId": "058153a8-eb86-40a4-beb9-c4e9de5217c6",
        "quantity": 2
    }
]'
```

Endpoint applies all discounts on provided products and returns theirs base and discount price.

## Technologies used

* Build tool - gradle
* Application - Spring Framework
* ORM - JPA/Hibernate
* Tests - Spock
* Database - PostgreSql
* Db migration - Liquibase

## Running application

Connect to database setting up env variable `SPRING_DATASOURCE_URL` defaults to `jdbc:postgresql://localhost:5432/postgres`

You can use docker database specified in docker-compose.yml file using command `$ docker compose up` from root of a 
project. 

Run application with command `$ ./gradlew bootRun`, database schema will be created automatically.
## Docker image

Application docker image can be generated using `jib`. Run `$ gradle jibDockerBuild -Djib.to.tags=latest` to publish 
it to your local docker daemon with tag *latest*.

Run app image with command `$ docker run -p 8080:8080 -e SPRING_DATASOURCE_URL=${DATABASE_URL} price-service`

## Test data

SQL script supplying example test data can be found on GitHub Wiki
[data](https://github.com/andrzej-kusiak-iteo/price-service/wiki/Example-test-data).
