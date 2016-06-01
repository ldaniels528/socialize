db.posts.drop();

db.posts.insert({
    text: "Just published Socialized v0.8.20!",
    submitter: { _id: ObjectId("5633c756d9d5baa77a714803"), name:"Lawrence Daniels, MBA", primaryEmail:"lawrence.daniels@gmail.com" },
    likes: 0,
    likedBy: [],
    comments: [{
        _id: ObjectId(), text: "That's cool! ;-)", creationTime: new Date(), likes: 0, likedBy: [],
        submitter: { _id: ObjectId("56340a6f3c21a4b485d47c55"), name:"Michael Williams", primaryEmail:"mfwilliams@gmail.com" }
    }],
    tags: ["akka", "angular.js", "mongodb", "play", "scala", "scala.js"],
    creationTime: new Date(),
    lastUpdateTime: new Date()
});

db.posts.insert({
    text: "Working really hard to get the next release of Socialized (v0.8.2) out the door!",
    submitter: { _id: ObjectId("5633c756d9d5baa77a714803"), name:"Lawrence Daniels, MBA", primaryEmail:"lawrence.daniels@gmail.com" },
    likes: 0,
    likedBy: [],
    comments: [],
    tags: ["akka", "angular.js", "mongodb", "play", "scala", "scala.js"],
    creationTime: new Date("10/29/2015"),
    lastUpdateTime: new Date()
});

db.posts.insert({
    text: "Tech Unemployment Rises In Some Categories",
    link: "http://insights.dice.com/2015/10/28/tech-unemployment-rises-in-some-categories/?CMPID=EM_RE_UP_JS_AD_DA_CP_&utm_source=Responsys&utm_medium=Email&utm_content=&utm_campaign=Advisory_DiceAdvisor",
    submitter: { _id: ObjectId("56340a6f3c21a4b485d47c55"), name:"Michael Williams", primaryEmail:"mfwilliams@gmail.com" },
    likes: 0,
    likedBy: [],
    comments: [],
    tags: ["dice.com", "news", "unemployment"],
    creationTime: new Date("10/29/2015"),
    lastUpdateTime: new Date()
});

db.posts.insert({
    text: "Working on the authentication logic for Socialized, it's almost there!",
    submitter: { _id: ObjectId("56340a6f3c21a4b485d47c55"), name:"Michael Williams", primaryEmail:"mfwilliams@gmail.com" },
    likes: 0,
    likedBy: [],
    comments: [],
    tags: ["akka", "angular.js", "mongodb", "play", "scala", "scala.js"],
    creationTime: new Date("10/28/2015"),
    lastUpdateTime: new Date()
});