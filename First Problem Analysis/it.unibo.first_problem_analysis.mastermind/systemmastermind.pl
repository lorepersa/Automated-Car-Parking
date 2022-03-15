%====================================================================================
% systemmastermind description   
%====================================================================================
context(ctxmastermind, "localhost",  "TCP", "8070").
context(ctxbusinesslogic, "127.0.0.1",  "TCP", "8065").
context(ctxweightsensor, "127.0.0.1",  "TCP", "8060").
context(ctxoutsonar, "127.0.0.1",  "TCP", "8061").
context(ctxthermometer, "127.0.0.1",  "TCP", "8062").
 qactor( businesslogic, ctxbusinesslogic, "external").
  qactor( weightsensor, ctxweightsensor, "external").
  qactor( outsonar, ctxoutsonar, "external").
  qactor( thermometer, ctxthermometer, "external").
  qactor( parkservicegui, ctxmastermind, "it.unibo.parkservicegui.Parkservicegui").
  qactor( parkservicestatusgui, ctxmastermind, "it.unibo.parkservicestatusgui.Parkservicestatusgui").
  qactor( mastermind, ctxmastermind, "it.unibo.mastermind.Mastermind").
