#!/bin/bash

cd it.unibo.automatedcarparking.weightsensor
gradle build
cd ../it.unibo.automatedcarparking.outsonar
gradle build
cd ../it.unibo.automatedcarparking.thermometer
gradle build
cd ../it.unibo.automatedcarparking.fan
gradle build
cd ../it.unibo.automatedcarparking.businesslogic
gradle build
