@REM ----------------------------------------------------------------------------
@REM Maven Wrapper startup script for Windows
@REM Generated for korhal:javatest
@REM ----------------------------------------------------------------------------
@echo off
setlocal enabledelayedexpansion

SET MAVEN_WRAPPER_JAR=.mvn\wrapper\maven-wrapper.jar
SET MAVEN_WRAPPER_PROPERTIES=.mvn\wrapper\maven-wrapper.properties

@REM Read wrapperUrl from properties
FOR /F "tokens=2 delims==" %%A IN ('findstr /B "wrapperUrl" "%MAVEN_WRAPPER_PROPERTIES%"') DO SET DOWNLOAD_URL=%%A
FOR /F "tokens=2 delims==" %%A IN ('findstr /B "distributionUrl" "%MAVEN_WRAPPER_PROPERTIES%"') DO SET DISTRIBUTION_URL=%%A

@REM Download wrapper JAR if missing
IF NOT EXIST "%MAVEN_WRAPPER_JAR%" (
    echo Downloading Maven Wrapper JAR...
    powershell -Command "Invoke-WebRequest -Uri '%DOWNLOAD_URL%' -OutFile '%MAVEN_WRAPPER_JAR%'"
    IF ERRORLEVEL 1 (
        echo ERROR: Failed to download Maven Wrapper JAR.
        echo Please download manually: %DOWNLOAD_URL%
        echo to %MAVEN_WRAPPER_JAR%
        exit /B 1
    )
)

@REM Resolve JAVA_HOME
IF NOT "%MAVEN_WRAPPER_JAVA_HOME%"=="" GOTO javaHomeSet
IF NOT "%JAVA_HOME%"=="" (
    SET MAVEN_WRAPPER_JAVA_HOME=%JAVA_HOME%
    GOTO javaHomeSet
)
FOR /F %%j IN ('java -XshowSettings:all -version 2^>^&1 ^| findstr "java.home"') DO SET MAVEN_WRAPPER_JAVA_HOME=%%j
:javaHomeSet

"%MAVEN_WRAPPER_JAVA_HOME%\bin\java" ^
  -classpath ".mvn\wrapper\maven-wrapper.jar" ^
  "-Dmaven.wrapper.distributionUrl=%DISTRIBUTION_URL%" ^
  org.apache.maven.wrapper.MavenWrapperMain %*
