create table Users(
                      ID int auto_increment primary key ,
                      Login VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL UNIQUE,
                      Password varchar(60) NOT NULL
);
CREATE TABLE Locations (
                           ID INT AUTO_INCREMENT PRIMARY KEY,
                           Name VARCHAR(100),
                           UserId INT REFERENCES users(ID) ON DELETE CASCADE,
                           Latitude DECIMAL(30,20),
                           Longitude DECIMAL(30,20)
);
CREATE TABLE Sessions
(
    ID        VARCHAR(36) PRIMARY KEY DEFAULT UUID(),
    UserId    INT references users (ID) on delete cascade,
    ExpiresAt DATETIME NOT NULL
);

CREATE INDEX login_idx ON users(Login);
CREATE INDEX expiresAt_idx ON sessions(ExpiresAt);