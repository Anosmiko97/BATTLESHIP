CREATE DATABASE battleship;
USE battleship;

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) not null,
);

CREATE TABLE match (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT
    victory BOOLEAN,
    sunken_boats INT,
    score INT,
    number_of_shots INT,
    opponent_name VARCHAR(255),

    FOREIGN KEY (user_id) REFERENCES user(id)
);