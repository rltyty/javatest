#!/bin/sh

rm -rf spitest2

mvn archetype:generate -DgroupId=korhal.spi -DartifactId=spitest2 \
  -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false

cd spitest2
rm -rf src

# 'EOF' disable expansion of variables like ${maven-compiler.version} in
# heredoc. To enable expansion use EOF unquoted.
tee pom.xml <<'EOF'
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>korhal.spi</groupId>
  <artifactId>spitest2</artifactId>
  <version>1.0</version>
  <packaging>pom</packaging>

  <!-- <modules> -->
  <!--   <module>proto</module> -->
  <!--   <module>provider</module> -->
  <!--   <module>client</module> -->
  <!-- </modules> -->

  <properties>
    <maven.compiler.source>24</maven.compiler.source>
    <maven.compiler.target>24</maven.compiler.target>
    <maven-compiler.version>3.14.0</maven-compiler.version>
    <lombok.version>1.18.38</lombok.version>
  </properties>

  <dependencies>
    <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>${lombok.version}</version>
    </dependency>
  </dependencies>

  <build>
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-compiler-plugin</artifactId>
          <version>${maven-compiler.version}</version>
          <configuration>
            <source>${maven.compiler.source}</source>
            <target>${maven.compiler.target}</target>

          <compilerArgs>
            <arg>-Xlint:all,-options,-path</arg>
            <arg>-parameters</arg>
              <!--
            <arg>-verbose</arg>
              -->
          </compilerArgs>

          <!-- https://projectlombok.org/setup/maven -->
          <!-- for boilerplate code generation like getter/setter -->
          <annotationProcessorPaths>
            <path>
              <groupId>org.projectlombok</groupId>
              <artifactId>lombok</artifactId>
              <version>${lombok.version}</version>
            </path>
          </annotationProcessorPaths>

          </configuration>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>

</project>
EOF

mvn archetype:generate -DgroupId=korhal.spi -DartifactId=proto -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
mvn archetype:generate -DgroupId=korhal.spi -DartifactId=provider -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
mvn archetype:generate -DgroupId=korhal.spi -DartifactId=client -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false


# 1. uncomment the 3 modules in the main pom.xml
# 2. after create classes and META-INF files:
#    mvn clean install
# NOTE: `mvn install` will install package to ~/.m2/repository/korhal/, to
# make pom dependency visible, then mvn compile knows where to find the
# dependency. IDE (Neovim) + jdtls also knows how to resolve symbols.
