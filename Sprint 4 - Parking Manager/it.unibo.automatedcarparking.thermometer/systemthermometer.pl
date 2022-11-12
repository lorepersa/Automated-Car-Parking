%====================================================================================
% systemthermometer description   
%====================================================================================
context(ctxthermometer, "localhost",  "TCP", "8062").
 qactor( thermometer, ctxthermometer, "it.unibo.thermometer.Thermometer").
