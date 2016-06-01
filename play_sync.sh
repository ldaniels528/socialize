#!/bin/bash
#
# This script copies the generated JavaScript files from the app-nodejs project into app-play.
#
cp app-nodejs/public/javascripts/* app-play/public/javascripts/
ls -al app-play/public/javascripts/
