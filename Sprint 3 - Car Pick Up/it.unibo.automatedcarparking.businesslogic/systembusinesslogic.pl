%====================================================================================
% systembusinesslogic description   
%====================================================================================
context(ctxbusinesslogic, "localhost",  "TCP", "8065").
context(ctxweightsensor, "weightsensor",  "TCP", "8060").
context(ctxoutsonar, "outsonar",  "TCP", "8061").
context(ctxbasicrobot, "robot",  "TCP", "8020").
 qactor( weightsensor, ctxweightsensor, "external").
  qactor( outsonar, ctxoutsonar, "external").
  qactor( basicrobot, ctxbasicrobot, "external").
  qactor( indoorcontroller, ctxbusinesslogic, "it.unibo.indoorcontroller.Indoorcontroller").
  qactor( outdoorcontroller, ctxbusinesslogic, "it.unibo.outdoorcontroller.Outdoorcontroller").
  qactor( parkservicecontroller, ctxbusinesslogic, "it.unibo.parkservicecontroller.Parkservicecontroller").
  qactor( parkingslotscontroller, ctxbusinesslogic, "it.unibo.parkingslotscontroller.Parkingslotscontroller").
  qactor( transporttrolley, ctxbusinesslogic, "it.unibo.transporttrolley.Transporttrolley").
