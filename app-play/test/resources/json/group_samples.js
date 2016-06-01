db.groups.drop();
db.groups.ensureIndex({name: 1});

db.groups.insert({
    name: "Full Stack Developers",
    creationTime: new Date()
});

db.groups.insert({
    name: "Scala.js Developers",
    creationTime: new Date()
});