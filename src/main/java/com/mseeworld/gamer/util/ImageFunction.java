/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.mseeworld.gamer.util;

import com.mseeworld.gamer.show.ImageShow;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author xy
 */
public class ImageFunction {

    private static final Log log = LogFactory.getLog(ImageFunction.class);

    /**
     * 应该返回四个角点！！！！
     * 适用于子图像被旋转或缩放的情况
     * 通过Homography方法对特征点建立映射矩阵，来计算subImg在srcImg中的匹配区域
     * SURF方法在subImg较小时，提取的特征点偏少，匹配误差大
     * SIFT方法提取特征点较多，或许可用弥补在subImg较小时，SURF特征点较少的问题
     *
     * @param srcImg
     * @param subImg
     * @return
     */
    public static Rect getSubRegin(Mat srcImg, Mat subImg) {

        Rect subRegin = new Rect();

        Mat srcblur = new Mat();
        Mat subblur = new Mat();
        MatOfKeyPoint srckp = new MatOfKeyPoint();
        MatOfKeyPoint subkp = new MatOfKeyPoint();
        Mat srcDescriptor = new Mat();
        Mat subDescriptor = new Mat();

        Imgproc.blur(srcImg, srcblur, new Size(3, 3));
        Imgproc.blur(subImg, subblur, new Size(3, 3));

        FeatureDetector srcDetector = FeatureDetector.create(FeatureDetector.SIFT);
        FeatureDetector subDetector = FeatureDetector.create(FeatureDetector.SIFT);
        srcDetector.detect(srcblur, srckp);
        subDetector.detect(subblur, subkp);

        DescriptorExtractor srcExtractor = DescriptorExtractor.create(DescriptorExtractor.SIFT);
        DescriptorExtractor subExtractor = DescriptorExtractor.create(DescriptorExtractor.SIFT);
        srcExtractor.compute(srcblur, srckp, srcDescriptor);
        subExtractor.compute(subblur, subkp, subDescriptor);
        log.debug("src key point size: " + srckp.rows());
        log.debug("sub key point size: " + subkp.rows());

        DescriptorMatcher dm = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
        dm.add(Arrays.asList(srcDescriptor));
        dm.train();
        MatOfDMatch modm = new MatOfDMatch();
        dm.match(subDescriptor, modm);
        log.debug("match key point size: " + modm.rows());

        /*
         if (log.isDebugEnabled()) {
         log.debug("idx, distance, subx-srcx, suby-srcy");
         for (int i = 0; i < modm.rows(); i++) {
         double[] tmp = modm.get(i, 0);
         int idx1 = (int) tmp[0];
         int idx2 = (int) tmp[1];
         log.debug((i + 1) + " , " + tmp[3] + " , " + (subkp.get(idx1, 0)[0] - srckp.get(idx2, 0)[0]) + " , "
         + (subkp.get(idx1, 0)[1] - srckp.get(idx2, 0)[1]));
         }
         }
         */
        List<DMatch> srclist = modm.toList();
        ArrayList<Point> points1 = new ArrayList<>();
        ArrayList<Point> points2 = new ArrayList<>();
        for (DMatch tmatch : srclist) {
            points1.add(new Point(srckp.get(tmatch.trainIdx, 0)[0], srckp.get(tmatch.trainIdx, 0)[1]));
            points2.add(new Point(subkp.get(tmatch.queryIdx, 0)[0], subkp.get(tmatch.queryIdx, 0)[1]));
        }

        MatOfPoint2f mop1 = new MatOfPoint2f();
        MatOfPoint2f mop2 = new MatOfPoint2f();
        mop1.fromList(points1);
        mop2.fromList(points2);

        Mat mask = new Mat();
        //在特征点较少时（小于10个），计算的变换矩阵误差较大
        Mat H = Calib3d.findHomography(mop2, mop1, Calib3d.FM_RANSAC, 1, mask);

        points1 = new ArrayList<>();
        points1.add(new Point(0, 0));
        points1.add(new Point(0, subImg.rows()));
        points1.add(new Point(subImg.cols(), subImg.rows()));
        points1.add(new Point(subImg.cols(), 0));

        MatOfPoint2f subImgBorder = new MatOfPoint2f();
        MatOfPoint2f borderTransform = new MatOfPoint2f();
        subImgBorder.fromList(points1);

        Core.perspectiveTransform(subImgBorder, borderTransform, H);
        List<Point> transPoint = borderTransform.toList();
        subRegin.x = (int) transPoint.get(0).x;
        subRegin.y = (int) transPoint.get(0).y;
        subRegin.width = (int) (transPoint.get(2).x - transPoint.get(0).x);
        subRegin.height = (int) (transPoint.get(1).y - transPoint.get(0).y);

//        过滤掉匹配异常的点，用于画出匹配点 
//        List<DMatch> dmlists = modm.toList();
//        ArrayList<DMatch> inliers = new ArrayList<>();
//        for (int i = 0; i < mask.rows(); i++) {
//            if (mask.get(i, 0)[0] > 0) {
//                inliers.add(dmlists.get(i));
//            }
//        }
//        MatOfDMatch modm2 = new MatOfDMatch();
//        modm2.fromList(inliers);
//        log.debug("after Homography, key point size: " + modm2.rows());
//        画出匹配的区域外框
//        Core.line(srcImg, new Point(borderTransform.get(0, 0)), new Point(borderTransform.get(1, 0)), new Scalar(255), 1);
//        Core.line(srcImg, new Point(borderTransform.get(1, 0)), new Point(borderTransform.get(2, 0)), new Scalar(255), 1);
//        Core.line(srcImg, new Point(borderTransform.get(2, 0)), new Point(borderTransform.get(3, 0)), new Scalar(255), 1);
//        Core.line(srcImg, new Point(borderTransform.get(3, 0)), new Point(borderTransform.get(0, 0)), new Scalar(255), 1);
//        直接画出矩形，在目标区域为矩形时可用，大部分情况下不是矩形，所有需要分别画四条边
//        Core.rectangle(srcImg, new Point(subRegin.x, subRegin.y), new Point(subRegin.x + subRegin.width, subRegin.y + subRegin.height), new Scalar(255));
//        Mat out = new Mat();
//        Features2d.drawMatches(subImg, subkp, srcImg, srckp, modm2, out, new Scalar(255), new Scalar(255), new MatOfByte(), Features2d.NOT_DRAW_SINGLE_POINTS);
//        new ImageShow(out, "out").run();
        return subRegin;
    }
    
