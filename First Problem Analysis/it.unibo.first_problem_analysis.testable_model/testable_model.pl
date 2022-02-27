%====================================================================================
% testable_model description   
%====================================================================================
context(ctxtestablemodel, "localhost",  "TCP", "8053").
 qactor( physicalweightsensor, ctxtestablemodel, "it.unibo.physicalweightsensor.Physicalweightsensor").
  qactor( logicalweightsensor, ctxtestablemodel, "it.unibo.logicalweightsensor.Logicalweightsensor").
  qactor( physicalsonar, ctxtestablemodel, "it.unibo.physicalsonar.Physicalsonar").
  qactor( logicalsonar, ctxtestablemodel, "it.unibo.logicalsonar.Logicalsonar").
  qactor( physicalthermometer, ctxtestablemodel, "it.unibo.physicalthermometer.Physicalthermometer").
  qactor( logicalthermometer, ctxtestablemodel, "it.unibo.logicalthermometer.Logicalthermometer").
  qactor( logicalfan, ctxtestablemodel, "it.unibo.logicalfan.Logicalfan").
  qactor( logicaltransporttrolley, ctxtestablemodel, "it.unibo.logicaltransporttrolley.Logicaltransporttrolley").
  qactor( businesslogic, ctxtestablemodel, "it.unibo.businesslogic.Businesslogic").
  qactor( parkservicegui, ctxtestablemodel, "it.unibo.parkservicegui.Parkservicegui").
  qactor( parkservicestatusgui, ctxtestablemodel, "it.unibo.parkservicestatusgui.Parkservicestatusgui").
