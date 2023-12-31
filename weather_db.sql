create table Users(
                      ID int auto_increment primary key ,
                      Login varchar(100) NOT NULL,
                      Password varchar(60) NOT NULL
);

create table Locations(
                          ID int auto_increment primary key ,
                          Name varchar(100),
                          UserId int references users(ID) on delete cascade,
                          Latitude DECIMAL,
                          Longitude DECIMAL
);

CREATE TABLE Sessions
(
    ID        VARCHAR(36) PRIMARY KEY DEFAULT UUID(),
    UserId    INT references users (ID) on delete cascade,
    ExpiresAt DATETIME default (CURRENT_TIMESTAMP + INTERVAL 1 HOUR )
)