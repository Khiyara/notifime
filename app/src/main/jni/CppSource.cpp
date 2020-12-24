//
// Created by khiyara99 on 19/12/20.
//

#include <string>
#include "CppSource.h"

jstring jniCreateMessageNotification(JNIEnv *env, jobject, jstring title, jstring date, jstring time){

    // append to prove it is from C++ code
    const char *titleBuild = env->GetStringUTFChars(title, NULL);
    const char *dateBuild = env->GetStringUTFChars(date, NULL);
    const char *timeBuild = env->GetStringUTFChars(time, NULL);
    std::string titleTmp = titleBuild;
    std::string dateTmp = dateBuild;
    std::string timeTmp = timeBuild;
    std::string tmp = titleTmp + " will be reminded at " + dateTmp + " " + timeTmp;
    jstring messageJS = env->NewStringUTF(tmp.c_str());

    return messageJS;
}

jstring jniCreateTextNotification(JNIEnv *env, jobject, jstring title){

    // append to prove it is from C++ code
    const char *titleBuild = env->GetStringUTFChars(title, NULL);
    std::string titleTmp = titleBuild;
    std::string tmp = titleTmp + " new episode is release now!!";

    jstring textJS = env->NewStringUTF(tmp.c_str());

    return textJS;
}