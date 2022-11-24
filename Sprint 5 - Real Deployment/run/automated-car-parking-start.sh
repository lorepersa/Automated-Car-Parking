#!/bin/bash

echo "start basicrobot ..."
ssh iss@basicrobot.iss "docker-compose -f basicrobotVirtual.yaml up -d"
sleep 3
echo ""
echo "start weightsensor ..."
ssh pi@weightsensor.iss "docker-compose -f weightsensor.yaml up -d"
sleep 3
echo ""
echo "start outsonar ..."
ssh pi@outsonar.iss -p 2298 "docker-compose -f outsonar.yaml up -d"
sleep 3
echo ""
echo "start thermometer ..."
ssh pi@thermometer.iss "docker-compose -f thermometer.yaml up -d"
sleep 3
echo ""
echo "start fan ..."
ssh pi@fan.iss -p 2298 "docker-compose -f fan.yaml up -d"
sleep 3
echo ""
echo "start businesslogic ..."
ssh iss@businesslogic.iss "docker-compose -f businesslogic.yaml up -d"
sleep 3
echo ""
echo "start parkservicegui ..."
docker-compose -f parkservicegui.yaml up -d
sleep 3
echo ""
echo "start parkservicestatusgui ..."
docker-compose -f parkservicestatusgui.yaml up -d
sleep 3
echo ""
echo "start mockdatagui ..."
docker-compose -f mockdatagui.yaml up -d
sleep 3
echo ""
echo "done"
