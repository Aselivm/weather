create table Users(
                      ID int auto_increment primary key ,
                      Login VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL UNIQUE,
                      Password varchar(60) NOT NULL
);
create table Locations(
                          ID int auto_increment primary key ,
                          Name varchar(100),
                          UserId int references users(ID) on delete cascade,
                          Latitude DECIMAL(10,7),
                          Longitude DECIMAL(10,7)
);
CREATE TABLE Sessions
(
    ID        VARCHAR(36) PRIMARY KEY DEFAULT UUID(),
    UserId    INT references users (ID) on delete cascade,
    ExpiresAt DATETIME
);

CREATE INDEX login_idx ON users(Login);
CREATE INDEX expiresAt_idx ON sessions(ExpiresAt);