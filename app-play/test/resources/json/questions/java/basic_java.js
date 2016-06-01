db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "Why do we need \"wrapper\" classes in Java?",
    "answers": [
        {"_id": ObjectId(), "text": "To \"wrap\" primitive values in an Object", "correct": false},
        {"_id": ObjectId(), "text": "To be able to add primitive values to collections", "correct": false},
        {
            "_id": ObjectId(),
            "text": "A way to return primitive values from a method that returns an object",
            "correct": false
        },
        {"_id": ObjectId(), "text": "All of the above", "correct": true}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "In Java, there is a \"wrapper\" class for all the primitives.",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": true},
        {"_id": ObjectId(), "text": "False", "correct": false}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "Which of the following is not a valid \"wrapper\" class in Java?",
    "answers": [
        {"_id": ObjectId(), "text": "Integer", "correct": false},
        {"_id": ObjectId(), "text": "Byte", "correct": false},
        {"_id": ObjectId(), "text": "Char", "correct": true},
        {"_id": ObjectId(), "text": "Double", "correct": false}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "Wrapper objects are immutable.",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": true},
        {"_id": ObjectId(), "text": "False", "correct": false}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "Which of the following statements is invalid?",
    "answers": [
        {"_id": ObjectId(), "text": "Integer i = new Integer(\"100\");", "correct": false},
        {"_id": ObjectId(), "text": "Float f = new Float(\"25.3f\");", "correct": false},
        {"_id": ObjectId(), "text": "Character c = new Character(\"c\");", "correct": true},
        {"_id": ObjectId(), "text": "Double d = new Double(\"50.00\");", "correct": false},
        {"_id": ObjectId(), "text": "None of the above", "correct": false}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "The class \"File\" is an abstract representation of file and directory path names.",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": true},
        {"_id": ObjectId(), "text": "False", "correct": false}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "The BufferedReader class provided better performance than the FileReader class when reading a large amount of characters from a file.",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": true},
        {"_id": ObjectId(), "text": "False", "correct": false}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "Stream classes are used to read and write characters whereas Reader and Writer classes are used to read and write bytes.",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": false},
        {"_id": ObjectId(), "text": "False", "correct": true}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "When you initialize a File object, you create an actual file/folder on the disk.",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": false},
        {"_id": ObjectId(), "text": "False", "correct": true}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "Which methods in the Java API are used to serialize or deserialize objects?",
    "answers": [
        {"_id": ObjectId(), "text": "writeBytes", "correct": false},
        {"_id": ObjectId(), "text": "retrieveObject", "correct": false},
        {"_id": ObjectId(), "text": "writeObject", "correct": true},
        {"_id": ObjectId(), "text": "getObject", "correct": false}
    ]
});

db.questions.insert({
    "tags": ["scala"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "What is the advantage of Scala?",
    "answers": [
        {"_id": ObjectId(), "text": "Less error prone functional style", "correct": false},
        {"_id": ObjectId(), "text": "High maintainability and productivity", "correct": false},
        {"_id": ObjectId(), "text": "High scalability", "correct": false},
        {"_id": ObjectId(), "text": "High testability", "correct": false},
        {"_id": ObjectId(), "text": "Provides features of concurrent programming", "correct": false},
        {"_id": ObjectId(), "text": "All of the above", "correct": true}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "The expression in an \"if\" statement must be a boolean expression.",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": true},
        {"_id": ObjectId(), "text": "False", "correct": false}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "It is legal to use an enum to evaluate the expression in a \"switch\" statement.",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": true},
        {"_id": ObjectId(), "text": "False", "correct": false}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "Which one of the following types can the \"switch\" expression not evaluate to?",
    "answers": [
        {"_id": ObjectId(), "text": "int", "correct": false},
        {"_id": ObjectId(), "text": "long", "correct": true},
        {"_id": ObjectId(), "text": "short", "correct": false},
        {"_id": ObjectId(), "text": "byte", "correct": false}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "There is a loop construct in Java, that lets you execute the code inside the loop body even when the loop expression fails or evaluates to \"false\".",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": true},
        {"_id": ObjectId(), "text": "False", "correct": false}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "There are two possible ways to define and instantiate a thread in Java.",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": true},
        {"_id": ObjectId(), "text": "False", "correct": false}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "When it comes to setting up a custom thread class in Java, extending the java.lang.Thread class is generally considered a better alternative to implementing the Runnable interface.",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": false},
        {"_id": ObjectId(), "text": "False", "correct": true}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "If you invoke the start() method more than once on the same thread instance, it will throw an IllegalThreadStateException.",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": true},
        {"_id": ObjectId(), "text": "False", "correct": false}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "The class java.lang.Thread itself implements the Runnable interface.",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": true},
        {"_id": ObjectId(), "text": "False", "correct": false}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "When you invoke a synchronized method on an object, its built-in lock is used to protect the method's code.",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": true},
        {"_id": ObjectId(), "text": "False", "correct": false}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "In all instances of running a synchronized code, an object level lock must be acquired.",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": false},
        {"_id": ObjectId(), "text": "False", "correct": true}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "Synchronization can cause deadlocks",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": true},
        {"_id": ObjectId(), "text": "False", "correct": false}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "When a thread goes to sleep, it releases all the locks it has acquired.",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": false},
        {"_id": ObjectId(), "text": "False", "correct": true}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "Synchronization creates a performance hit on your code.",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": true},
        {"_id": ObjectId(), "text": "False", "correct": false}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "You can synchronize methods, blocks of code and variables, but not classes.",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": false},
        {"_id": ObjectId(), "text": "False", "correct": true}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "A thread cannot acquire more than one lock at a time.",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": false},
        {"_id": ObjectId(), "text": "False", "correct": true}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "Synchronizing at the method declaration/signature (basically the whole method) provides better performance gains than synchronizing just a subset (block) of the method code",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": false},
        {"_id": ObjectId(), "text": "False", "correct": true}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "Static methods cannot be synchronized.",
    "answers": [
        {"_id": ObjectId(), "text": "True", "correct": false},
        {"_id": ObjectId(), "text": "False", "correct": true}
    ]
});

db.questions.insert({
    "tags": ["java"],
    "difficulty": NumberInt(1),
    "type": "MULTIPLE_CHOICE",
    "question": "Which one of the following methods is not defined in java.lang.Thread class?",
    "answers": [
        {"_id": ObjectId(), "text": "wait()", "correct": true},
        {"_id": ObjectId(), "text": "join()", "correct": false},
        {"_id": ObjectId(), "text": "sleep()", "correct": false},
        {"_id": ObjectId(), "text": "yield()", "correct": false}
    ]
});
