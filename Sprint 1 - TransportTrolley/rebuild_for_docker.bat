
cd itunibo.qakutils
call gradle build
copy /Y build\libs\itunibo.qakutils-1.0.jar ..\jars\
cd ..\it.unibo.sprint1.project.transporttrolley
call gradle build