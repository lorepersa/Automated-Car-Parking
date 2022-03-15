
cd it.unibo.qakobserver
call gradle build
copy /Y build\libs\it.unibo.qakobserver-1.0.jar ..\jars\
cd ..\it.unibo.first_problem_analysis.weightsensor
call gradle -b build_ctxweightsensor.gradle build
cd ..\it.unibo.first_problem_analysis.outsonar
call gradle -b build_ctxoutsonar.gradle build
cd ..\it.unibo.first_problem_analysis.thermometer
call gradle -b build_ctxthermometer.gradle build
cd ..\it.unibo.first_problem_analysis.fan
call gradle -b build_ctxfan.gradle build
cd ..\it.unibo.first_problem_analysis.businesslogic
call gradle -b build_ctxbusinesslogic.gradle build