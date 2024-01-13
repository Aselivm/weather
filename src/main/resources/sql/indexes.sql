CREATE INDEX login_idx ON users(Login);
CREATE INDEX expiresAt_idx ON sessions(ExpiresAt);
CREATE UNIQUE INDEX idx_unique_location ON Locations (Latitude, Longitude, UserId);
