db.exams.insert({
    "name": "Advanced Java Exam",
    "description": "An Exam for Advanced Java Developers",
    "likedBy": ["5633c756d9d5baa77a714803"],
    "likes": 1,
    "submitter": {
        "avatarURL": "https://media.licdn.com/mpr/mpr/shrinknp_400_400/p/3/000/0a6/0bc/2a4b0ba.jpg",
        "name": "Lawrence Daniels, MBA",
        "_id": ObjectId("5633c756d9d5baa77a714803")
    },
    "questions": [{
        "tags": ["java"],
        "difficulty": NumberInt(3),
        "type": "MULTIPLE_CHOICE",
        "question": "What is the result?",
        "code": "public class Sequence { \n" +
        "   Sequence() { System.out.print(\"c \"); } \n\n" +
        "   { System.out.print(\"y \"); } \n\n" +
        "   public static void main(String[] args) {\n" +
        "    new Sequence().go();\n" +
        "   }\n\n" +
        "   void go() { System.out.print(\"g \"); }\n" +
        "   static { System.out.print(\"x \"); }\n" +
        "} ",
        "answers": [
            {"_id": ObjectId(), "text": "c x y g", "correct": false},
            {"_id": ObjectId(), "text": "c g x y", "correct": false},
            {"_id": ObjectId(), "text": "x c y g", "correct": false},
            {"_id": ObjectId(), "text": "x y c g", "correct": true},
            {"_id": ObjectId(), "text": "y x c g", "correct": false},
            {"_id": ObjectId(), "text": "y c g x", "correct": false}]
    }, {
        "tags": ["java"],
        "difficulty": NumberInt(3),
        "type": "MULTIPLE_CHOICE",
        "question": "What is the result?",
        "code": "public class MyStuff { \n" +
        "   MyStuff(String n) { name = n; } \n\n" +
        "   String name; \n\n" +
        "   public static void main(String[] args) { \n" +
        "       MyStuff m1 = new MyStuff(\"guitar\"); \n" +
        "       MyStuff m2 = new MyStuff(\"tv\"); \n" +
        "       System.out.println(m2.equals(m1)); \n" +
        "   }\n\n" +
        "   public boolean equals(Object o) { \n" +
        "       MyStuff m = (MyStuff) o; \n" +
        "       if(m.name != null) \n" +
        "           return true; \n" +
        "       return false; \n" +
        "   }\n" +
        "}",
        "answers": [
            {
                "_id": ObjectId(),
                "text": "The output is \"true\" and MyStuff fulfills the Object.equals() contract.",
                "correct": false
            },
            {
                "_id": ObjectId(),
                "text": "The output is \"false\" and MyStuff fulfills the Object.equals() contract.",
                "correct": false
            },
            {
                "_id": ObjectId(),
                "text": "The output is \"true\" and MyStuff does NOT fulfill the Object.equals() contract.",
                "correct": true
            },
            {
                "_id": ObjectId(),
                "text": "The output is \"false\" and MyStuff does NOT fulfill the Object.equals() contract",
                "correct": false
            },
            {"_id": ObjectId(), "text": "Compilation fails", "correct": false}
        ]
    }, {
        "tags": ["java"],
        "difficulty": NumberInt(3),
        "type": "MULTIPLE_CHOICE",
        "question": "What is the result?",
        "code": "import java.util.*;\n\n" +
        "public class Primes {\n\n" +
        "   public static void main(String[] args) {\n" +
        "       List p = new ArrayList();\n" +
        "       p.add(7);\n" +
        "       p.add(2);\n" +
        "       p.add(5);\n" +
        "       p.add(2);\n" +
        "       p.sort();\n" +
        "       System.out.println(p);\n" +
        "   }\n" +
        "}",
        "answers": [
            {"_id": ObjectId(), "text": "[2, 5, 7]", "correct": false},
            {"_id": ObjectId(), "text": "[2, 2, 5, 7]", "correct": false},
            {"_id": ObjectId(), "text": "[7, 2, 5, 2]", "correct": false},
            {"_id": ObjectId(), "text": "[7, 5, 2, 2]", "correct": false},
            {"_id": ObjectId(), "text": "Compilation fails", "correct": true}
        ]
    }, {
        "tags": ["java"],
        "difficulty": NumberInt(3),
        "type": "MULTIPLE_CHOICE",
        "question": "What is the result?",
        "code": "public class MyLoop {\n\n" +
        "   public static void main(String[] args) {\n" +
        "       String[] sa = {\"tom \", \"jerry \"};\n" +
        "       for(int x = 0; x < 3; x++) {\n" +
        "           for(String s: sa) {\n" +
        "               System.out.print(x + \" \" + s);\n" +
        "               if( x == 1) break;\n" +
        "           }\n" +
        "       }\n" +
        "   }\n" +
        "}\n",
        "answers": [
            {"_id": ObjectId(), "text": "0 tom 0 jerry 1 tom", "correct": false},
            {"_id": ObjectId(), "text": "0 tom 0 jerry 1 tom 1 jerry", "correct": false},
            {"_id": ObjectId(), "text": "0 tom 0 jerry 2 tom 2 jerry", "correct": false},
            {"_id": ObjectId(), "text": "0 tom 0 jerry 1 tom 2 tom 2 jerry", "correct": true},
            {"_id": ObjectId(), "text": "0 tom 0 jerry 1 tom 1 jerry 2 tom 2 jerry", "correct": false},
            {"_id": ObjectId(), "text": "Compilation fails.", "correct": false}
        ]
    }, {
        "tags": ["java"],
        "difficulty": NumberInt(3),
        "type": "MULTIPLE_CHOICE",
        "question": "What is the result?",
        "code": "interface Rideable {\n" +
        "   String getGait();\n" +
        "}\n\n" +
        "public class Camel implements Rideable {\n" +
        "   int weight = 2;\n\n" +
        "   public static void main(String[] args) {\n" +
        "       new Camel().go(8);\n" +
        "   }\n\n" +
        "   void go(int speed) {\n" +
        "       ++speed;\n" +
        "       weight++;\n" +
        "       int walkrate = speed * weight;\n" +
        "       System.out.print(walkrate + getGait());\n" +
        "   }\n\n" +
        "   String getGait() {\n" +
        "       return \" mph, lope\";\n" +
        "   }\n" +
        "}",
        "answers": [
            {"_id": ObjectId(), "text": "16 mph, lope", "correct": false},
            {"_id": ObjectId(), "text": "18 mph, lope", "correct": false},
            {"_id": ObjectId(), "text": "24 mph, lope", "correct": false},
            {"_id": ObjectId(), "text": "27 mph, lope", "correct": false},
            {"_id": ObjectId(), "text": "Compilation fails.", "correct": true},
            {"_id": ObjectId(), "text": "An exception is thrown at run time.", "correct": false}
        ]
    }, {
        "tags": ["java"],
        "difficulty": NumberInt(3),
        "type": "MULTIPLE_CHOICE",
        "question": "What is the result?",
        "code": "class Alpha {\n" +
        "   String getType() { return \"alpha\"; }\n" +
        "}\n\n" +
        "class Beta extends Alpha {\n" +
        "   String getType() { return \"beta\"; }\n" +
        "}\n\n" +
        "class Gamma extends Beta {\n" +
        "   String getType() { return \"gamma\"; }\n\n" +
        "   public static void main(String[] args) {\n" +
        "       Gamma g1 = new Alpha();\n" +
        "       Gamma g2 = new Beta();\n" +
        "       System.out.println(g1.getType() + \" \" + g2.getType());\n" +
        "   }\n" +
        "}",
        "answers": [
            {"_id": ObjectId(), "text": "alpha beta", "correct": false},
            {"_id": ObjectId(), "text": "beta beta", "correct": false},
            {"_id": ObjectId(), "text": "gamma gamma", "correct": false},
            {"_id": ObjectId(), "text": "alpha alpha", "correct": false},
            {"_id": ObjectId(), "text": "Compilation fails.", "correct": true}
        ]
    }, {
        "tags": ["java"],
        "difficulty": NumberInt(3),
        "type": "MULTIPLE_CHOICE",
        "question": "What is the result?",
        "code": "class Feline {\n" +
        "   public String type = \"f \";\n\n" +
        "   public Feline() {\n" +
        "       System.out.print(\"feline \");\n" +
        "   }\n" +
        "}\n\n" +
        "public class Cougar extends Feline {\n\n" +
        "   public Cougar() {\n" +
        "       System.out.print(\"cougar \");\n" +
        "   }\n\n" +
        "   public static void main(String[] args) {\n" +
        "       new Cougar().go();\n" +
        "   }\n\n" +
        "   void go() {\n" +
        "       type = \"c \";\n" +
        "       System.out.print(this.type + super.type);\n" +
        "   }\n" +
        "}",
        "answers": [
            {"_id": ObjectId(), "text": "cougar c c", "correct": false},
            {"_id": ObjectId(), "text": "cougar c f", "correct": false},
            {"_id": ObjectId(), "text": "feline cougar c c", "correct": true},
            {"_id": ObjectId(), "text": "feline cougar c f", "correct": false},
            {"_id": ObjectId(), "text": "Compilation fails", "correct": false},
            {"_id": ObjectId(), "text": "An exception is thrown at run time.", "correct": false}
        ]
    }]
});


