create table Users(
                      ID int auto_increment primary key ,
<<<<<<< HEAD
                      Login varchar(100) NOT NULL,
                      Password varchar(60) NOT NULL
);
=======
                      Login varchar(100),
                      Password varchar(60)
)
<<<<<<< HEAD
>>>>>>> d9e4b6772072c04d51ea09510b071bc093389507
=======
>>>>>>> d9e4b6772072c04d51ea09510b071bc093389507

create table Locations(
                          ID int auto_increment primary key ,
                          Name varchar(100),
                          UserId int references users(ID) on delete cascade,
                          Latitude DECIMAL,
                          Longitude DECIMAL
<<<<<<< HEAD
<<<<<<< HEAD
);
=======
)
>>>>>>> d9e4b6772072c04d51ea09510b071bc093389507
=======
)
>>>>>>> d9e4b6772072c04d51ea09510b071bc093389507

CREATE TABLE Sessions
(
    ID        VARCHAR(36) PRIMARY KEY DEFAULT UUID(),
    UserId    INT references users (ID) on delete cascade,
    ExpiresAt DATETIME default (CURRENT_TIMESTAMP + INTERVAL 1 HOUR )
)