    /**
     * 适用于子图像没有被旋转或缩放的情况
     * 用SURF提取特征点，使用Homography过滤异常特征点，取出匹配成功的任意（第一个）匹配点
     * 用矩形减法直接计算匹配区域
     * @param srcImg
     * @param subImg
     * @return 
     */
    public static Rect getSubRegin2(Mat srcImg, Mat subImg) {

        Rect subRegin = new Rect();

        Mat srcblur = new Mat();
        Mat subblur = new Mat();
        MatOfKeyPoint srckp = new MatOfKeyPoint();
        MatOfKeyPoint subkp = new MatOfKeyPoint();
        Mat srcDescriptor = new Mat();
        Mat subDescriptor = new Mat();

        Imgproc.blur(srcImg, srcblur, new Size(3, 3));
        Imgproc.blur(subImg, subblur, new Size(3, 3));

        FeatureDetector srcDetector = FeatureDetector.create(FeatureDetector.SURF);
        FeatureDetector subDetector = FeatureDetector.create(FeatureDetector.SURF);
        srcDetector.detect(srcblur, srckp);
        subDetector.detect(subblur, subkp);

        DescriptorExtractor srcExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
        DescriptorExtractor subExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
        srcExtractor.compute(srcblur, srckp, srcDescriptor);
        subExtractor.compute(subblur, subkp, subDescriptor);
        log.debug("src key point size: " + srckp.rows());
        log.debug("sub key point size: " + subkp.rows());

        DescriptorMatcher dm = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
        dm.add(Arrays.asList(srcDescriptor));
        dm.train();
        MatOfDMatch modm = new MatOfDMatch();
        dm.match(subDescriptor, modm);
        log.debug("match key point size: " + modm.rows());

        List<DMatch> srclist = modm.toList();
        ArrayList<Point> points1 = new ArrayList<>();
        ArrayList<Point> points2 = new ArrayList<>();
        for (DMatch tmatch : srclist) {
            points1.add(new Point(srckp.get(tmatch.trainIdx, 0)[0], srckp.get(tmatch.trainIdx, 0)[1]));
            points2.add(new Point(subkp.get(tmatch.queryIdx, 0)[0], subkp.get(tmatch.queryIdx, 0)[1]));
        }

        MatOfPoint2f mop1 = new MatOfPoint2f();
        MatOfPoint2f mop2 = new MatOfPoint2f();
        mop1.fromList(points1);
        mop2.fromList(points2);

        Mat mask = new Mat();
        //在特征点较少时（小于10个），计算的变换矩阵误差较大
        Calib3d.findHomography(mop2, mop1, Calib3d.FM_RANSAC, 1, mask);
//        过滤掉匹配异常的点，用于画出匹配点 
        List<DMatch> dmlists = modm.toList();
        ArrayList<DMatch> inliers = new ArrayList<>();
        for (int i = 0; i < mask.rows(); i++) {
            if (mask.get(i, 0)[0] > 0) {
                inliers.add(dmlists.get(i));
            }
        }

        Point srcPoint = new Point(srckp.get(inliers.get(0).trainIdx, 0)[0], srckp.get(inliers.get(0).trainIdx, 0)[1]);
        Point subPoint = new Point(subkp.get(inliers.get(0).queryIdx, 0)[0], subkp.get(inliers.get(0).queryIdx, 0)[1]);
        subRegin.x = (int) (srcPoint.x - subPoint.x);
        subRegin.y = (int) (srcPoint.y - subPoint.y);
        subRegin.width = subImg.cols();
        subRegin.height = subImg.rows();

//        画出匹配的区域外框
//        Core.rectangle(srcImg, new Point(subRegin.x, subRegin.y), new Point(subRegin.x + subRegin.width, subRegin.y + subRegin.height), new Scalar(255));
//        new ImageShow(srcImg, "srcImg").run();
        return subRegin;
    }
}
