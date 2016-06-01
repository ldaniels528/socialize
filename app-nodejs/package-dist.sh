#!/bin/bash
#
# This script builds and packages an application distributable.
#

rm -rf socialized.js
mkdir socialized.js
cp ./target/scala-2.11/socialized-nodejs-fastopt.* ./socialized.js/
cp -r bower.json package.json server.js public notes.txt ./socialized.js/
zip socialized.zip -r socialized.js
#rm -rf socialized.js