db.questions.drop();
db.questions.ensureIndex({tags: 1});

db.questions.insert({
    "tags": ["java"],
    "difficulty": 1,
    "type": "MULTIPLE_CHOICE",
    "question": "",
    "answers": [
        {"_id": ObjectId(), "text": "", "correct": false},
        {"_id": ObjectId(), "text": "", "correct": false},
        {"_id": ObjectId(), "text": "", "correct": false},
        {"_id": ObjectId(), "text": "", "correct": false},
        {"_id": ObjectId(), "text": "", "correct": false}
    ]
});