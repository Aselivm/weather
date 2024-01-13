CREATE TABLE Sessions
(
    ID        VARCHAR(36) PRIMARY KEY DEFAULT UUID(),
    UserId    INT references users (ID) on delete cascade,
    ExpiresAt DATETIME NOT NULL
);