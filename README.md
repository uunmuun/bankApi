# bankApi

## Стек

- Java 17
- Spring Boot (Spring Web MVC, Spring Data JPA, Bean Validation)
- PostgreSQL
- JUnit 5, Mockito

## Запуск

1. Создай базу данных `bank_api` в PostgreSQL.
2. Проверь настройки в `src/main/resources/application.properties`: имя пользователя и пароль должны совпадать с твоей локальной установкой PostgreSQL.
3. Запусти приложение:

```bash
./mvnw spring-boot:run
```

Таблицы создаются автоматически при первом запуске (`spring.jpa.hibernate.ddl-auto=update`).

Тесты:

```bash
./mvnw test
```

## Сущности

- Client: фамилия, имя, отчество, email (уникальный), дата рождения.
- Phone: номер, тип (`MOBILE`, `HOME`, `WORK`), принадлежит клиенту.
- Account: номер счёта, валюта (`KGS`, `USD`, `EUR`), баланс, принадлежит клиенту.
- Card: номер карты, срок действия, тип (`DEBIT`, `CREDIT`), статус (`ACTIVE`, `BLOCKED`), принадлежит счёту.

Связи: клиент имеет несколько телефонов и несколько счетов, счёт имеет несколько карт.

## Эндпоинты

### Клиенты

- `POST /clients` — создать клиента
- `GET /clients` — список клиентов
- `GET /clients/{id}` — клиент по id
- `PUT /clients/{id}` — обновить клиента
- `DELETE /clients/{id}` — удалить клиента

### Телефоны клиента

- `POST /clients/{clientId}/phones` — добавить телефон
- `GET /clients/{clientId}/phones` — телефоны клиента
- `GET /clients/{clientId}/phones/{phoneId}` — телефон по id
- `PUT /clients/{clientId}/phones/{phoneId}` — обновить телефон
- `DELETE /clients/{clientId}/phones/{phoneId}` — удалить телефон

### Счета

- `POST /api/accounts/client/{clientId}` — открыть счёт клиенту
- `GET /api/accounts` — все счета
- `GET /api/accounts/{id}` — счёт по id
- `GET /api/accounts/client/{clientId}` — счета клиента
- `GET /api/accounts/{id}/cards` — карты счёта
- `PUT /api/accounts/{id}` — изменить валюту счёта
- `DELETE /api/accounts/{id}` — закрыть счёт
- `POST /api/accounts/transfer` — перевод между счетами

### Карты

- `POST /api/cards` — выпустить карту на счёт (`accountId` передаётся в теле запроса)
- `GET /api/cards` — все карты
- `GET /api/cards/{id}` — карта по id
- `PUT /api/cards/{id}` — обновить карту
- `PATCH /api/cards/{id}/block` — заблокировать карту
- `PATCH /api/cards/{id}/unblock` — разблокировать карту
- `DELETE /api/cards/{id}` — удалить карту

Пути клиентов и телефонов записаны без префикса `/api`, пути счетов и карт с ним. 
в будущем стоит привести их к одному виду. 

## Нестандартные ситуации

- Недостаточно средств на счёте при переводе — 400.
- Перевод между счетами в разных валютах — 400, конвертация не реализована.
- Перевод на тот же счёт — 400.
- Закрытие счёта с ненулевым балансом — 400.
- Закрытие счёта с активными картами — 400.
- Смена валюты счёта с ненулевым балансом — 400.
- Удаление клиента, у которого есть счета — 409.
- Клиент с таким email уже существует — 409.
- Клиент, телефон, счёт или карта не найдены — 404.

При закрытии счёта заблокированные карты удаляются вместе со счётом. Активные карты закрытию не дают.
