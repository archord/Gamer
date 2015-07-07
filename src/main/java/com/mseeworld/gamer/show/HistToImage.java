/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.mseeworld.gamer.show;

import org.opencv.core.Core;
import static org.opencv.core.CvType.CV_8U;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

/**
 *
 * @author xy
 */
public class HistToImage {

    /**
     * 将opencv的直方图mat转换为图像
     *
     * @param hist，opencv的直方图，使用calcHist计算得来，其中cols=1,rows=直方图bin的个数
     * @return 直方图对应的图像
     */
    public static Mat getHistImage(Mat hist) {

        int scale = 1;
        int size = hist.rows();
        Core.MinMaxLocResult mmlr = Core.minMaxLoc(hist);

        Mat dstImage = new Mat(size * scale, size * 2, CV_8U, new Scalar(255));

        double hpt = 0.9 * size;
        for (int i = 0; i < size; i++) {
            double binValue = hist.get(i, 0)[0];           //   注意hist中是float类型    而在OpenCV1.0版中用cvQueryHistValue_1D
            double realValue = binValue * hpt / mmlr.maxVal;
            Core.rectangle(dstImage, new Point(i * 2 * scale, size - 1), new Point((i * 2 + 1) * scale - 1, size - realValue), new Scalar(0));
        }
        return dstImage;
    }
}
