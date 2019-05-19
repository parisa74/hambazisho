#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_ir_idpz_hambazisho_MyApplication_getClientID(
        JNIEnv *env,
        jobject /* this */) {
    std::string clientId = "05e1d1a5-0996-4ddf-aae9-b75684d3e5ac";
    return env->NewStringUTF(clientId.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_ir_idpz_hambazisho_MyApplication_getClientSecret(
        JNIEnv *env,
        jobject /* this */) {
    std::string clientSecret = "sLno6YNtO7Np5H1c2f9G";
    return env->NewStringUTF(clientSecret.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_ir_idpz_hambazisho_MyApplication_getSite(
        JNIEnv *env,
        jobject /* this */) {
    std::string site = "http://hambazisho.vaslapp.com";
    return env->NewStringUTF(site.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_ir_idpz_hambazisho_MyApplication_getUserName(
        JNIEnv *env,
        jobject /* this */) {
    std::string username = "android-Opt10qIEmYM28IYOdu2I";
    return env->NewStringUTF(username.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_ir_idpz_hambazisho_MyApplication_getPassWord(
        JNIEnv *env,
        jobject /* this */) {
    std::string password = "zkzJc8sDvwL9i1X4JtZ3";
    return env->NewStringUTF(password.c_str());
}