# Socialized

_Socialized_ is an example application, which has been develop to demonstrate two distinct features of [MEANS.js](https://github.com/ldaniels528/MEANS.js):
* Using MEANS.js to build a Play application (Scala.js + AngularJS front-end, Play and Scala RESTful backend).
* Using MEANS.js to build a MEAN application (Scala.js + AngularJS front-end, Node and Scala.js RESTful backend). 

## Motivations

Learning a new framework or API can be difficult; however, the learning curve can be greatly reduced when one is provided
with good examples. I figured what could be better than a full-fledged application as an example! _Socialized_ also has 
been a great test bed for me in terms of ensuring that most of the features offered by the facade work as advertised.

## Building the code

<a name="Build_Requirements"></a>
#### Build Requirements

* [Scala 2.11.8+] (http://scala-lang.org/download/)
* [Scala.js 0.6.8] (http://www.scala-js.org/)
* [SBT 0.13.11+] (http://www.scala-sbt.org/download.html)

#### Building the MEANS.js + Play application distributable (AngularJS frontend, Play backend)

To build the MEANS.js + Play application (distibutable .ZIP) do the following:

```bash
$ sbt "project playapp" dist
```

#### Running the MEANS.js + Play application (AngularJS frontend, Play backend)

Optionally, you could run the application directly from the sources:

```bash
$ sbt "project playapp" run
```

The above will startup the application on port 9000 by default.

#### Building the MEANS.js + Node application (AngularJS frontend, Node backend)

Prior to building the code, you need to install the bower and node modules. 
*NOTE*: You only need to perform this step once.

```bash
$ cd app-nodejs
$ npm install
$ bower install
$ cd ..
```

Now, you can compile the Scala.js sources to JavaScript by executing the following command:

```bash
$ sbt "project nodejs" fastOptJS
```

#### Running the MEANS.js + Node application (AngularJS frontend, Node backend)

```bash
$ cd ./app-node
$ node ./dev-server.js    
```

The above will startup the application on port 1337 by default. To listen/bind to a different port. Set the "port" environment
variable.

```bash
$ export port=8000
```

Then (re)start the application.

<img src="https://github.com/ldaniels528/socialized/blob/master/socialized.png">