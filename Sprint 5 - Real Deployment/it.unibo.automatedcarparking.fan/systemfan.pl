%====================================================================================
% systemfan description   
%====================================================================================
context(ctxfan, "localhost",  "TCP", "8063").
 qactor( fan, ctxfan, "it.unibo.fan.Fan").
