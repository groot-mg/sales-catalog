[![Build and Test App](https://github.com/mauriciogeneroso/spring-shopping-api/actions/workflows/gradle.yml/badge.svg)](https://github.com/mauriciogeneroso/spring-shopping-api/actions/workflows/gradle.yml)
[![Prometheus CI check](https://github.com/mauriciogeneroso/spring-shopping-api/actions/workflows/prometheus-ci-check.yml/badge.svg)](https://github.com/mauriciogeneroso/spring-shopping-api/actions/workflows/prometheus-ci-check.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://github.com/algarves/loteria-crawlers/blob/master/LICENSE)

# Spring Shopping API
---

This API is an example using Spring boot to storage products, services and orders.

It is using the following tools:

* Spring boot
* Gradle
* Docker
* Postgresql
* Prometheus
* Grafana

# `1. How to start up it locally`

To start up the application is required `Java 11` and `Docker` installed.

### Using Linux/Mac environment

The following script is enough to build and run the application on docker containers:
```
./docker-compose.sh
```

### Using Windows environment

-- To do

### Urls

With all the containers up, there are the available URL locally:
  * `http://localhost:8080/spring-crud/`: the Spring API
  * `http://localhost:8080/spring-crud/private/health`: Health check URL
  * `http://localhost:8080/spring-crud/private/info`: Info page
  * `http://localhost:8080/spring-crud/swagger-ui/index.html`: Swagger UI provided by OpenApi library
  * `http://localhost:9090`: Prometheus
  * `http://localhost:3000`: Grafana UI

# `2. API`
  
  Details about how to use the API and business rules. 

## 2.1 Items and services

This topic describes the endpoints to manage items and services.

### 2.1.1 Register a new product/service

To register a new service, should be used `POST /api/v1/items`:

```javascript
{
	"name": "Service name",
	"type": "SERVICE",
	"price": 100
}
```

#### Restrictions:
* It is not possible to register a service with the same name
* The price should be a positive value

### 2.1.2 Update a product or service

The update endpoint for a product/service is `PUT /api/v1/items/{id}` using the id:

```javascript
{
	"name": "Changed service name",
	"type": "SERVICE",
	"price": 50
}
```

By default, a new product/service has the status active.

#### Restrictions:
* It is not possible to update to an existing product/service name 
* The price should be a positive value.

### 2.1.3 Delete a product or service

The endpoint to delete a product/service is `DELETE /api/v1/items/{id}` using the id.

#### Restriction:
* It is not possible to delete a product/service linked to an order.

### 2.1.4 Getting products/services

To get all or a specific product/service is used `GET /api/v1/items/` (get all) or `GET /api/v1/items/{id}` (get a single one).

The amount per page can be changed using the parameter `pageSize`, and the current page using `pageNumber`.

E.g: `/api/v1/items?pageNumber=1&pageSize=1`.

```javascript
{
    "content": [
        {
            "id": "d6bc0f21-c4a7-40c0-b86c-8bdc074e6fa1",
            "createdAt": "2019-12-09 06:51:05",
            "version": "V1",
            "name": "Teste",
            "type": "SERVICE",
            "price": 100.0,
            "active": true
        }
    ],
    "pageable": {
        "sort": {
            "sorted": false,
            "unsorted": true
        },
        "pageSize": 15,
        "pageNumber": 0,
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 1,
    "first": true,
    "sort": {
        "sorted": false,
        "unsorted": true
    },
    "numberOfElements": 1,
    "size": 15,
    "number": 0
}
```

### 2.1.5 Activate or deactivate a product/service

To activate or deactivate a product/service, should be used the following endpoints: `/PUT api/v1/items/{id}/activate` and `PUT /api/v1/items/{id}/deactivate`.

## 2.2 Order

An order has many products/services, and it is possible to apply discounts.

### 2.2.1 Register a new order

To register a new order is necessary specify the list of products/services at least with amount and id. The endpoint is `POST /api/v1/orders`:

```javascript
{
    "orderItems": [
        {
            "amount": 2,
            "itemId": "72d0fa34-26ee-4ce3-9b21-41701fb47e19"
        },
        {
            "amount": 2,
            "itemId": "19afb017-4373-4370-86f8-5664c0d05d08"
        }
    ]
}
```

#### Restrictions
* It is not possible to register the same product/service with different quantities. A product/service should be added once.
* It is required at least 1 for each product/service in the order.

### 2.2.2 Applying discounts

To apply discount in an order, should be used the endpoint `PUT /api/v1/orders/{id}/discount`:

The value received on the server is the percentage.

```javascript
{
    "discount": 10
}
```

P.s: The discount preview is available when querying the order details (according 3.2.5) on `totalPreview` field.

#### Restrictions
* The discount should be between 0 and 100.
* It is not possible to apply discount in a closed order.
* The discounts are applied only on products, services don't receive discounts.

### 2.2.3 Close an order

To close an order should be used the endpoint `PUT /api/v1/orders/{id}/close`.

#### Restrictions
* A closed order cannot be re-opened.

### 2.2.4 Delete an order

To delete an order should be used `DELETE /api/v1/orders`.

### 2.2.5 Get orders

It uses pagination as it is on 3.1.4 to get products/services. It should be used the endpoint `GET /api/v1/orders` to get all and `PUT /api/v1/orders/{id}` to get an specific order.

To get a specific order, only the simple info will be returned:

```javascript
{
    "id": "febe23ec-8ee1-441f-8acc-df0ed9c698f7",
    "createdAt": "2019-12-09 07:18:42",
    "version": "V1",
    "open": true,
    "discount": 0,
    "totalPreview": 100.0
}
```
To expand details on the order, should be used the parameters `orderItemsExpanded`.

E.g: `/api/v1/orders/{id}?expand=orderItemsExpanded` returns:

```javascript
{
    "id": "febe23ec-8ee1-441f-8acc-df0ed9c698f7",
    "createdAt": "2019-12-09 07:18:42",
    "version": "V1",
    "open": true,
    "discount": 0,
    "totalPreview": 100.0,
    "orderItems": [
        {
            "id": "b0237beb-4720-48b4-a9fe-c5fbe7d67f4c",
            "createdAt": "2019-12-09 07:18:42",
            "version": "V1",
            "amount": 1,
            "itemId": "c5d57f13-06f5-4ef3-b5a8-ef250ba5fe1b",
            "item": {
                "id": "c5d57f13-06f5-4ef3-b5a8-ef250ba5fe1b",
                "createdAt": "2019-12-09 07:18:07",
                "version": "V1",
                "name": "Teste 1",
                "type": "PRODUCT",
                "price": 100.0,
                "active": true
            }
        }
    ]
}
```

## 2.3 Order items

An order item is created together with an order. After register an order, we can do some operations with the order items.

### 2.3.1 Get order items

It uses pagination as usual. The endpoint to make the request is `GET api/v1/orders/{orderId}/order-items` to get all and `GET api/v1/orders/{orderId}/order-items/{id}` to get an specific item.

### 2.3.2 Add a new item in the order

To add a new product/service in the order should be used the endpoint `POST api/v1/orders/{orderId}/order-items` with body as bellow:

```javascript
{
    "amount": 5,
    "itemId": "ca9147bb-017f-4b9d-bc11-2431552d2fa8"
}
```

#### Restriction
* It is not possible to add an item already existing in the order item

### 2.3.3 Update existing product/service

To update the amount, should be used `PUT /api/v1/orders/{orderId}/order-items/{id}` (the id in the url is the order item id, and not the product/service id).

```javascript
{
    "amount": 5,
    "itemId": "19afb017-4373-4370-86f8-5664c0d05d08"
}
```

### 2.3.4 Delete a product/service

To delete a product/service from the order, should be used the endpoint `/api/v1/orders/{orderId}/order-items/{id}`.

#### Restriction
* It is not possible to remove all items from the order, it is required at least one product/service. 
