%====================================================================================
% systemoutsonar description   
%====================================================================================
context(ctxoutsonar, "localhost",  "TCP", "8061").
 qactor( outsonar, ctxoutsonar, "it.unibo.outsonar.Outsonar").
