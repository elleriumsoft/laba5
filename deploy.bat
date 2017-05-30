call mvn clean package

SET SERVERPATH=E:\apache-tomee-plus-7.0.2
SET APPNAME=laba5

del %SERVERPATH%\apps\* /s /q
rd %SERVERPATH%\apps\%APPNAME%-ear-1.0 /s /q
rd %SERVERPATH%\work\Catalina\localhost\app /s /q
md %SERVERPATH%\bin\xml

call xcopy %APPNAME%-ear\target\%APPNAME%-ear-1.0.ear %SERVERPATH%\apps\ /y /c /r
call xcopy %APPNAME%-ejb\xslt\* %SERVERPATH%\bin\xslt\ /y /c /r
call xcopy %APPNAME%-ejb\xsd\* %SERVERPATH%\bin\xsd\ /y /c /r
call xcopy lababase.s3db %SERVERPATH%\bin\ /d

cd /d %SERVERPATH%\bin\
call startup.bat

