create table Users(
                      ID int auto_increment primary key ,
                      Login varchar(100) NOT NULL UNIQUE,
                      Password varchar(60) NOT NULL
);