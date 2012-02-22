#include <opencv/cv.h>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/features2d/features2d.hpp>
#include <opencv2/highgui/highgui.hpp>

double calcCircularity(std::vector<cv::Point> contour);

std::string colorSomeStuff(cv::Mat& mbgra);
