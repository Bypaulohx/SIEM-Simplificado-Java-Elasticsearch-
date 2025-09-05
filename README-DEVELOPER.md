# SIEM Simplificado — Guia do Desenvolvedor

## Como rodar em modo desenvolvimento

- Configure `application.yml` com `elastic.host` se necessário
- Inicie ES/Kibana via Docker

## Como adicionar regra de correlação

1. Implemente nova função na `CorrelationService`
2. Use Elasticsearch `aggregations` para agrupar e contar eventos
3. Indexe alertas em `siem-alerts`

## Testes

- Unit tests: `mvn test`
- Integração: inicie ES + execute testes de integração manual
