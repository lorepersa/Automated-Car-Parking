
cd itunibo.qakutils
call gradle build
copy /Y build\libs\itunibo.qakutils-1.0.jar ..\jars\
copy /Y build\libs\itunibo.qakutils-1.0.jar ..\it.unibo.sprint1.gui\jars\
cd ..\itunibo.automatedcarparking.dsl
call gradle build
copy /Y build\libs\itunibo.automatedcarparking.dsl-1.0.jar ..\jars\
copy /Y build\libs\itunibo.automatedcarparking.dsl-1.0.jar ..\it.unibo.sprint1.gui\jars\
cd ..\it.unibo.sprint1.project.transporttrolley
call gradle build