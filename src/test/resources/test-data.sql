-- test-data.sql
-- Create the api_call table
CREATE TABLE IF NOT EXISTS api_call
(
    id        INTEGER PRIMARY KEY AUTO_INCREMENT,
    endpoint  TEXT NOT NULL,
    timestamp TEXT NOT NULL
);

-- Insert some test data
INSERT INTO api_call (endpoint, timestamp)
VALUES ('/weather', '2022-01-01T00:00:00');
INSERT INTO api_call (endpoint, timestamp)
VALUES ('/forecast', '2022-01-02T00:00:00');