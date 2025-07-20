#!/bin/sh

mvn -f spitest2/pom.xml clean
rm -rf $HOME/.m2/repository/korhal
