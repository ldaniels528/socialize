db.interviews.drop();

db.interviews.insert({
    companyName: "Google Inc.",
    position: "Software in Test",
    location: {city: "Irvine", state: "CA"},
    submitter: { _id: ObjectId("5633c756d9d5baa77a714803"), name:"Lawrence Daniels", primaryEmail:"lawrence.daniels@gmail.com", creationTime: new Date() },
    active: true,
    tags: ["Java", "Algorithms", "Google"],
    creationTime: new Date()
});

db.interviews.insert({
    companyName: "Toyota",
    position: "Cloud Architect",
    location: {city: "Torrance", state: "CA"},
    submitter: { _id: ObjectId("5633c756d9d5baa77a714803"), name:"Lawrence Daniels", primaryEmail:"lawrence.daniels@gmail.com", creationTime: new Date() },
    active: true,
    tags: ["Cloud", "Big Data"],
    creationTime: new Date()
});