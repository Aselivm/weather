create table Users(
                      ID int auto_increment primary key ,
                      Login VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL UNIQUE,
                      Password varchar(60) NOT NULL
);