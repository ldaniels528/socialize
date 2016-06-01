db.organizations.drop();
db.organizations.ensureIndex({name: 1});

db.organizations.insert({
    name: "Amazon",
    creationTime: new Date()
});

db.organizations.insert({
    name: "Apple",
    creationTime: new Date()
});

db.organizations.insert({
    name: "Facebook",
    creationTime: new Date()
});

db.organizations.insert({
    name: "Google",
    creationTime: new Date()
});

db.organizations.insert({
    name: "IBM",
    creationTime: new Date()
});

db.organizations.insert({
    name: "Intel Corporation",
    creationTime: new Date()
});

db.organizations.insert({
    name: "LinkedIn",
    creationTime: new Date()
});

db.organizations.insert({
    name: "MFW Company",
    creationTime: new Date()
});

db.organizations.insert({
    name: "Microsoft Corporation",
    avatarURL: "https://pbs.twimg.com/profile_images/543484284716077057/EfWdfyI3_bigger.jpeg",
    creationTime: new Date()
});

db.organizations.insert({
    name: "Twitter",
    creationTime: new Date()
});