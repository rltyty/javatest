#include <stdio.h>
#include "MyClass.h"

JNIEXPORT jstring JNICALL Java_MyClass_myNativeMethod
  (JNIEnv *env, jobject obj, jstring jstr) {
  const char *s = (*env)->GetStringUTFChars(env, jstr, NULL);
  printf("Receive from Java: <%s>\n", s);
  (*env)->ReleaseStringUTFChars(env, jstr, s); // Always release it!
  return (*env)->NewStringUTF(env,  "I'm fine. And you?");
}
