# SIEM Simplificado (Java + Elasticsearch)

Projeto educacional: centraliza logs, indexa em Elasticsearch e aplica regras de correlação para gerar alertas.

## Arquitetura

- API: Spring Boot (recebe logs via HTTP)
- Storage: Elasticsearch (index `siem-events`)
- Correlation: Serviço agendado que gera documentos em `siem-alerts`
- Visualização: Kibana (opcional)

## Pré-requisitos

- Java 17+
- Maven
- Docker + Docker Compose

## Rodando localmente

1. Inicie Elasticsearch e Kibana:

```bash
cd docker
docker compose up -d
```

2. Build e run da aplicação Java:

```bash
mvn clean package
java -jar target/siem-0.0.1-SNAPSHOT.jar
```

3. Teste ingestão:

```bash
curl -X POST http://localhost:8080/api/logs -H "Content-Type: application/json" -d '{"sourceIp":"1.2.3.4","eventType":"FAILED_LOGIN","message":"senha errada"}'
```

## Endpoints

- `POST /api/logs` — ingest
- `GET /api/alerts` — lista alertas
- `GET /api/logs/search?q=...` — busca (não implementada neste template)

## Regras de correlação exemplos

- BRUTE_FORCE: 5 FAILED_LOGIN do mesmo IP em 10 minutos

## Contribuição

- Abra issues para bugs e features
- Use branches por recurso
