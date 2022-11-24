%====================================================================================
% systembusinesslogic description   
%====================================================================================
context(ctxbusinesslogic, "localhost",  "TCP", "8065").
context(ctxweightsensor, "weightsensor.iss",  "TCP", "8060").
context(ctxoutsonar, "outsonar.iss",  "TCP", "8061").
context(ctxthermometer, "thermometer.iss",  "TCP", "8062").
context(ctxfan, "fan.iss",  "TCP", "8063").
context(ctxbasicrobot, "basicrobot.iss",  "TCP", "8020").
 qactor( weightsensor, ctxweightsensor, "external").
  qactor( outsonar, ctxoutsonar, "external").
  qactor( thermometer, ctxthermometer, "external").
  qactor( fan, ctxfan, "external").
  qactor( basicrobot, ctxbasicrobot, "external").
  qactor( indoorcontroller, ctxbusinesslogic, "it.unibo.indoorcontroller.Indoorcontroller").
  qactor( outdoorcontroller, ctxbusinesslogic, "it.unibo.outdoorcontroller.Outdoorcontroller").
  qactor( parkservicecontroller, ctxbusinesslogic, "it.unibo.parkservicecontroller.Parkservicecontroller").
  qactor( parkingslotscontroller, ctxbusinesslogic, "it.unibo.parkingslotscontroller.Parkingslotscontroller").
  qactor( thermometerfilter, ctxbusinesslogic, "it.unibo.thermometerfilter.Thermometerfilter").
  qactor( fancontroller, ctxbusinesslogic, "it.unibo.fancontroller.Fancontroller").
  qactor( transporttrolley, ctxbusinesslogic, "it.unibo.transporttrolley.Transporttrolley").
  qactor( statusaggregator, ctxbusinesslogic, "it.unibo.statusaggregator.Statusaggregator").
  qactor( managercontroller, ctxbusinesslogic, "it.unibo.managercontroller.Managercontroller").
