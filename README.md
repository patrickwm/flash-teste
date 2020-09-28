Flash
==============

Projeto teste para a empresa Flash Courier.


Tecnlogias Utilizadas
========

- Java
- Vaadin v8.11
- PostgreSQL 11
- JPA
- CDI
- Wildfly 20.0.1

Requisitos
========

- JDK 11
- Maven
- PostgreSQL 11

Descrição
========
O projeto é um teste para a empresa FlashCourier, que basicamente consiste em buscar um código de rastreio e retornar os detalhes a respeito dele.

Ao executar em alguma IDE (Utilizei o Intellij) o banco será criado automaticamente (Deve criar o datasource do servidor - Nome disponível no arquivo persistence.xml) 

Insert para testes
========
```
INSERT INTO 
    public.encomenda(id, codigo_rastreio, status, cidade_destino, cidade_origem, data_hora_postagem)
VALUES 
    (1, 'AB123456CD', 'POSTADO', 'Curitiba - PR', 'São Paulo - SP', '2020-09-28 13:05:00'),
    (2, 'AB123456CD', 'ENTREGUE', 'São Paulo - SP', 'Curitiba - PR', '2018-09-28 13:05:00'),
    (3, 'CD123456AB', 'EM_TRANSITO', 'São Paulo - SP', 'Curitiba - PR', '2020-09-14 13:05:00');
    
    
INSERT INTO 
    public.detalhes_rastreio(id, cidade_destino, cidade_origem, data_hora, descricao, encomenda_id)
VALUES 
    (1, '', 'Curitiba', '2020-09-28 13:05:00', 'Objeto Postado', 1),
    (2, '', 'Barueri', '2018-09-28 13:05:00', 'Objeto Postado', 2),
    (3, 'Santos', 'Barueri', '2018-09-28 17:00:00', 'Objeto Encaminhado', 2),
    (4, 'São Paulo', 'Santos', '2018-09-29 09:40:00', 'Objeto Encaminhado', 2),
    (5, 'Curitiba', 'São Paulo', '2018-09-29 15:30:00', 'Objeto Encaminhado', 2),
    (6, 'Curitiba', 'Curitiba', '2018-09-30 09:40:00', 'Objeto Saiu para entrega', 2),
    (7, 'Curitiba', '', '2018-09-30 13:00:00', 'Objeto Entregue', 2),
    (8, '', 'Curitiba', '2020-09-28 13:05:00', 'Objeto Postado', 3),
    (9, 'São Paulo', 'Curitiba', '2020-09-28 17:00:00', 'Objeto Encaminhado', 3);

```



