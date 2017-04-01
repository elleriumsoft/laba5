call mvn clean package

SET SERVERPATH=E:\apache-tomee-plus-7.0.2
SET APPNAME=laba4-ear-1.0

del %SERVERPATH%\apps\* /s /q
rd %SERVERPATH%\apps\%APPNAME% /s /q
rd %SERVERPATH%\work\Catalina\localhost\app /s /q
call xcopy laba4-ear\target\%APPNAME%.ear %SERVERPATH%\apps\ /y /c /r
call xcopy lababase.s3db %SERVERPATH%\bin\ /y /c /r

cd /d %SERVERPATH%\bin\
call startup.bat

