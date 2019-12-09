# Spring Crud app Example
---

API de exemplo utilizando Springboot com os cadastros de produto/serviço, pedido e item de pedido.

# `1. Requisitos`

* Maven
* Java 8 (variáveis de ambiente configuradas)
* PostgreSQL
* Configurado as variáveis de ambiente `DATASOURCE_URL`, `DATASOURCE_USERNAME` e `DATASOURCE_PASSWORD` com os respectivos dados para conectar no banco de dados. O formato da URL é `jdbc:postgresql://<host>:<port>/<database>`.

# `2. Executando o projeto`

* 1 Executar o comando `mvn package`
* 2 Executar o comando `java -jar target/example-0.0.1-SNAPSHOT.jar`

Por padrão o projeto está disponível na porta `8080` no contexto `/spring-crud-example`. Com o projeto em execução em ambiente local, o contexto para executar as requests será `http://localhost:8080/spring-crud-example`.

Detalhes sobre os endpoints podem ser visualizados no Swagger disponível no endereço:
http://localhost:8080/spring-crud-example/swagger-ui.html#/

* <a href="https://spring-crud-app-example.herokuapp.com/spring-crud-example/swagger-ui.html#/">Versão online do Swagger</a>

# `3. API`
---
  
  Destalhes sobre a utilização da API.

## 3.1 Items e serviços

Neste tópico está descrito as informações sobre os itens e serviços.

### 3.1.1 Cadastro de um produto ou serviço

O cadastro de um produto ou de um serviço é feito por meio do endpoint `POST /api/v1/items`:

```javascript
{
	"name": "Service name",
	"type": "SERVICE",
	"price": 100
}
```

#### Restrições:
* Não é possível registrar mais de um produto/serviço com mesmo nome
* O preço deve ser um valor positivo.

### 3.1.2 Atualização de um produto ou seviço

A atualização de um produto ou de um serviço é feita por meio do endpoint `PUT /api/v1/items/{id}` utilizando o id:

```javascript
{
	"name": "Changed service name",
	"type": "SERVICE",
	"price": 50
}
```

Por padrão um produto/serviço é cadastrado com o status ativo.

#### Restrições:
* Não é possível registrar mais de um produto/serviço com mesmo nome
* O preço deve ser um valor positivo.

### 3.1.3 Remover um produto ou serviço

A exclusão de um produto/serviço é feita por meio do endpoint `DELETE /api/v1/items/{id}` utilizando o id.

#### Restrições:
* Não é possível remover um produto/serviço que está em uso vinculado a um pedido

### 3.1.4 Listagem de um produto ou serviço

A busca/listagem dos produtos/serviços é feita por meio do endpoint `GET /api/v1/items/` (listage) ou `GET /api/v1/items/{id}` (busca específica por um item).

A quantidade de itens por página pode ser alterado por meio do parâmetro `pageSize`, e a página atual por meio do parâmetro `pageNumber`. Exemplo: `/api/v1/items?pageNumber=1&pageSize=1`.

O retorno dos dados é feito de forma paginada:

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

### 3.1.5 Ativar e desativar um produto/serviço

Para ativar ou desativar um produto/serviço deve ser utilizado os respectivos endpoints: `/PUT api/v1/items/{id}/activate` e `PUT /api/v1/items/{id}/deactivate`.

## 3.2 Pedido

Um pedido possui diversos produtos/serviços com suas respectivas quantidade, e é possível aplicar desconto sobre os produtos (serviço não).

### 3.2.1 Cadastro de um pedido

O pedido é cadastrado informando uma lista com todos os itens de pedido, contendo a quantidade e o id do produto/serviço. É utilizado o endpoint `POST /api/v1/orders`:

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

#### Restrições
* Não é possível registrar quantidades diferentes para um mesmo produto/serviço. Um produto/serviço dentro de um pedido deve ser único.
* Deve-se ter pelo menos a quantidade de 1 produto/serviço no pedido.

### 3.2.2 Aplicando desconto

Para aplicar desconto sobre um pedido, deve-se utilizar o endpoint `PUT /api/v1/orders/{id}/discount`:

```javascript
{
    "discount": 10
}
```

Obs: O desconto pode ser visualizado ao buscar os dados do pedido (conforme 3.2.5) e pode ser visualizado no campo `totalPreview`.

#### Restrições
* O desconto deve ser informado entre 0 a 100.
* Não é possível aplicar desconto para um pedido fechado.
* O desconto só é aplicado sobre o valor total dos produtos (Para serviços não há descontos).

### 3.2.3 Fechar um pedido

Um pedido é fechado por meio do endpoint `PUT /api/v1/orders/{id}/close`.

#### Restrições
* Um pedido fechado não poderá ser aberto.

### 3.2.4 Remover um pedido

A remoção de um pedido é feita por meio do enpoint `DELETE /api/v1/orders`.

### 3.2.5 Listar pedidos e um pedido específico

A listagem é feita com paginação também assim como no item 3.1.4 para listar produtos/serviços. Deve ser utilizado o endpoint `GET /api/v1/orders` para uma listagem geral e `PUT /api/v1/orders/{id}` para buscar um pedido específico.

Ao buscar um pedido específico, somente os dados básicos serão retornados:

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
Para expandir os dados dos itens do pedido, deve-se passar o parâmetro `orderItemsExpanded`.

Exemplo utilizando o endpoint `/api/v1/orders/{id}?expand=orderItemsExpanded` com o retorno:

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

## 3.3 Itens de um pedido

Um item de um pedido é cadastrado junto a um pedido. Após ter um pedido cadastrado é possível realizar algumas operações com os itens do pedido.

### 3.3.1 Listar os itens de um pedido ou um item específico

A listagem dos itens de um pedido utiliza listagem assim como na listagem dos produtos/serviços e dos pedidos. É feita por meio do endpoint `GET api/v1/orders/{orderId}/order-items` para uma listagem geral e `GET api/v1/orders/{orderId}/order-items/{id}` para uma listagem de um item específico.

### 3.3.2 Adicionar um novo item ao pedido:

Para adicionar um novo item deve-se utilizar `POST api/v1/orders/{orderId}/order-items` informando a quantidade e o produto/serviço:

```javascript
{
    "amount": 5,
    "itemId": "ca9147bb-017f-4b9d-bc11-2431552d2fa8"
}
```

#### Restrições
* Não é possível adicionar um pedido/produto que já esteja adicionado ao pedido

### 3.3.3 Atualizar a quantidade de items

A atualização da quantidade de um item do pedido é feita por meio do endpoint `PUT /api/v1/orders/{orderId}/order-items/{id}` em que o id é o id do vínculo entre o pedido e o item (e não o id do produto/serviço).

```javascript
{
    "amount": 5,
    "itemId": "19afb017-4373-4370-86f8-5664c0d05d08"
}
```

### 3.3.4 Remover um item do pedido

A remoção de um item do pedido é feita por meio do endpoint `/api/v1/orders/{orderId}/order-items/{id}` em que o id é o id do vínculo entre o pedido e o item (e não o id do produto/serviço).

#### Restrições
* Não é possível remover todos os itens de um pedido, é necessário que o pedido tenha pelo menos um item.

---
@author: <a href="https://github.com/MauricioGenerosoTR">Maurício Generoso</a></h6>
