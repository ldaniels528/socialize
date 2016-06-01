db.jobs.drop();

db.jobs.insert({
    title: "Sr. Java Developer",
    description: "Responsible for arriving at application architecture, interface and service design and overall " +
    "detailed design of the core Telematics system features. Evaluate business and system requirements; " +
    "ensure technical feasibility, estimate development effort and implementation sequence, provide " +
    "technical guidance to the engineers. Support and lead development efforts, performance engineering " +
    "and test automation. ",
    locations: [{city: "Fountain Valley", state: "CA"}],
    source: "Dice.com",
    skills: ["Java", "J2EE", "Oracle", "SDLC"],
    tags: ["Java", "J2EE", "Oracle", "SDLC"],
    active: true,
    creationTime: new Date()
});