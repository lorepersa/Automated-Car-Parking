%====================================================================================
% systembusinesslogic description   
%====================================================================================
context(ctxbusinesslogic, "localhost",  "TCP", "8065").
context(ctxbasicrobot, "robot",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( transporttrolley, ctxbusinesslogic, "it.unibo.transporttrolley.Transporttrolley").
