CREATE DATABASE battleship;
USE battleship;

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) not null,
    flag VARCHAR(255) not null
);

CREATE TABLE match_party (
    id INT AUTO_INCREMENT PRIMARY KEY,
    victory BOOLEAN,
    sunken_boats INT,
    score INT,
    number_of_shots INT,
    opponent_name VARCHAR(255)
);

/*
    INSERT INTO user (name, flag) VALUES ("Uriel", "./src/controlers/test/mexico.png") 
*/

/* 
    INSERT INTO match (user_id, 
                    victory,
                    sunken_boats,
                    score,
                    number_of_shots,
                    opponent_name) VALUES (1, TRUE, 5, 20, 27, "ale"); 
*/