# diskSharingApp
# API:

#### `http://localhost:8080/user/` - Обязательный основной URL 

---

### GET запросы: 
#### `/disks` - Получение всех дисков текущего пользователя 
#### `/disks/free` - Получение всех свободных дисков 
#### `/disks/taken-by-user` - Получения дисков, взятых текущем пользователем 
#### `/disks/taken-from-user` - Получения дисков, взятых у пользователя 

---

### PUT запросы: 

#### `/disk/return/{id}` - Возврат одолженного диска, где `id` - номер диска
#### `/disk/take/{id}` - Присвоение свободного диска, где `id` - номер диска

---

# Инструкция по развертыванию:

## Сборка

Для сборки нужны JDK 8+, Maven 3.x+. Команда для сборки, из корня проекта: `mvn install`

## Внешние зависимости

* JRE 8+
* PostgreSQL 12.3+

## Конфигурация

Для соединения с БД необходимо переопределить следующие параметры:

* --spring.datasource.url=`value`
* --spring.datasource.username=`value`
* --spring.datasource.password=`value`

Конфигурация по умолчанию

* spring.datasource.url=jdbc:postgresql://localhost:5432/ProjectDB
* spring.datasource.username=postgres
* spring.datasource.password=1234

## Запуск

`java -jar JAR_FILE --param1=value --param2=value ...`
