#!/bin/sh

BUILD_ROOT=build.spi
MOD=app
LIB_DIR=lib
java -cp "$BUILD_ROOT/$MOD:$MOD/$LIB_DIR/*" korhal.spi.lsp.client.MyNeovim
