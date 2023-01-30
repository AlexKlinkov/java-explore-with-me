# "java-explore-with-me" (backend of web app)

## Description:

Приложение для дружеских встречь, которое позволяет:
- Добавлять события, 
- Удалять события, 
- Менять информацию о событиях, 
- Подавать заявку на участие в событие(ях), 
- Получать статистику просмотров события, 
- Оставлять комментарии к событиям.

## BD SCHEMA OF MAIN SERVICE

![DB_EWN](https://github.com/AlexKlinkov/java-explore-with-me/blob/main/SCHEMA_BD_OF_EWN_SERVICE.jpg)

## DB SCHEMA OF STATISTIC SERVICE

![DB_STAT](https://github.com/AlexKlinkov/java-explore-with-me/blob/main/SCHEMA_BD_OF_STAT_SERVICE.jpg)

## Specification of app:
### API for Swagger :

Главный сервер: [ewm-main-service-spec.json](https://github.com/AlexKlinkov/explore-with-me/blob/main/ewm-main-service-spec.json)

Сервер статистики: [ewm-stats-service-spec.json](https://github.com/AlexKlinkov/explore-with-me/blob/main/ewm-stats-service-spec.json)

## Instruction of launch app::

С помощью IntelliJ IDEA (my version 2022.1)

Открываем проект.
IntelliJ IDEA сообщит, что "Maven Build Scripts Found", следует нажать "Load".
Запускаем команду mvn clean install.
Запускаем команду docker-compose up в терминале IDEA, убедившись, что находимся в той же директории, что и файл "docker-compose.yml", при этом Docker daemon должен быть запущен.
Так же можно, загрузить базовую коллекцию для сервисов ([Ссылка на коллекцию](https://github.com/AlexKlinkov/java-explore-with-me/tree/main/postman)) в Postman и подергать разные ручки, посмотреть как работает приложение.

## Technology stack:

- Java 11.0.15
- Spring Boot 2.7.2
- Maven 4.0.0
- MapStruct 1.5.2.Final
- Lombok 1.18.24
- WebClient 2.6.4
- ORM Hibernate 5.6.10
- PostgreSQL 14.5
- JUnit 5.0
- Mockito 4.5.1
- Docker 3.8

## Future plans:

- Добавить тесты
