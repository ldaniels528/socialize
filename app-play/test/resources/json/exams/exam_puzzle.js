db.exams.insert({
    "name": "Coding Puzzles Exam",
    "description": "An Exam containing Development Puzzles",
    "likedBy": ["5633c756d9d5baa77a714803"],
    "likes": 1,
    "submitter": {
        "avatarURL": "https://media.licdn.com/mpr/mpr/shrinknp_400_400/p/3/000/0a6/0bc/2a4b0ba.jpg",
        "name": "Lawrence Daniels, MBA",
        "_id": ObjectId("5633c756d9d5baa77a714803")
    },
    "questions": [{
        "tags": ["puzzle"],
        "difficulty": 1,
        "type": "MULTIPLE_CHOICE",
        "question": "You have eight coins which, except for one that is slightly " +
        "heavier, are all the same weight. You don't know which coin is heavier. You have an " +
        "old‐style balance, which allows you to weigh two piles of coins to see which one is " +
        "heavier or if they are of equal weight. What is the fewest number of weighings " +
        "that you can make which will tell you which coin is the heavier one?",
        "answers": [{
            "_id": ObjectId(),
            "text": "Weigh 4 coins against 4 coins. Remove a coing from the heavier side. Remove a " +
            "second coin from the heavier side. If the change from one of the removal was greater than the other then that's " +
            "the heavier coin. If not remove the third coin. If the change is similar to the previous two removal then the " +
            "last coin is the hevier. If not then the last coin removed is the heavier.",
            "correct": false
        }, {
            "_id": ObjectId(),
            "text": "Weigh 4 coins against 4 coins. Discard the lighter coins, and weigh 2 coins " +
            "against 2 coins. Discard the lighter coins and weigh the remaining two coins.",
            "correct": false
        }, {
            "_id": ObjectId(),
            "text": "Weigh 3 coins against 3 coins. If one of the groups is heavier, weighvone of its " +
            "coins against another one of its coins; this allows you to identify the heavy coin. If the two groups balance, " +
            "weigh the two leftover coins.",
            "correct": true
        }]
    }]
});