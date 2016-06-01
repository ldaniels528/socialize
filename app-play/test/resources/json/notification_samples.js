db.notifications.drop();

db.notifications.insert({
    "title": "QuakeCon",
    "type": "EVENT",
    "text": "QuakeCon starts in 2 days",
    "owner": { _id: ObjectId("5633c756d9d5baa77a714803"), name:"Lawrence Daniels, MBA", primaryEmail:"lawrence.daniels@gmail.com" },
    "eventTime" : new Date("11/22/2015"),
    "creationTime" : new Date()
});

db.notifications.insert({
    "title": "ScalaCon",
    "type": "EVENT",
    "text": "ScalaCon starts in 10 days",
    "owner": { _id: ObjectId("5633c756d9d5baa77a714803"), name:"Lawrence Daniels, MBA", primaryEmail:"lawrence.daniels@gmail.com" },
    "eventTime" : new Date("12/07/2015"),
    "creationTime" : new Date()
});

db.notifications.insert({
    "title": "Lawrence has leveled up!",
    "type": "LEVELUP",
    "text": "Lawrence's level has increased to 17",
    "owner": { _id: ObjectId("5633c756d9d5baa77a714803"), name:"Lawrence Daniels, MBA", primaryEmail:"lawrence.daniels@gmail.com" },
    "creationTime" : new Date()
});

db.notifications.insert({
    "title": "Lawrence aced a test!",
    "type": "ACHIEVEMENT",
    "text": "Lawrence scored 100% in Java, Cloud and Scala",
    "owner": { _id: ObjectId("5633c756d9d5baa77a714803"), name:"Lawrence Daniels, MBA", primaryEmail:"lawrence.daniels@gmail.com" },
    "creationTime" : new Date("10/30/2015")
});

db.notifications.insert({
    "title": "Lawrence commented on a post",
    "type": "COMMENT",
    "text": "Lawrence commented on Michael Williams' post",
    "owner": { _id: ObjectId("5633c756d9d5baa77a714803"), name:"Lawrence Daniels, MBA", primaryEmail:"lawrence.daniels@gmail.com" },
    "creationTime" : new Date("10/29/2015")
});

db.notifications.insert({
    "title": "Michael shared a post",
    "type": "COMMENT",
    "text": "Michael shared a post",
    "owner": { _id: ObjectId("56340a6f3c21a4b485d47c55"), name:"Michael Williams", primaryEmail:"mfwilliams@gmail.com" },
    "creationTime" : new Date("10/29/2015")
});