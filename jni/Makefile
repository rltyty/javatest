.PHONY: all run clean test

ROOT = .
PLATFORM = $(shell $(ROOT)/systype.sh)
CC_FLAGS.linux=-shared -fPIC
CC_FLAGS.darwin=-dynamiclib
LIB_EXT.linux=so
LIB_EXT.darwin=dylib

all:
	javac -h . MyClass.java
	gcc $(CC_FLAGS.$(PLATFORM)) -I"$$JAVA_HOME/include" -I"$$JAVA_HOME/include/$(PLATFORM)" -o libmylib.$(LIB_EXT.$(PLATFORM)) mycode.c
	file libmylib.$(LIB_EXT.$(PLATFORM))
run:
	java -Djava.library.path=. MyClass "How are you?"

clean:
	rm -f *.class *.$(LIB_EXT.$(PLATFORM)) *.h
