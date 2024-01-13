CREATE TABLE Locations (
                           ID INT AUTO_INCREMENT PRIMARY KEY,
                           Name VARCHAR(100),
                           UserId INT REFERENCES users(ID) ON DELETE CASCADE,
                           Latitude DECIMAL(30,20),
                           Longitude DECIMAL(30,20)
);