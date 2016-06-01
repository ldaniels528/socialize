CREATE TABLE dbo.credentials (
  uid          INTEGER              IDENTITY (1, 1) PRIMARY KEY,
  username     VARCHAR(64) NOT NULL,
  hashPassword VARCHAR(64) NOT NULL,
  hashSalt     VARCHAR(64) NOT NULL,
  creationTime DATETIME    NOT NULL DEFAULT GETDATE()
);

CREATE UNIQUE INDEX credentials_xpk ON dbo.credentials (username);
