@echo off
set STARTPATH=%CD%
echo BUILD STARTED AT %STARTPATH%
rd /s/q build
md build
xcopy /e/i/q/y "%STARTPATH%\AncientWarfare\src\shadowmage\*.*" "%STARTPATH%\mcp\src\minecraft\*.*"
echo FILES COPIED, RECOMPILING....
mcp\runtime\bin\python\python_mcp runtime\recompile.py %*
echo RECOMPILE COMPLETED, REOBFUSCATING....
mc\runtime\bin\python\python_mcp runtime\reobfuscate.py %*
xcopy /e/i/q/y "%STARTPATH%\mcp\reobf\minecraft\*.*" "%STARTPATH%\build\*.*"

xcopy /e/i/q/y "%STARTPATH%\AncientWarfare\src\shadowmage\ancient_warfare\resources\*.*" "%STARTPATH%\build\shadowmage\ancient_warfare\resources\*.*"

xcopy /e/i/q/y "%STARTPATH%\AncientWarfare\src\mods\*.*" "%STARTPATH%\build\mods\*.*"

del /q *.zip

7za a -r build.zip .\build\*.*

set DESTFILENAME=AW-Auto-MC152-%BUILD_NUMBER%.zip
rename build.zip %DESTFILENAME%

echo BUILD COMPLETED....
pause 
