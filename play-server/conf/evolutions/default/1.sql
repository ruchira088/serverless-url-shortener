# ---!Ups
CREATE TABLE url(
  url_key VARCHAR(255) NOT NULL PRIMARY KEY,
  created_at TIMESTAMP NOT NULL,
  long_url VARCHAR(2047) NOT NULL,
  hits INT NOT NULL
);

# ---!Downs
DROP TABLE url;
