#!/bin/sh

BUILD_ROOT=build.spi
MOD=app
LIB_DIR=lib
cp "$BUILD_ROOT/proto/spi-lsp.jar" "$MOD/$LIB_DIR"
cp "$BUILD_ROOT/provider/spi-lsp-impl.jar" "$MOD/$LIB_DIR"
javac -cp "$MOD/$LIB_DIR/*" -d "$BUILD_ROOT/$MOD" -sourcepath "$MOD" "$MOD"/korhal/spi/lsp/client/*.java

