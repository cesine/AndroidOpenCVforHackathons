/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <stdarg.h>
#include <jni.h>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/features2d/features2d.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <vector>


#include <android/log.h>
#include "image_processing.h"


using namespace cv;
using namespace std;

/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/HelloJni/HelloJni.java
 */
extern "C" {
     JNIEXPORT jstring JNICALL Java_com_androidmontreal_opencv_AndroidOpenCVforHackathonsActivity_stringFromJNI(JNIEnv * env, jobject thiz);
 };

 JNIEXPORT jstring JNICALL Java_com_androidmontreal_opencv_AndroidOpenCVforHackathonsActivity_stringFromJNI(JNIEnv * env, jobject thiz)
 {
	 return env->NewStringUTF("Hello From CPP");
 }
 extern "C" {
      JNIEXPORT jstring JNICALL Java_com_androidmontreal_opencv_OpenCVPreview_processimage(JNIEnv* env, jobject thiz, jint width, jint height, jbyteArray yuv, jintArray bgra);
  };
 JNIEXPORT jstring JNICALL Java_com_androidmontreal_opencv_OpenCVPreview_processimage(JNIEnv* env, jobject thiz, jint width, jint height, jbyteArray yuv, jintArray bgra)
  {

	 // Get input and output arrays
	jbyte* _yuv = env->GetByteArrayElements(yuv, 0);
	jint* _bgra = env->GetIntArrayElements(bgra, 0);

	Mat myuv(height + height / 2, width, CV_8UC1, (unsigned char *) _yuv);
	Mat mbgra(height, width, CV_8UC4, (unsigned char *) _bgra);

	// Please pay attention to BGRA byte order
	// ARGB stored in java as int array becomes BGRA at native level
	cvtColor(myuv, mbgra, CV_YUV420sp2BGR, 4);


	int imageYwidth = mbgra.size[1];
	int imageXheight = mbgra.size[0];

	 vector<vector<Point> > items;
	 string resultsIfAny = colorSomeStuff(mbgra);

	 env->ReleaseIntArrayElements(bgra, _bgra, 0);
	 env->ReleaseByteArrayElements(yuv, _yuv, 0);

	 const char* resultCstring = resultsIfAny.c_str();
	 return env->NewStringUTF(resultCstring);
  }
