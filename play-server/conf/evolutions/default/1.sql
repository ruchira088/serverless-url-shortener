# ---!Ups
CREATE TABLE url(
  key VARCHAR(255) NOT NULL PRIMARY KEY,
  created_at TIMESTAMP NOT NULL,
  long_url TEXT NOT NULL,
  hits REAL NOT NULL
);

# ---!Downs
DROP TABLE url;