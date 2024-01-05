CREATE TABLE Sessions
(
    ID        varchar(36) DEFAULT random_uuid() primary key,
    UserId    INT references users (ID) on delete cascade,
    ExpiresAt DATETIME
);