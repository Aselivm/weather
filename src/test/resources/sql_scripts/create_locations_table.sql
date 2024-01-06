create table Locations(
                          ID int auto_increment primary key ,
                          Name varchar(100),
                          UserId int references Users(ID) on delete cascade,
                          Latitude DECIMAL(10,7),
                          Longitude DECIMAL(10,7)
);