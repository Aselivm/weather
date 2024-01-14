create table Users(
                      ID int auto_increment primary key ,
                      Login VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL UNIQUE,
                      Password varchar(60) NOT NULL
);
CREATE TABLE Locations (
                           ID INT AUTO_INCREMENT PRIMARY KEY,
                           Name VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                           UserId INT,
                           Latitude DECIMAL(30,20),
                           Longitude DECIMAL(30,20),
                           FOREIGN KEY (UserId) REFERENCES Users(ID) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE TABLE Sessions
(
    ID        VARCHAR(36) PRIMARY KEY DEFAULT UUID(),
    UserId    INT references users (ID) on delete cascade,
    ExpiresAt DATETIME NOT NULL
);

CREATE INDEX login_idx ON users(Login);
CREATE INDEX expiresAt_idx ON sessions(ExpiresAt);
CREATE UNIQUE INDEX idx_unique_location ON Locations (Latitude, Longitude, UserId);