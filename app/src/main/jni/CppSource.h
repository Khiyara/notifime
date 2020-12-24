//
// Created by khiyara99 on 19/12/20.
//

#ifndef NOTIFIME_CPPSOURCE_H
#define NOTIFIME_CPPSOURCE_H

#include <jni.h>

jstring jniCreateMessageNotification(JNIEnv *env, jobject, jstring title, jstring date, jstring time);
jstring jniCreateTextNotification(JNIEnv *env, jobject, jstring title);

#endif //NOTIFIME_CPPSOURCE_H
