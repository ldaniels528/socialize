db.study_questions.drop();

db.study_questions.insert({
    "tags": ["oop"],
    "difficulty": 1,
    "type":"STUDY",
    "question" : "What is a class?",
    "answers" : [
        "A class describes all the attributes of objects, as well as the methods that implement the behavior of member " +
        "objects. It is a comprehensive data type, which represents a blue print of objects. It is a template of object. ",
        "A class can be defined as the primary building block of OOP. It also serves as a template that describes the " +
        "properties, state, and behaviors common to a particular group of objects.",
        "A class contains data and behavior of an entity. For example, the aircraft class can contain data, such as " +
        "model number, category, and color and behavior, such as duration of flight, speed, and number of passengers. " +
        "A class inherits the data members and behaviors of other classes by extending from them."
    ]
});

db.study_questions.insert({
    "tags": ["bigdata"],
    "difficulty": 1,
    "type":"TEST",
    "question" : "What is a Data Lake?",
    "answers" : [
        "A data lake is a storage repository that holds a vast amount of raw data in its native format until it is needed."
    ]
});

db.study_questions.insert({
    "tags": ["bigdata"],
    "difficulty": 1,
    "type":"TEST",
    "question" : "What is a Hadoop?",
    "answers" : [
        "Hadoop is an open-source software framework for storing data and running applications on clusters of commodity hardware. It provides massive storage for any kind of data, enormous processing power and the ability to handle virtually limitless concurrent tasks or jobs."
    ]
});

db.study_questions.insert({
    "tags": ["oop"],
    "difficulty": 1,
    "type":"STUDY",
    "question" : "What is an object?",
    "answers" : [
        "They are instance of classes. It is a basic unit of a system. An object is an entity that has attributes, " +
        "behavior, and identity. Attributes and behavior of an object are defined by the class definition."
    ]
});

db.study_questions.insert({
    "tags": ["cloud"],
    "difficulty": 1,
    "type":"STUDY",
    "question" : "What is cloud computing?",
    "answers" : [
        "The technology in which computer resources are provided as a service over internet to end users is termed as " +
        "cloud computing. This can be core, memory, storage, networking or even software such as database, operating system etc."
    ]
});

db.study_questions.insert({
    "tags": ["cloud"],
    "difficulty": 1,
    "type":"STUDY",
    "question" : "What is Internet of Things (IoT)?",
    "answers" : [
        "The Internet of Things (IoT) is an environment in which objects, animals or people are provided with unique " +
        "identifiers and the ability to transfer data over a network without requiring human-to-human or " +
        "human-to-computer interaction."
    ]
});

db.study_questions.insert({
    "tags": ["cloud"],
    "difficulty": 1,
    "type":"STUDY",
    "question" : "What resources are provided by infrastructure as a service?",
    "answers" : [
        "Infrastructure as a Service provides physical and virtual resources that are used to build a cloud. " +
        "Infrastructure deals with the complexities of maintaining and deploying of the services provided by this layer. " +
        "The infrastructure here is the servers, storage and other hardware systems."
    ]
});

db.study_questions.insert({
    "tags": ["cloud"],
    "difficulty": 1,
    "type":"STUDY",
    "question" : "How important is platform as a service?",
    "answers" : [
        "Platform as a Service is an important layer in cloud architecture. It is built on the infrastructure model, " +
        "which provides resources like computers, storage and network. This layer includes organizing and operate the " +
        "resources provided by the below layer. It is also responsible to provide complete virtualization of the " +
        "infrastructure layer to make it look like a single server and keep it hidden from the outside world."
    ]
});

db.study_questions.insert({
    "tags": ["cloud"],
    "difficulty": 1,
    "type":"STUDY",
    "question" : "What does software as a service provide?",
    "answers" : [
        "Software as a Service is another layer of cloud computing, which provides cloud applications like Google docs, " +
        "which enables the user to save their documents on the cloud and create as well. It provides the applications to " +
        "be created on fly without adding or installing any extra software component. It provides built-in software to " +
        "create wide varieties of applications and documents and share it with other people online."
    ]
});

db.study_questions.insert({
    "tags": ["cloud"],
    "difficulty": 1,
    "type":"STUDY",
    "question" : "What are the benefits of cloud?",
    "answers" : [
        "Pay for what you use – Pay for those services that you’ve used and not for everything. " +
        "Pay per hour is the type that almost all cloud providers use, Azure plans to come up with " +
        "Pay per minute option soon.",
        "Highly scalable – Create even 1000 servers within minutes.",
        "No Cap-Ex – Eliminates capital expenditure, only requires operational expenditure.",
        "Quick DR – Disaster Recovery can be very fast at low cost."
    ]
});

db.study_questions.insert({
    "tags": ["azure"],
    "difficulty": 1,
    "type":"STUDY",
    "question" : "What is the difference between Azure VM and Azure Instance?",
    "answers" : [
        "Azure VM provides you with the entire virtual machine, where you can decide the OS, " +
        "patches or updates etc. It is just that your have control on your own machine, but it resides on MS datacenter. " +
        "Azure Instances comes within the cloud service, where you only worry about the application within it " +
        "and other tasks such as OS, patching/updating etc will be taken care by MS. Instances are more suited " +
        "if you’ve a web application."
    ]
});

