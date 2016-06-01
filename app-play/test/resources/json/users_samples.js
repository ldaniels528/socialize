db.users.drop();
db.users.ensureIndex({userName: 1});
db.users.ensureIndex({primaryEmail: 1});

db.users.insert({
    "_id": ObjectId("5633c756d9d5baa77a714803"),
    "screenName": "ldaniels",
    "firstName": "Lawrence",
    "lastName": "Daniels",
    "gender": "M",
    "credentials": ["MBA"],
    "title": "Cloud/Big Data Architect",
    "company": "Thales Avionics",
    "level": 17,
    "avatarId": null,
    "avatarURL": "https://media.licdn.com/mpr/mpr/shrinknp_400_400/p/3/000/0a6/0bc/2a4b0ba.jpg",
    "primaryEmail": "lawrence.daniels@gmail.com",
    "creationTime" : new Date()
});

db.users.insert({
    "_id": ObjectId("56340a6f3c21a4b485d47c55"),
    "screenName": "mwilliams",
    "firstName": "Michael",
    "lastName": "Williams",
    "gender": "M",
    "credentials": [],
    "title": "Builder of Teams/Products/Tech/Companies",
    "company": "The MFW Company",
    "level": 21,
    "avatarId": null,
    "avatarURL": "https://media.licdn.com/mpr/mpr/shrinknp_400_400/p/6/000/20f/043/029913f.jpg",
    "primaryEmail": "mfwilliams@gmail.com",
    "creationTime" : new Date()
});