echo off

rem set ENVIRONMENT
set arg1=%1
rem set VALIDATE, Ex: V and U for UPDATE
set arg2=%2
rem set MODULE to export
set arg3=%3

if "%arg1%"=="" goto :usage
if "%arg2%"=="" goto :usage
echo .

if "%arg2%"=="V" (
	REM Validate Liquibase script
	C:\Development\BLD-Trunk\SourceCode\Database\LiquiBase\liquibase --changeLogFile masterlog\db.changelog.master.xml --defaultsFile liquibase_%arg1%.properties --classpath C:\Development\BLD-Trunk\SourceCode\Database\LiquiBase\lib\ojdbc7.jar validate -Denv.name=%arg1%
) else (
	REM Add new changeset
	C:\Development\BLD-Trunk\SourceCode\Database\LiquiBase\liquibase --changeLogFile masterlog\db.changelog.master.xml --defaultsFile liquibase_%arg1%.properties --classpath C:\Development\BLD-Trunk\SourceCode\Database\LiquiBase\lib\ojdbc7.jar update -Denv.name=%arg1%
)
echo .
goto :eof

REM Clear checkSums
rem C:\Development\BLD-Trunk\SourceCode\Database\LiquiBase\liquibase --changeLogFile masterlog\db.changelog.master.xml --defaultsFile liquibase_%arg1%.properties --classpath C:\Development\BLD-Trunk\SourceCode\Database\LiquiBase\lib\ojdbc7.jar clearCheckSums

REM ..\liquiBase\liquibase --changeLogFile masterlog\db.changelog.master.xml --defaultsFile liquibase_dev.properties --classpath ..\liquimin\lib\ojdbc7.jar update

REM To reverse Engineer the AAM DB-schema to LiquiBase XML-file
REM ..\liquiBase\liquibase --changeLogFile changelog\db.changelog.AAM_ExportSchema.xml --defaultsFile liquibase_REV.properties --classpath ..\liquiBase\lib\ojdbc7.jar generateChangeLog
REM Exporting data
REM ..\liquiBase\liquibase --changeLogFile changelog\db.changelog.AAM_ExportData.xml --defaultsFile liquibase_REV.properties --classpath ..\liquiBase\lib\ojdbc7.jar --diffTypes="data" generateChangeLog

REM To reverse Engineer the MLCS DB-schema to LiquiBase XML-file
REM ..\liquiBase\liquibase --changeLogFile changelog\db.changelog.MLCS_ExportSchema.xml --defaultsFile liquibase_MLCS.properties --classpath ..\liquiBase\lib\ojdbc7.jar generateChangeLog

REM Exporting DATA
rem C:\Development\BLD-Trunk\SourceCode\Database\LiquiBase\liquibase --changeLogFile changelog\db.changelog.BOM_ExportData.xml --defaultsFile liquibase_MLCS.properties --classpath C:\Development\BLD-Trunk\SourceCode\Database\LiquiBase\lib\ojdbc7.jar --diffTypes="data" generateChangeLog

REM Exporting STRUCTURE
rem C:\Development\BLD-Trunk\SourceCode\Database\LiquiBase\liquibase --changeLogFile changelog\db.changelog.BOM_ExportStructure.xml --defaultsFile liquibase_TST.properties --classpath C:\Development\BLD-Trunk\SourceCode\Database\LiquiBase\lib\ojdbc7.jar generateChangeLog

:usage
@echo .
@echo Usage: %0 ^< ENVIRONMENT Ex: DEV, INT VALIDATE(V) or UPDATE(U) ^>
exit /B 1

:eof

