# java-explore-with-me
Template repository for ExploreWithMe project.

# Ссылка на pull request: https://github.com/ekip-s/java-explore-with-me/pull/2

Свободное время — ценный ресурс. Ежедневно мы планируем, как его потратить — куда и с кем сходить. 
Сложнее всего в таком планировании поиск информации и переговоры. Какие намечаются мероприятия, свободны ли в этот момент
друзья, как всех пригласить и где собраться. Это приложение — афиша, где можно предложить какое-либо событие от выставки
до похода в кино и набрать компанию для участия в нём.

# Оновной сервис:
## Public: События
#### `GET /events` - получение событий с возможностью фильтрации;
#### `GET/events/id` - получение подробной информации об опубликованном событии по его идентификатору;
## Public: Подборки событи
#### `GET /compilations` - получение подборок событий;
#### `GET /compilations/compId` - получение подборки событий по id;
## Public: Категории
#### `GET /categories` - получение категорий;
#### `GET /categories` - получение информации о категории по id;
## Private: События
#### `GET /users/userId/events` - события добавленные текущим пользователем;
#### `PATCH /users/userId/events` - изменение события текущего пользователя; 
#### `POST /users/userId/events` - добавление нового события;
#### `GET /users/userId/events/eventId` - полная информации о событии текущего пользователя; 
#### `PATCH /users/userId/events/eventId` - отмена события текущего пользователя;
#### `GET /users/userId/events/eventId/requests` - получение запросов на участие в событии пользователя;
#### `PATCH /users/userId/events/eventId/requests/reqId/confirm` - подтверждение заявки на учатие в событии пользователя;
#### `PATCH /users/userId/events/eventId/requests/reqId/reject` - отклонение заявки на событие пользователя;
## Private: Запросы на участие
#### `GET /users/userId/requests` - заявки текущего пользователя на участия в событиях;
#### `POST /users/userId/requests` - добавление запроса от пользователя, на участие в событии;
#### `PATCH /users/userId/requests/requestId` - отмена своего запроса на участие в событии;
## Admin: События
#### `GET /admin/events` - поиск события;
#### `PUT /admin/events/ventId` - редактирование события;
#### `PATCH /admin/events/ventId/publish` - публикация события; 
#### `PATCH /admin/events/ventId/reject` - отклонение события;
## Admin: Категории
#### `PATCH /admin/categories` - изменение категории; 
#### `POST /admin/categories` - добавление новой категории;
#### `DELETE /admin/categories/catId` - удаление категории;
## Admin: Пользователи
#### `GET /admin/users` - информация о пользователях;
#### `POST /admin/users` - добавление нового пользователя;
#### `DELETE /admin/users/userId` - удаление пользователя;
## Admin: Подборки событий
#### `POST /admin/compilations` - добавление новой подборки;
#### `DELETE /admin/compilations/compId` - удаление подборки;
#### `DELETE /admin/compilations/compId/events/eventId` - удаление события из подборки; 
#### `PATCH /admin/compilations/compId/events/eventId` - добавление события в подборку;
#### `DELETE /admin/compilations/compId/pin` - открепить подборку на главной;
#### `PATCH /admin/compilations/compId/pin` - закрепить подборку на главной;
## Admin: Комментарии
#### `PATCH /admin/comments/commentId` - обновить текст комментария;
#### `PATCH /admin/comments/commentId/confirm` - опубликовать комментарий; 
#### `PATCH /admin/comments/commentId/reject/reason/reasonId` - отклонить комментарий;
#### `DELETE /admin/comments/commentId` - удалить комментарий;
## Admin: Причины отмены или удаления комментария
#### `GET /admin/reason/reasonId` - поиск причины по id;
#### `GET /admin/reason` - поиск всех причин;
#### `POST /admin/reason` - создание новой причины;
#### `PATCH /admin/reason/reasonId` - обновить описание причины;
#### `DELETE /admin/reason/reasonId` - удалить причину;
## Private: Комментарии
#### `POST /users/userId/comments` - создать новый комментарий;
#### `PATCH /users/userId/comments/commentId` - обновить текст комментария;
#### `DELETE /users/userId/comments/commentId` - удалить комментарий;
## Public: Комментарии
#### `GET /comments/publication/eventId` - поиск всех комментарий по событию;
#### `GET /comments/search/search` - поиск комментарий по ключевому слову; 
#### `GET /comments/commentId` - комментарий по id; 



# Сервис статистики:
## StatsController
#### `POST /hit` - созранение информации о том, что к эндпоинту был запрос;
#### `GET /stats` - получение статистики;