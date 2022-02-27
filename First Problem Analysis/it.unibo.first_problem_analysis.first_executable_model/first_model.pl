%====================================================================================
% first_model description   
%====================================================================================
context(ctxfirstmodel, "localhost",  "TCP", "8053").
 qactor( physicalweightsensor, ctxfirstmodel, "it.unibo.physicalweightsensor.Physicalweightsensor").
  qactor( logicalweightsensor, ctxfirstmodel, "it.unibo.logicalweightsensor.Logicalweightsensor").
  qactor( physicalsonar, ctxfirstmodel, "it.unibo.physicalsonar.Physicalsonar").
  qactor( logicalsonar, ctxfirstmodel, "it.unibo.logicalsonar.Logicalsonar").
  qactor( physicalthermometer, ctxfirstmodel, "it.unibo.physicalthermometer.Physicalthermometer").
  qactor( logicalthermometer, ctxfirstmodel, "it.unibo.logicalthermometer.Logicalthermometer").
  qactor( physicalfan, ctxfirstmodel, "it.unibo.physicalfan.Physicalfan").
  qactor( logicalfan, ctxfirstmodel, "it.unibo.logicalfan.Logicalfan").
  qactor( logicaltransporttrolley, ctxfirstmodel, "it.unibo.logicaltransporttrolley.Logicaltransporttrolley").
  qactor( businesslogic, ctxfirstmodel, "it.unibo.businesslogic.Businesslogic").
  qactor( parkservicegui, ctxfirstmodel, "it.unibo.parkservicegui.Parkservicegui").
  qactor( parkservicestatusgui, ctxfirstmodel, "it.unibo.parkservicestatusgui.Parkservicestatusgui").
  qactor( mastermind, ctxfirstmodel, "it.unibo.mastermind.Mastermind").
