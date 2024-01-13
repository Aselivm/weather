CREATE TABLE Locations (
                           ID INT AUTO_INCREMENT PRIMARY KEY,
                           Name VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                           UserId INT,
                           Latitude DECIMAL(30,20),
                           Longitude DECIMAL(30,20),
                           FOREIGN KEY (UserId) REFERENCES Users(ID) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;