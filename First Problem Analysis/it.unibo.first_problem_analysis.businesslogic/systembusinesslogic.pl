%====================================================================================
% systembusinesslogic description   
%====================================================================================
context(ctxbusinesslogic, "localhost",  "TCP", "8065").
context(ctxweightsensor, "weightsensor",  "TCP", "8060").
context(ctxoutsonar, "outsonar",  "TCP", "8061").
context(ctxthermometer, "thermometer",  "TCP", "8062").
context(ctxfan, "fan",  "TCP", "8063").
 qactor( weightsensor, ctxweightsensor, "external").
  qactor( outsonar, ctxoutsonar, "external").
  qactor( thermometer, ctxthermometer, "external").
  qactor( fan, ctxfan, "external").
  qactor( businesslogic, ctxbusinesslogic, "it.unibo.businesslogic.Businesslogic").
  qactor( dtoccupiedwatchdog, ctxbusinesslogic, "it.unibo.dtoccupiedwatchdog.Dtoccupiedwatchdog").
  qactor( dtfreewatchdog, ctxbusinesslogic, "it.unibo.dtfreewatchdog.Dtfreewatchdog").
