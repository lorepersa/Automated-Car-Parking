#!/bin/bash

cd itunibo.qakutils
gradle build
cp build/libs/itunibo.qakutils-1.0.jar ../jars/
cd ../it.unibo.sprint1.project.transporttrolley
gradle build
