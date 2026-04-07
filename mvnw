#!/bin/sh
# ----------------------------------------------------------------------------
# Maven Wrapper startup script for UN*X
# Generated for korhal:javatest
# ----------------------------------------------------------------------------

if [ -z "$MAVEN_WRAPPER_JAVA_HOME" ]; then
  if [ -n "$JAVA_HOME" ]; then
    MAVEN_WRAPPER_JAVA_HOME="$JAVA_HOME"
  else
    MAVEN_WRAPPER_JAVA_HOME=$(java -XshowSettings:all -version 2>&1 | grep 'java.home' | awk '{print $3}')
  fi
fi

MAVEN_WRAPPER_JAR=".mvn/wrapper/maven-wrapper.jar"
MAVEN_WRAPPER_PROPERTIES=".mvn/wrapper/maven-wrapper.properties"
DOWNLOAD_URL=$(grep "^wrapperUrl" "$MAVEN_WRAPPER_PROPERTIES" | cut -d= -f2-)
DISTRIBUTION_URL=$(grep "^distributionUrl" "$MAVEN_WRAPPER_PROPERTIES" | cut -d= -f2-)

# Download wrapper jar if missing
if [ ! -f "$MAVEN_WRAPPER_JAR" ]; then
  echo "Downloading Maven Wrapper JAR..."
  if command -v curl > /dev/null; then
    curl -o "$MAVEN_WRAPPER_JAR" "$DOWNLOAD_URL" -fsSL
  elif command -v wget > /dev/null; then
    wget -q -O "$MAVEN_WRAPPER_JAR" "$DOWNLOAD_URL"
  else
    echo "ERROR: Neither curl nor wget found. Please download:"
    echo "  $DOWNLOAD_URL"
    echo "to $MAVEN_WRAPPER_JAR"
    exit 1
  fi
fi

exec "$MAVEN_WRAPPER_JAVA_HOME/bin/java" \
  -classpath ".mvn/wrapper/maven-wrapper.jar" \
  "-Dmaven.wrapper.distributionUrl=$DISTRIBUTION_URL" \
  org.apache.maven.wrapper.MavenWrapperMain "$@"
