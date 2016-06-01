db.exams.insert({
    "name": "Intermediate Java Exam",
    "description": "An Exam for Intermediate Java Developers",
    "likedBy": ["5633c756d9d5baa77a714803"],
    "likes": 1,
    "submitter": {
        "avatarURL": "https://media.licdn.com/mpr/mpr/shrinknp_400_400/p/3/000/0a6/0bc/2a4b0ba.jpg",
        "name": "Lawrence Daniels, MBA",
        "_id": ObjectId("5633c756d9d5baa77a714803")
    },
    "questions": [{
        "tags": ["java"],
        "difficulty": NumberInt(2),
        "type": "MULTIPLE_CHOICE",
        "question": "What will the following block of code print out?",
        "code": "int x = 1;\n" +
        "int y = 2;\n" +
        "boolean alwaysTrue = true;\n\n" +
        "if ((x>0) && (y<2 || alwaysTrue)) {\n" +
        "   System.out.println(\"It came to me!\");\n" +
        "}",
        "answers": [
            {"_id": ObjectId(), "text": "It came to me!", "correct": true},
            {"_id": ObjectId(), "text": "Compilation error", "correct": false},
            {"_id": ObjectId(), "text": "Nothing", "correct": false},
            {"_id": ObjectId(), "text": "None of the above", "correct": false}]
    }, {
        "tags": ["java"],
        "difficulty": NumberInt(2),
        "type": "MULTIPLE_CHOICE",
        "question": "What will the following block of code print out?",
        "code": "int alwaysTrue = 1;\n" +
        "if (alwaysTrue) {\n" +
        "   System.out.println(\"I will always run!\");\n" +
        "}\n",
        "answers": [
            {"_id": ObjectId(), "text": "I will always run!", "correct": false},
            {"_id": ObjectId(), "text": "Compilation error", "correct": true},
            {"_id": ObjectId(), "text": "Nothing", "correct": false},
            {"_id": ObjectId(), "text": "None of the above", "correct": false}]
    }, {
        "tags": ["java"],
        "difficulty": NumberInt(2),
        "type": "MULTIPLE_CHOICE",
        "question": "What will the following code snippet print out?",
        "code": "int a = 5;\n" +
        "boolean iAmNotReady = false;\n" +
        "if (iAmNotReady = true) {\n" +
        "    System.out.println(\"Value of a: \" + a);\n" +
        "}",
        "answers": [
            {"_id": ObjectId(), "text": "Value of a: 5", "correct": false},
            {"_id": ObjectId(), "text": "Compilation error", "correct": true},
            {"_id": ObjectId(), "text": "Nothing", "correct": false},
            {"_id": ObjectId(), "text": "None of the above", "correct": false}]
    }, {
        "tags": ["java"],
        "difficulty": NumberInt(2),
        "type": "MULTIPLE_CHOICE",
        "question": "What will the following code snippet print out?",
        "code": "for (int a=1,b=2,c=3; a<10; a++) {\n" +
        "    System.out.println(b+c);\n" +
        "}",
        "answers": [
            {"_id": ObjectId(), "text": "print 5 nine times", "correct": true},
            {"_id": ObjectId(), "text": "Compilation error", "correct": false},
            {"_id": ObjectId(), "text": "Nothing", "correct": false},
            {"_id": ObjectId(), "text": "Runtime error", "correct": false}]
    }, {
        "tags": ["java"],
        "difficulty": NumberInt(2),
        "type": "MULTIPLE_CHOICE",
        "question": "What will the following code snippet print out?",
        "code": "for (int x=0; x<5; x++) {\n" +
        "    System.out.print(x+\", \");\n" +
        "}\n" +
        "System.out.print(++x);",
        "answers": [
            {"_id": ObjectId(), "text": "0, 1, 2, 3, 4, 5", "correct": false},
            {"_id": ObjectId(), "text": "Compilation error", "correct": true},
            {"_id": ObjectId(), "text": "Runtime error", "correct": false},
            {"_id": ObjectId(), "text": "None of the above", "correct": false}]
    }, {
        "tags": ["java"],
        "difficulty": NumberInt(2),
        "type": "MULTIPLE_CHOICE",
        "question": "What will happen with the following code snippet?",
        "code": "for (;;) {\n" +
        "    System.out.println(\"Somebody stop me!\");\n" +
        "}",
        "answers": [
            {"_id": ObjectId(), "text": "Compilation error", "correct": false},
            {"_id": ObjectId(), "text": "Infinite loop", "correct": true},
            {"_id": ObjectId(), "text": "OutOfMemoryError", "correct": false},
            {"_id": ObjectId(), "text": "Nothing", "correct": false},
            {"_id": ObjectId(), "text": "prints \"Sombody stop me!\" once and then exits", "correct": false}]
    }, {
        "tags": ["java"],
        "difficulty": NumberInt(2),
        "type": "MULTIPLE_CHOICE",
        "question": "In Java, a \"thread of execution\" is an instance of class java.lang.Thread.",
        "answers": [
            {"_id": ObjectId(), "text": "True", "correct": false},
            {"_id": ObjectId(), "text": "False", "correct": true}]
    }, {
        "tags": ["java"],
        "difficulty": NumberInt(2),
        "type": "MULTIPLE_CHOICE",
        "question": "In Java, every \"thread of execution\" has its own call stack.",
        "answers": [
            {"_id": ObjectId(), "text": "True", "correct": true},
            {"_id": ObjectId(), "text": "False", "correct": false}]
    }]
});

