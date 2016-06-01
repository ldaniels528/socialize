db.sessions.createIndex({ "lastUpdated": 1 }, { expireAfterSeconds: 8*3600 });

db.sessions.update({
    "_id": ObjectId("563eb3cf1b591f90710063a1")
}, {
    "username": "lawrence.daniels@gmail.com",
    "isAnonymous": false,
    "creationTime": new Date(),
    "userID": ObjectId("5633c756d9d5baa77a714803"),
    "avatarURL": "https://media.licdn.com/mpr/mpr/shrinknp_400_400/p/3/000/0a6/0bc/2a4b0ba.jpg"
});
