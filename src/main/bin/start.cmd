@echo off

set "MAIN_CLASS=net.kkolyan.elements.ElementsGame"

set "CP=.\lib\*;.\conf"

set "JVM_ARGS="
rem set "JVM_ARGS=%JVM_ARGS% -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
rem set "JVM_ARGS=%JVM_ARGS% -ea"
set "JVM_ARGS=%JVM_ARGS% -cp %CP%"
set "JVM_ARGS=%JVM_ARGS% -Djava.library.path=lib/windows"
set "JVM_ARGS=%JVM_ARGS% -Xms64m -Xmx256m"

set "COMMAND=start javaw %JVM_ARGS% %MAIN_CLASS%"
rem echo %COMMAND%
%COMMAND%
