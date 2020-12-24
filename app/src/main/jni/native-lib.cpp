//
// Created by khiyara99 on 19/12/20.
//

#include "CppSource.h"
#include <jni.h>
#include <string>

#ifdef __cplusplus
extern "C" {
#endif

static const JNINativeMethod gMethods[] = {
         {
         "nBuildMessage",
         "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;",
         (void *) jniCreateMessageNotification
         },
         {"nBuildText",
          "(Ljava/lang/String;)Ljava/lang/String;",
          (void *) jniCreateTextNotification
         }
};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    puts("いくぞ");

    JNIEnv *env = NULL;
    jint result = -1;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return result;
    }
    puts("いくぞ");
    jclass clazz = env->FindClass("id/ac/ui/cs/mobileprogramming/rizkhiph/notifime/JniManager");
    if (clazz == NULL) {
        return result;
    }

    jint count = sizeof(gMethods) / sizeof(gMethods[0]);

    if (env->RegisterNatives(clazz, gMethods, count) != JNI_OK) {
        return result;
    }

    result = JNI_VERSION_1_6;

    puts("やった！！");
    return result;
}

#ifdef __cplusplus
}
#endif