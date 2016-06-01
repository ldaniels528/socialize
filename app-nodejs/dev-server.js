/**
 * Socialized ScalaJS Bootstrap
 * @author: lawrence.daniels@gmail.com
 */
(function () {
    require("./target/scala-2.11/socialized-nodejs-fastopt.js");
    const facade = com.socialized.javascript.SocializedJSServer();
    facade.startServer({
        "__dirname": __dirname,
        "__filename": __filename,
        "exports": exports,
        "module": module,
        "require": require
    });
})();