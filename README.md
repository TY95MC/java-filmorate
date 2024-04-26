# java-filmorate сервис по оценке фильмов
Сервис позволяет добавлять/обновлять/удалять фильмы, ставить/убирать лайк фильму, добавлять/удалять друзей, получение общего списка друзей

стек технологий: Spring-Boot, Lombok, H2database; SDK 11

Схема базы данных Н2
<image src="ER-diagram.png">

Запустить сервис можно в IntelliJ IDEA:
Запустите класс FilmorateApplication в папке 
java-filmorate\src\main\java\ru\yandex\practicum\filmorate\

Все взаимодействие сервиса с БД происходит посредством SQL-запросов. Ниже представлены примеры таких запросов

Получение топ-10 названий фильмов по количеству лайков

SELECT f.name\
FROM films AS f\
WHERE film_id IN\
&emsp;&emsp;(SELECT film_id\
&emsp;&emsp;&ensp;FROM film_likes\
&emsp;&emsp;&ensp;GROUP BY film_id\
&emsp;&emsp;&ensp;ORDER BY COUNT(user_id) desc\
&emsp;&emsp;&ensp;LIMIT 10);

Получение возрастных рейтингов фильмов из списка топ-10

SELECT r.rating_name\
FROM films AS f\
LEFT OUTER JOIN film_rating AS fr ON f.film_id = fr.film_id\
LEFT OUTER JOIN rating AS r ON fr.rating_id = r.rating_id\
WHERE film_id IN\
&emsp;&emsp;(SELECT film_id\
&emsp;&emsp;&ensp;FROM film_likes\
&emsp;&emsp;&ensp;GROUP BY film_id\
&emsp;&emsp;&ensp;ORDER BY COUNT(user_id) DESC\
&emsp;&emsp;&ensp;LIMIT 10);

Получение количества лайков к фильму с id = 1

SELECT COUNT(user_id)\
FROM film_likes\
WHERE film_id = 1;

Получение id и логинов подтвержденных друзей пользователя c id = 1

SELECT u.user_id,\
&emsp;&emsp;&emsp;&emsp;u.login\
FROM users AS u\
WHERE u.user_id IN\
&emsp;&emsp;(SELECT f.friend_id\
&emsp;&emsp;&ensp;FROM user_friendship AS uf\
&emsp;&emsp;&ensp;LEFT OUTER JOIN friendship as f ON uf.friendship_id = f.friendship_id\
&emsp;&emsp;&ensp;WHERE uf.user_id = 1 AND f.status = 'ACCEPTED');




