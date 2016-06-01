// Create username index on credential
db.credentials.createIndex({username: 1}, {unique: true});

// TTL index for events - 24 hours
db.notification_log.createIndex({ "creationTime": 1 }, { expireAfterSeconds: 3600*24 });