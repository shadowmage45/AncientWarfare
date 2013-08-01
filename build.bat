@echo off
set STARTPATH=%CD%
echo BUILD STARTED AT %STARTPATH%
del /q *.zip
rd /s/q build
rd /s/q "%STARTPATH%\mcp\src\minecraft\shadowmage"
md build
xcopy /e/i/q/y "%STARTPATH%\AncientWarfare\src\shadowmage\*.*" "%STARTPATH%\mcp\src\minecraft\shadowmage\*.*"
echo FILES COPIED, RECOMPILING....
cd mcp
runtime\bin\python\python_mcp runtime\recompile.py %*
echo RECOMPILE COMPLETED, REOBFUSCATING.....
runtime\bin\python\python_mcp runtime\reobfuscate.py %*
cd..
echo COPYING COMPILED FILES FOR REPACKAGE.......
xcopy /e/i/q/y "%STARTPATH%\mcp\reobf\minecraft\*.*" "%STARTPATH%\build\*.*"
xcopy /e/i/q/y "%STARTPATH%\AncientWarfare\src\shadowmage\ancient_warfare\resources\*.*" "%STARTPATH%\build\shadowmage\ancient_warfare\resources\*.*"
xcopy /e/i/q/y "%STARTPATH%\AncientWarfare\src\mods\*.*" "%STARTPATH%\build\mods\*.*"
xcopy /e/i/q/y "%STARTPATH%\AncientWarfare\src\lang\*.*" "%STARTPATH%\build\lang\*.*"
xcopy /e/i/q/y "%STARTPATH%\AncientWarfare\src\*.info" "%STARTPATH%\build\*.info"
echo ZIPPING AND FINISHING BUILD...............

set version="BAD VERSION"
FOR /F "usebackq eol=# tokens=1,2,3 delims==" %%i in ("%STARTPATH%\AncientWarfare\src\shadowmage\ancient_warfare\resources\version.properties") do set version=%%j

7za a -r build.zip .\build\*.*
set DESTFILENAME=AW-%version%-%BUILD_NUMBER%.zip
rename build.zip %DESTFILENAME%
echo BUILD COMPLETED..........................
pause 
