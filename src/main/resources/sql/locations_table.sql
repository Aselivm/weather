create table Locations(
                          ID int auto_increment primary key ,
                          Name varchar(100),
                          UserId int references users(ID) on delete cascade,
                          Latitude DECIMAL(11,8),
                          Longitude DECIMAL(11,8)
);