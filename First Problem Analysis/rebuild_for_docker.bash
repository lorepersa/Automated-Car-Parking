#!/bin/bash

cd it.unibo.qakobserver
gradle build
cp build/libs/it.unibo.qakobserver-1.0.jar ../jars/
cd ../it.unibo.first_problem_analysis.weightsensor
gradle -b build_ctxweightsensor.gradle build
cd ../it.unibo.first_problem_analysis.outsonar
gradle -b build_ctxoutsonar.gradle build
cd ../it.unibo.first_problem_analysis.thermometer
gradle -b build_ctxthermometer.gradle build
cd ../it.unibo.first_problem_analysis.fan
gradle -b build_ctxfan.gradle build
cd ../it.unibo.first_problem_analysis.businesslogic
gradle -b build_ctxbusinesslogic.gradle build
