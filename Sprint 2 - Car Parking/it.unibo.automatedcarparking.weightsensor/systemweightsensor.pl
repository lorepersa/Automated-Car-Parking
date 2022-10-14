%====================================================================================
% systemweightsensor description   
%====================================================================================
context(ctxweightsensor, "localhost",  "TCP", "8060").
 qactor( weightsensor, ctxweightsensor, "it.unibo.weightsensor.Weightsensor").
