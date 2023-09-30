merge into genre (genre_id, genre_name) values (1, 'Комедия');
merge into genre (genre_id, genre_name) values (2, 'Драма');
merge into genre (genre_id, genre_name) values (3, 'Мультфильм');
merge into genre (genre_id, genre_name) values (4, 'Триллер');
merge into genre (genre_id, genre_name) values (5, 'Документальный');
merge into genre (genre_id, genre_name) values (6, 'Боевик');

merge into rating (rating_id, rating_name) values (1, 'G');
merge into rating (rating_id, rating_name) values (2, 'PG');
merge into rating (rating_id, rating_name) values (3, 'PG-13');
merge into rating (rating_id, rating_name) values (4, 'R');
merge into rating (rating_id, rating_name) values (5, 'NC-17');

merge into friendship (friendship_id, status) values (1, 'Неподтверждённая');
merge into friendship (friendship_id, status) values (2, 'Подтверждённая');