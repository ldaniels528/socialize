db.questions.insert({
    "tags": ["oop"],
    "difficulty": 1,
    "type": "MULTIPLE_CHOICE",
    "question": "What is a class?",
    "answers": [
        {
            "_id": ObjectId(),
            "text": "A class describes all the attributes of objects, as well as the methods that implement the behavior of member " +
            "objects. It is a comprehensive data type, which represents a blue print of objects. It is a template of object. ",
            "correct": false
        },
        {
            "_id": ObjectId(),
            "text": "A class can be defined as the primary building block of OOP. It also serves as a template that describes the " +
            "properties, state, and behaviors common to a particular group of objects.",
            "correct": false
        },
        {
            "_id": ObjectId(),
            "text": "A class contains data and behavior of an entity. For example, the aircraft class can contain data, such as " +
            "model number, category, and color and behavior, such as duration of flight, speed, and number of passengers. " +
            "A class inherits the data members and behaviors of other classes by extending from them.",
            "correct": false
        },
        {"_id": ObjectId(), "text": "All of the above", "correct": true}
    ]
});

db.questions.insert({
    "tags": ["cloud"],
    "difficulty": 1,
    "type": "MULTIPLE_CHOICE",
    "question": "What are the main types or categories of cloud?",
    "answers": [
        {"_id": ObjectId(), "text": "Public Cloud, Private Cloud and Hybrid Cloud", "correct": false},
        {
            "_id": ObjectId(),
            "text": "Infrastructure As A Service (IAAS), Platform As A Service (PAAS) and Software As A Service (SAAS)",
            "correct": true
        }
    ]
});

db.questions.insert({
    "tags": ["azure"],
    "difficulty": 1,
    "type": "MULTIPLE_CHOICE",
    "question": "Among the main types or categories of cloud, what all does Azure provide you?",
    "answers": [
        {
            "_id": ObjectId(),
            "text": "Infrastructure As A Service (IAAS) and Software As A Service (SAAS)",
            "correct": false
        },
        {
            "_id": ObjectId(),
            "text": "Infrastructure As A Service (IAAS) and Platform As A Service (PAAS)",
            "correct": true
        },
        {"_id": ObjectId(), "text": "Platform As A Service (PAAS) and Software As A Service (SAAS)", "correct": false}
    ]
});

db.questions.insert({
    "tags": ["cloud"],
    "difficulty": 1,
    "type": "MULTIPLE_CHOICE",
    "question": "What is elasticity?",
    "answers": [
        {
            "_id": ObjectId(),
            "text": "A characteristic of cloud computing through which increasing workload can be handled by " +
            "increasing in proportion the amount of resource capacity. It allows the architecture to provide on-demand " +
            "resources if the requirement is being raised by the traffic.",
            "correct": true
        },
        {
            "_id": ObjectId(),
            "text": "The concept of commissioning and decommissioning of large amount of resource capacity dynamically. " +
            "It is measured by the speed by which the resources are coming on demand and the usage of the resources.",
            "correct": false
        }
    ]
});

db.questions.insert({
    "tags": ["cloud"],
    "difficulty": 1,
    "type": "MULTIPLE_CHOICE",
    "question": "What is scalability?",
    "answers": [
        {
            "_id": ObjectId(),
            "text": "A characteristic of cloud computing through which increasing workload can be handled by " +
            "increasing in proportion the amount of resource capacity. It allows the architecture to provide on-demand " +
            "resources if the requirement is being raised by the traffic.",
            "correct": false
        },
        {
            "_id": ObjectId(),
            "text": "The concept of commissioning and decommissioning of large amount of resource capacity dynamically. " +
            "It is measured by the speed by which the resources are coming on demand and the usage of the resources.",
            "correct": true
        }
    ]
});

