## Тема
Книга рецептов (Recipe Book).

## Основные сущности
- `User` — пользователь
- `Recipe` — рецепт
- `Ingredient` — ингредиент (уникален по названию)
- `Rating` — оценка рецепта (1..5), уникальна для пары (recipe, user)
- `Comment` — комментарий к рецепту

## Возможности сервиса
### CRUD (по каждой сущности)
- `POST/GET/PUT/DELETE`
  - `/api/users`
  - `/api/ingredients`
  - `/api/recipes`
  - `/api/ratings`
  - `/api/comments`

### Бизнес-операции (Задание 3)
- `POST /api/recipes/publish` — публикация рецепта по списку названий ингредиентов (создаёт отсутствующие ингредиенты).
- `POST /api/recipes/{id}/ingredients` — добавить ингредиент к рецепту по названию (создаёт, если отсутствует).
- `GET /api/recipes/{id}/details` — детали рецепта + статистика (средняя оценка, кол-во оценок, кол-во комментариев).
- `GET /api/recipes/top?limit=N` — топ рецептов по рейтингу.
- `POST /api/ratings/set` — установить оценку рецепту (create или update) для пары (recipe, user).

## Postman
Коллекция запросов:
- `postman/RBPOLAB.postman_collection.json`
