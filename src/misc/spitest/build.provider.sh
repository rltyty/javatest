#!/bin/sh

BUILD_ROOT=build.spi
MOD=provider
TARGET=spi-lsp-impl.jar
LIB_DIR=lib
LIB="spi-lsp.jar"

cp "$BUILD_ROOT/proto/$LIB" "$MOD/lib"
javac -cp "$MOD/$LIB_DIR/$LIB" -d "$BUILD_ROOT/$MOD" -sourcepath "$MOD" "$MOD"/korhal/spi/lsp/impl/c/*.java
javac -cp "$MOD/$LIB_DIR/$LIB" -d "$BUILD_ROOT/$MOD" -sourcepath "$MOD" "$MOD"/korhal/spi/lsp/impl/java/*.java
jar cf "$BUILD_ROOT/$MOD/$TARGET" -C "$BUILD_ROOT/$MOD" korhal -C "$MOD" META-INF

