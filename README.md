## RBPO 2026 — задания 1–2, вариант 27

Тема: **Редакция новостного издания**.

### Запуск

```bash
cd RBPOLAB
mvn clean test
mvn spring-boot:run
```

Сервис: `http://localhost:8080`

### Задание 1

- Spring Boot 3.5.5, Maven, JDK 21
- `application.yml` в `src/main/resources` и `src/test/resources` (`spring.application.name` — номер студенческого билета, замените `24001234567` на свой)
- GET `/api/info/greeting` → `привет это первое задание`
- GET `/api/info/numbers` → JSON-массив из 3 случайных целых (1–99)
- Testcontainers (test scope, без Docker в тестах)

### Задание 2 (вариант 27)

| Ресурс | Путь |
|--------|------|
| Пользователь | `/api/users` |
| Рубрика | `/api/editorial-sections` |
| Статья | `/api/articles` |
| Редакторская проверка | `/api/editorial-reviews` |
| Выпуск издания | `/api/edition-issues` |

CRUD: GET list/id, POST (201), PATCH (200), DELETE (204). Любой PUT возвращает статический HTML.

Хранилище — in-memory (задание 3 добавит БД).
