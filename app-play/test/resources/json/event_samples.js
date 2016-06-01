db.events.drop();

db.events.insert({
    "title": "QuakeCon",
    "type": "CONFERENCE",
    "owner": { _id: ObjectId("5633c756d9d5baa77a714803"), name:"Lawrence Daniels, MBA", primaryEmail:"lawrence.daniels@gmail.com" },
    "eventTime" : new Date("12/22/2015"),
    "creationTime" : new Date()
});

db.events.insert({
    "title": "ScalaCon",
    "type": "CONFERENCE",
    "owner": { _id: ObjectId("56421dc13200006600585318"), name:"Lawrence Daniels, MBA", primaryEmail:"lawrence.daniels@gmail.com" },
    "eventTime" : new Date("01/07/2015"),
    "creationTime" : new Date()
});
