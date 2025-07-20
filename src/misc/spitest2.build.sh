#!/bin/sh

rm -rf $HOME/.m2/repository/korhal
mvn -f spitest2/pom.xml clean install

cd spitest2
java -cp "client/target/client-1.0-SNAPSHOT.jar:provider/target/provider-1.0-SNAPSHOT.jar:proto/target/proto-1.0-SNAPSHOT.jar" korhal.spi.lsp.client.MyNeovim

