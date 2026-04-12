@echo off
REM Script de compilation du projet Labyrinthe_Solveur

echo.
echo ===================================
echo Compilation du projet
echo ===================================
echo.

echo [1/2] Nettoyage des anciens .class...
set BASE_DIR=%~dp0
del /Q "%BASE_DIR%src\main\java\client\*.class" "%BASE_DIR%src\main\java\serveur\*.class" "%BASE_DIR%src\main\java\commun\*.class" 2>nul
echo .class supprimes.


timeout /t 1 /nobreak >nul


echo.
echo [2/2] Compilation...
cd src\main\java
javac client\*.java serveur\*.java commun\*.java
if errorlevel 1 (
    echo Erreur lors de la compilation!
    exit /b 1
)


echo.
echo ===================================
echo Compilation terminee avec succes!
echo ===================================
echo.

echo Lancement du serveur :
echo    Consone : java serveur.ServeurApp
echo    GUI     : java serveur.ServeurGUI

echo Lancement du client :
echo    Console : java client.ClientTest
echo    GUI     : java client.ClientGUI

echo.