db.study_questions.insert({
    "tags": ["azure"],
    "difficulty": 1,
    "type":"STUDY",
    "question" : "What are the role types of Azure cloud service instances?",
    "answers" : [
        "Two roles are:",
        "Web role – where the front end code of your application will reside. It has IIS running inside it.",
        "Worker role – where the core code (for your service) runs. It doesn’t have IIS."
    ]
});

db.study_questions.insert({
    "tags": ["azure", "teamcity"],
    "difficulty": 1,
    "type":"STUDY",
    "question" : "How the application is moved to Azure cloud service?",
    "answers" : [
        "Three main file types used are:",
        ".csdef – cs definition file defining service typels as well as the number of roles",
        ".cscfg – cs configuration file defining configuration settings as well as the number of role instances",
        ".cspkg – cs package file, containing the application code as well as the csdef file",
        "The application can be pushed to Azure manually (by clicking upload button in cloud services page in Azure portal) or can be done using some continuous integration tool like TeamCity."
    ]
});

db.study_questions.insert({
    "tags": ["azure"],
    "difficulty": 1,
    "type":"STUDY",
    "question" : "What is a storage? What are the types in Azure?",
    "answers" : [
        "Storage normally means some space where the user can store data. Basically almost all cloud services provides " +
        "you with 2 different classes of storage, one which termed as storage itself another with the name database.",
        "Storage normally would be an object based storage where the user can store image, video or any such contents. " +
        "Databases would be the area to store the data in table like structure.",
        "In Azure, storage is again divided into 4 as:",
        "Blob – For storing unstructured data such as documents or media files.",
        "Queues – Messaging store for workflow processing.",
        "Tables – For structured no-sql based data.",
        "Files – New service, shared storage for apps using the SMB protocol."
    ]
});

db.study_questions.insert({
    "tags": ["azure"],
    "difficulty": 1,
    "type":"STUDY",
    "question" : "How can I add extra hard disk space in my Azure VM?",
    "answers" : [
        "By default using Azure management portal you can’t do this. There are some third party tools like CloudXplorer, " +
        "which will help you to extend your hard disk space. Or yes, if your hard disk is small enough (in used space) " +
        "and if you’ve a good internet connection, you can get the hard disk downloaded, extend using Hyper-V manager " +
        "snap in and upload it to Azure."
    ]
});

db.study_questions.insert({
    "tags": ["azure"],
    "difficulty": 1,
    "type":"STUDY",
    "question" : "What are Blobs, Tables, Queues and Files in Azure?",
    "answers" : [
        "Blob – For storing unstructured data such as documents or media files.",
        "Queues – Messaging store for workflow processing.",
        "Tables – For structured no-sql based data.",
        "Files – New service, shared storage for apps using the SMB protocol."
    ]
});

db.study_questions.insert({
    "tags": ["azure"],
    "difficulty": 1,
    "type":"STUDY",
    "question" : "How does Azure pricing differ for Azure VM hard disk and Azure Blob Storage?",
    "answers" : [
        "Azure VM hard disk is a .vhd file backed by a Page Blobs. You are allowed to create a hard disk " +
        "of size 1 TB maximum (as of 14-May-2015), but MS costs you for how much data stored in that page blob. " +
        "Which means even if you’ve 1 TB page blob and you store just 2 GB of data, then you’re charged " +
        "for 2 GB rather than for 1 TB. Azure blob storage uses Block Blobs. The maximum size of a block blob " +
        "is 200 GB (as of 14-May-2015). Even if you store just 2 GB of data in your 5 GB allocated space, " +
        "you’re charged for the entire 5 GB."
    ]
});

db.study_questions.insert({
    "tags": ["cloud"],
    "difficulty": 1,
    "type":"STUDY",
    "question" : "What is the difference between scalability and elasticity?",
    "answers" : [
        "Scalability is a characteristic of cloud computing through which increasing workload can be handled by " +
        "increasing in proportion the amount of resource capacity. It allows the architecture to provide on-demand " +
        "resources if the requirement is being raised by the traffic.",
        "Elasticity is the characteristic of providing the concept of commissioning and decommissioning of " +
        "large amount of resource capacity dynamically. It is measured by the speed by which the resources are coming " +
        "on demand and the usage of the resources."
    ]
});

db.study_questions.insert({
    "tags": ["cloud"],
    "difficulty": 1,
    "type":"STUDY",
    "question" : "How does cloud computing provides on-demand functionality?",
    "answers" : [
        "It provides on-demand access to virtualized IT resources that can be shared by others or subscribed by you. " +
        "It provides an easy way to provide configurable resources by taking it from a shared pool. The pool consists " +
        "of networks, servers, storage, applications and services."
    ]
});



