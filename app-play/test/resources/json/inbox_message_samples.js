db.inbox_messages.drop();

db.inbox_messages.insert({
    title: "Endorse me!",
    message: "It's been a while",
    creationTime: new Date()
});

db.inbox_messages.insert({
    title: "Gaikai offer",
    message: "It brings us great please to offer you a position at Sony Computer Entertainment",
    creationTime: new Date("03/11/2013")
});