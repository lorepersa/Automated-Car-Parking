#!/bin/bash

cd itunibo.qakutils
gradle build
cp build/libs/itunibo.qakutils-1.0.jar ../jars/
cp build/libs/itunibo.qakutils-1.0.jar ../it.unibo.sprint1.gui/jars/
cd ../itunibo.automatedcarparking.dsl
gradle build
cp build/libs/itunibo.automatedcarparking.dsl-1.0.jar ../jars/
cp build/libs/itunibo.automatedcarparking.dsl-1.0.jar ../it.unibo.sprint1.gui/jars/
cd ../it.unibo.sprint1.project.transporttrolley
gradle build
