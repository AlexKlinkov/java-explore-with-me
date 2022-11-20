# Дипломный проект : java-explore-with-me

## Описание/Функциональность

Приложение для дружеских встречь, которое позволяет:
- Добавлять событие, 
- Удалять событие, 
- Менять информацию о событии, 
- Подавать заявку на участие в событие, 
- Получать статистику просмотров события, 
- Оставлять комментарии к событию.

## Требования к приложению (Спецификация). API для Swagger :

Главный сервер: [ewm-main-service-spec.json](https://github.com/AlexKlinkov/explore-with-me/blob/main/ewm-main-service-spec.json)
Сервер статистики: [ewm-stats-service-spec.json](https://github.com/AlexKlinkov/explore-with-me/blob/main/ewm-stats-service-spec.json)

## Запуск приложения:

С помощью IntelliJ IDEA

1. Открываем проект
2. Запускаем команду **mvn clean install** в Maven
3. Запускаем команду **docker-compose up** в терминале IDEA (Docker daemon должен быть запущен)

## Стек

- Spring Boot 2.7.2
- Maven
- MapStruct
- Lombok
- ORM Hibernate
- PostgreSQL
- Docker
