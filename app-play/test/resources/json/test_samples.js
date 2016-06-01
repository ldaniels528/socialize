db.tests.drop();
db.tests.createIndex({name: 1}, {unique: 1});


db.tests.insert({
    name: "Java 101",
    questions: [
        {
            "tags": ["java"],
            "difficulty": 1,
            "type": "MULTIPLE_CHOICE",
            "question": "Why do we need \"wrapper\" classes in Java?",
            "answerIndex": 3,
            "answers": [
                "To \"wrap\" primitive values in an Object",
                "To be able to add primitive values to collections",
                "A way to return primitive values from a method that returns an object",
                "All of the above"
            ]
        }, {
            "tags": ["java"],
            "difficulty": 1,
            "type": "MULTIPLE_CHOICE",
            "question": "In Java, there is a \"wrapper\" class for all the primitives.",
            "answerIndex": 0,
            "answers": [
                "True",
                "False"
            ]
        }, {
            "tags": ["java"],
            "difficulty": 1,
            "type": "MULTIPLE_CHOICE",
            "question": "Which of the following is not a valid \"wrapper\" class in Java?",
            "answerIndex": 2,
            "answers": [
                "Integer",
                "Byte",
                "Char",
                "Double"
            ]
        }, {
            "tags": ["java"],
            "difficulty": 1,
            "type": "MULTIPLE_CHOICE",
            "question": "Wrapper objects are immutable.",
            "answerIndex": 0,
            "answers": [
                "True",
                "False"
            ]
        }, {
            "tags": ["java"],
            "difficulty": 1,
            "type":"TEST",
            "question" : "Which of the following statements is invalid?",
            "answerIndex": 2,
            "answers" : [
                "Integer i = new Integer(\"100\");",
                "Float f = new Float(\"25.3f\");",
                "Character c = new Character(\"c\");",
                "Double d = new Double(\"50.00\");",
                "None of the above"
            ]
        }, {
            "tags": ["java"],
            "difficulty": 1,
            "type":"TEST",
            "question" : "The class \"File\" is an abstract representation of file and directory path names.",
            "answerIndex": 0,
            "answers" : [
                "True",
                "False"
            ]
        }, {
            "tags": ["java"],
            "difficulty": 1,
            "type":"TEST",
            "question" : "The BufferedReader class provided better performance than the FileReader class when reading a large amount of characters from a file.",
            "answerIndex": 0,
            "answers" : [
                "True",
                "False"
            ]
        }
    ]
});