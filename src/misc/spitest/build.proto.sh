#!/bin/sh

BUILD_ROOT=build.spi
MOD=proto
TARGET=spi-lsp.jar
javac -d "$BUILD_ROOT/$MOD" -sourcepath $MOD "$MOD"/korhal/spi/lsp/*.java # *.java works in no quotes
jar cf "$BUILD_ROOT/$MOD/$TARGET" -C "$BUILD_ROOT/$MOD" korhal

