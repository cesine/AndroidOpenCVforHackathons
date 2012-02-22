#include "image_processing.h"
#include <android/log.h>

#define LOG_TAG "Image Processing"
#  define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#  define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)


using namespace cv;
using namespace std;

/* Calculate the circularity of a contour */
double calcCircularity(vector<Point> contour) {
	// Check circularity
	double perimeter = arcLength(contour, true);
	double area = contourArea(contour);
	double circularity = 4 * 3.14159265 * area / (perimeter * perimeter);
	return circularity;
}


/*
 *  Finds all the shapes (contours) in a region
 *  1. gets a thesholded matrix to find outlines
 *  2. gets all the contours around those outlines, contours are stored as a vector of points
 *
 */
string colorSomeStuff(Mat& mbgra) {
	LOGI("Using thresholds to find contrast and then contours around those highcontrast items...");

	int width = mbgra.size[1];
	int height = mbgra.size[0];

	// Separate the image in 3 places ( B, G and R )
	vector<Mat> rgbPlanes;
	split(mbgra, rgbPlanes);

	/*
	Get the yellow plane since most people wont write in yellow on a white board,
	then apply an adaptive threshold with a 31pixel window to look for contrast,
	save the contrast into the thresh matrix
	*/
	Mat yellow = rgbPlanes[1]/2+rgbPlanes[2]/2;
	Mat thresh;
	adaptiveThreshold(yellow, thresh, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY_INV, 31, 11);
	//Blackboard dont do an inverse, it will already be dark on light : adaptiveThreshold(yellow, thresh, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 31, 10);



	/*
	Take the thresh matrix, and look for contours in it
	*/
	vector<vector<Point> > contours;
	findContours(thresh, contours, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_NONE);//###
	LOGI("Contours: %d", contours.size());

	/*
	For each contour, find out how rectangle it is (between .8 and 1 is a pretty good rectangle candidate)
	*/
	vector<vector<Point> > checkboxes;
	for (int i = 0; i < contours.size(); i++) {
		Rect rect = boundingRect(contours[i]);

		// Make contour convex, not sure why but probably to get the outermost line of the contour
		vector<Point> convex;
		convexHull(contours[i], convex);

		// Check roundness
		double roundness = calcCircularity(convex);

		if (roundness < 0.8)
			continue;
		checkboxes.push_back (convex);
	}
	LOGI("Potential Checkboxes: %d", checkboxes.size());



	/*
	 * Draw the checkbox-like contours which were found in green Scalar(0, 255, 0, 255) so you can see what the function is doing...
	 */
	drawContours(mbgra, checkboxes, -1, Scalar(0, 255, 0, 255), 2);

	/*
	 * Draw a line below the "roundish" objects if you wish
	 */
	for (int i = 0; i < checkboxes.size(); i++) {
		/*
		 * Get the rectangle around the contour, use this if you need to know the x,y of the contour,
		 * or if you want to know the width or height of the contour.
		 *
		 * WATCHOUT: if you hold the andriod in portrait, x will be y, and you y will be x.
		 */
		Rect rect = boundingRect(checkboxes[i]);

		/*
		 * Draw a line across the screen,  under the "checkbox"
		 * you will see if your x is truely the x or if it is the y...
		 */
		if(rect.width >10){
			int margin = 5;
			int width = mbgra.size[0];
			int height = mbgra.size[1];
			line(mbgra
				, Point(rect.x + rect.width+margin, 0)
				, Point(rect.x + rect.width, height+margin)
				, Scalar(200, 200, 0, 255)
				, 2);
		}
	}

	/*
	 * Draw the threshold matrix in red Scalar(0, 0, 255, 255)as a mask on top of the image
	 */
	mbgra.setTo(Scalar(0, 0, 255, 255), thresh);

	/*
	 * Concatinate a string and return it to the Java,
	 *  if you have something that you want to return
	 */
	stringstream ss;
	ss << "This is the result for this frame " << contours.size();
	string resultString = ss.str();

	return resultString;
}
