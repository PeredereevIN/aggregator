# Messenger Aggregator MVP
---
## Запуск

```cmd
docker-compose up -d
```

## Swagger
http://localhost:8080/swagger-ui.html

## Администрирование
Добавление канала VK:

```
POST /api/admin/channels
{
  "type": "VK",
  "configJson": "{"access_token":"ваш_токен"}"
}
```
