create TABLE IF NOT EXISTS users (
    user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_email varchar,
    user_login varchar,
    user_name varchar,
    user_birthday timestamp
);

create TABLE IF NOT EXISTS friendship (
    friendship_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    status varchar
);

create TABLE IF NOT EXISTS user_friendship (
    user_id INTEGER REFERENCES users (user_id),
    friend_id INTEGER,
    friendship_id INTEGER REFERENCES friendship (friendship_id)
);

create TABLE IF NOT EXISTS films (
    film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_name varchar,
    film_description varchar(200),
    film_release_date timestamp,
    film_duration INTEGER
);

create TABLE IF NOT EXISTS film_likes (
    film_id INTEGER REFERENCES films (film_id),
    user_id INTEGER REFERENCES users (user_id)
);

create TABLE IF NOT EXISTS rating (
    rating_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    rating_name varchar
);

create TABLE IF NOT EXISTS film_rating (
    film_id INTEGER REFERENCES films (film_id),
    rating_id INTEGER REFERENCES rating (rating_id)
);

create TABLE IF NOT EXISTS genre (
    genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name varchar
);

create TABLE IF NOT EXISTS film_genre (
    film_id INTEGER  REFERENCES films (film_id),
    genre_id INTEGER REFERENCES genre (genre_id)
);





