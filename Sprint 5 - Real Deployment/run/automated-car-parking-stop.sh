#!/bin/bash

echo "stop mockdatagui ..."
docker-compose -f mockdatagui.yaml stop
sleep 3
echo ""
echo "stop parkservicestatusgui ..."
docker-compose -f parkservicestatusgui.yaml stop
sleep 3
echo ""
echo "stop parkservicegui ..."
docker-compose -f parkservicegui.yaml stop
sleep 3
echo ""
echo "stop businesslogic ..."
ssh iss@businesslogic.iss "docker-compose -f businesslogic.yaml stop"
sleep 3
echo ""
echo "stop basicrobot ..."
ssh iss@basicrobot.iss "docker-compose -f basicrobotVirtual.yaml stop"
sleep 3
echo ""
echo "stop weightsensor ..."
ssh pi@weightsensor.iss "docker-compose -f weightsensor.yaml stop"
sleep 3
echo ""
echo "stop outsonar ..."
ssh pi@outsonar.iss -p 2298 "docker-compose -f outsonar.yaml stop"
sleep 3
echo ""
echo "stop thermometer ..."
ssh pi@thermometer.iss "docker-compose -f thermometer.yaml stop"
sleep 3
echo ""
echo "stop fan ..."
ssh pi@fan.iss -p 2298 "docker-compose -f fan.yaml stop"
sleep 3
echo ""
echo "done"
