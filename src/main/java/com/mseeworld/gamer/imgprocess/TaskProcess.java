/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.mseeworld.gamer.imgprocess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mseeworld.gamer.ai.ProcessImage;
import com.mseeworld.gamer.config.GamerEnum;
import com.mseeworld.gamer.show.HistToImage;
import com.mseeworld.gamer.show.ImageShow;
import com.mseeworld.gamer.ui.FunctionRegin;
import com.mseeworld.gamer.ui.GameUI;
import com.mseeworld.gamer.ui.TaskUI;
import com.mseeworld.gamer.util.CommonFunction;
import com.mseeworld.gamer.util.Constant;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
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
import static org.opencv.highgui.Highgui.CV_LOAD_IMAGE_GRAYSCALE;
import static org.opencv.highgui.Highgui.imread;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author xy
 */
public class TaskProcess extends BaseProcess {

    private static final Log log = LogFactory.getLog(TaskProcess.class);
    private TaskUI uicfg;

    public TaskProcess() {
        super();
    }

    public TaskProcess(String srcImg) {
        super(srcImg);
    }

    @Override
    public int initConfigFile(String cfgName) {
        try {
            String taskui = CommonFunction.readFile(cfgName);
            if (!taskui.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                ObjectReader reader = mapper.reader(TaskUI.class);
                uicfg = reader.readValue(taskui);
            } else {
                log.error("config file is empty.");
                return GamerEnum.CONFIG_FILE_EMPTY;
            }
        } catch (IOException ex) {
            log.error("read json error.", ex);
        }
        return GamerEnum.SUCCESS;
    }

    @Override
    public int getTargetRegin() {
        srcMat = imread(srcImg, CV_LOAD_IMAGE_GRAYSCALE);
        bgMat = imread(uicfg.getBgImage(), CV_LOAD_IMAGE_GRAYSCALE);
        labelMat = imread(uicfg.getLabelImage(), CV_LOAD_IMAGE_GRAYSCALE);

        return GamerEnum.SUCCESS;
    }

    @Override
    public int preProcess() {

        String srcImg = "E:\\program_src\\image\\task\\kj5.png";
        String label = "E:\\program_src\\image\\task\\label.png";

        Mat gray = imread(srcImg, CV_LOAD_IMAGE_GRAYSCALE);
        if (gray.empty()) {
            System.out.println("read image error!");
            exit(0);
        }
        Mat detected_edges = new Mat();
        Imgproc.blur(gray, detected_edges, new Size(3, 3));
        FeatureDetector fast = FeatureDetector.create(FeatureDetector.SURF);
        MatOfKeyPoint kps = new MatOfKeyPoint();
        fast.detect(detected_edges, kps);

        Mat trainDescriptor = new Mat();
        DescriptorExtractor de = DescriptorExtractor.create(DescriptorExtractor.SURF);
        de.compute(gray, kps, trainDescriptor);
        System.out.println(kps.cols());
        System.out.println(kps.rows());

        Mat gray2 = imread(label, CV_LOAD_IMAGE_GRAYSCALE);
        if (gray2.empty()) {
            System.out.println("read image error!");
            exit(0);
        }
        Mat detected_edges2 = new Mat();
        Imgproc.blur(gray2, detected_edges2, new Size(3, 3));
        FeatureDetector fast2 = FeatureDetector.create(FeatureDetector.SURF);
        MatOfKeyPoint kps2 = new MatOfKeyPoint();
        fast2.detect(detected_edges2, kps2);

        Mat queryDescriptor = new Mat();
        DescriptorExtractor de2 = DescriptorExtractor.create(DescriptorExtractor.SURF);
        de2.compute(gray2, kps2, queryDescriptor);
        System.out.println(kps2.cols());
        System.out.println(kps2.rows());
        System.out.println(kps2.channels());

        DescriptorMatcher dm = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
        dm.add(Arrays.asList(trainDescriptor));
        dm.train();
        MatOfDMatch modm = new MatOfDMatch();
        dm.match(queryDescriptor, modm);
        System.out.println(modm.cols());
        System.out.println(modm.rows());
        System.out.println(modm.channels());
        for (int i = 0; i < modm.rows(); i++) {
            double[] tmp = modm.get(i, 0);
            int idx1 = (int) tmp[0];
            int idx2 = (int) tmp[1];
            System.out.println((i + 1) + " * " + tmp[3] + " * " + (kps2.get(idx1, 0)[0] - kps.get(idx2, 0)[0]) + " * "
                    + (kps2.get(idx1, 0)[1] - kps.get(idx2, 0)[1]));
        }
//
//        filtDMatch(modm, 3);

        List<DMatch> srclist = modm.toList();
        ArrayList<Point> points1 = new ArrayList<>();
        ArrayList<Point> points2 = new ArrayList<>();
        for (DMatch tmatch : srclist) {
            points1.add(new Point(kps.get(tmatch.trainIdx, 0)[0], kps.get(tmatch.trainIdx, 0)[1]));
            points2.add(new Point(kps2.get(tmatch.queryIdx, 0)[0], kps2.get(tmatch.queryIdx, 0)[1]));
        }
        MatOfPoint2f mop1 = new MatOfPoint2f();
        MatOfPoint2f mop2 = new MatOfPoint2f();
        mop1.fromList(points1);
        mop2.fromList(points2);

        Mat mask = new Mat();
        Mat rst = Calib3d.findHomography(mop1, mop2, Calib3d.FM_RANSAC, 1, mask);
        System.out.println(rst.toString());

        List<DMatch> dmlists = modm.toList();
        ArrayList<DMatch> inliers = new ArrayList<>();
        for (int i = 0; i < mask.rows(); i++) {
            if (mask.get(i, 0)[0] > 0) {
                System.out.println(mask.get(i, 0)[0]);
                inliers.add(dmlists.get(i));
            }
        }
        MatOfDMatch modm2 = new MatOfDMatch();
        modm2.fromList(inliers);
        System.out.println(modm2.cols());
        System.out.println(modm2.rows());
        System.out.println(modm2.channels());

        Mat out = new Mat();
        Features2d.drawMatches(gray2, kps2, gray, kps, modm2, out, new Scalar(255), new Scalar(255), new MatOfByte(), Features2d.NOT_DRAW_SINGLE_POINTS);
        new ImageShow(out, "out").run();
        return GamerEnum.SUCCESS;
    }

    @Override
    public int recognition() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int segmentation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public MatOfDMatch filtDMatch(MatOfDMatch match, int sigma) {

        int featureSize = match.rows();
        Mat tmat = new Mat();
        tmat.create(match.rows(), match.cols(), CvType.CV_64F);

        for (int i = 1; i <= featureSize; i++) {
            tmat.put(i - 1, 0, match.get(i - 1, 0)[3]);
        }

        Mat covar = new Mat();
        Mat mean = new Mat();
        Core.calcCovarMatrix(tmat, covar, mean, Core.COVAR_ROWS | Core.COVAR_NORMAL);
        System.out.println(mean.get(0, 0)[0]);
        System.out.println(covar.get(0, 0)[0]);

        return match;
    }

    /**
     * 该程序处理目标为白色背景，黑色前景（汉字）的图像。所以第一步要把图像转换为该种显示的。 1，背景前景计算方法： 目前采用3）
     * 1）图像分区，根据直方图，在不同的阈值时计算图的均值方差，直到满足某一条件停止。source extractor方法。对自然图像不行。
     * 2）灰度拉伸，使前景和背景分局两端，图像的亮度会偏向背景值的方向。计算图像的均值，判断总体偏亮还是偏暗。
     * 3）不灰度拉伸图像，直接计算图像的均值，判断总体偏亮还是偏暗。
     *
     * @param srcImage
     */
    public void calAvgForeAndBackground(Mat srcImage) {

        int i, j, k, m, n;

        double avgground = getMatGrayAvg(srcImage);
        if (avgground < Constant.MAX_GRAY_VALUE / 2.0) {//如果背景偏暗(是否使用MAX_GRAY_VALUE/2.0，值得商量)，则反转图像灰度
            Core.bitwise_not(srcImage, srcImage);
        }

        //int thld = 100;
        int thld = CommonFunction.otsu(srcImage);
        System.out.println(thld);
        Mat biny = new Mat();
        Imgproc.threshold(srcImage, biny, thld, Constant.MAX_GRAY_VALUE, 0);

        new ImageShow(biny, "biny").run();
        Mat edImg = filtromorfologico(biny, 7, 11);
        new ImageShow(edImg, "edImg").run();

//        List<Rect> lines = getLines(edImg, thld);
//        i = 0;
//        for (Rect line : lines) {
//            Core.rectangle(srcImage, new Point(line.x, line.y), new Point(line.x + line.width - 1, line.y + line.height - 1), new Scalar(128));
//            System.out.println(++i + " " + line.x + " " + line.y + " " + line.width + " " + line.height);
//        }
//        new ImageShow(srcImage, "src").run();
        int kernel_size = 3;
        int lowThreshold = 50;
        int ratio = 3;
        Mat detected_edges = new Mat();
        Imgproc.blur(srcImage, detected_edges, new Size(3, 3));
        Imgproc.Canny(detected_edges, detected_edges, lowThreshold, lowThreshold * ratio, kernel_size, false);
        new ImageShow(detected_edges, "detected_edges").run();
//        Mat enhance = new Mat();
//        Imgproc.equalizeHist(srcImage, enhance);
//        new ImageShow(enhance, "enhance").run();
//        System.out.println(getMatGrayAvg(srcImage));
//        System.out.println(getMatGrayAvg(enhance));
//
//        Mat tempmask = new Mat(); //要么为空，要么赋值。这种申请大小new Mat(srcImage.size(), CvType.CV_8UC1)不复制的情况，会导致计算出错
//        MatOfFloat ranges = new MatOfFloat(Constant.MIN_GRAY_VALUE, Constant.MAX_GRAY_VALUE);
//        MatOfInt histSize = new MatOfInt(Constant.MAX_GRAY_VALUE);
//        List<Mat> images = Arrays.asList(srcImage);
//        Mat hist = new Mat();
//        
//        Imgproc.calcHist(Arrays.asList(srcImage), new MatOfInt(0), tempmask, hist, histSize, ranges);
//        new ImageShow(HistToImage.getHistImage(hist), "src").run();
//        
//        Imgproc.calcHist(Arrays.asList(enhance), new MatOfInt(0), tempmask, hist, histSize, ranges);
//        new ImageShow(HistToImage.getHistImage(hist), "enhance").run();
    }

    public Mat filtromorfologico(Mat srcImage, int ke, int kd) {
        int ek = 3;
        int dk = 5;
        Mat modifica = new Mat();
        Imgproc.erode(srcImage, modifica, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2 * ek + 1, 2 * ek + 1), new Point(ek, ek)));
        Imgproc.dilate(modifica, modifica, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2 * dk + 1, 2 * dk + 1), new Point(dk, dk)));
        return modifica;
    }

    public float getMatGrayAvg(Mat mat) {
        int width = mat.cols();
        int height = mat.rows();

        float total = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                total += mat.get(j, i)[0];
            }
        }
        return total / (width * height);
    }

    public List<Rect> getLines(Mat srcImage, int thld) {

        ArrayList<Rect> lines = new ArrayList<>();

        int rowNumber = srcImage.rows();
        int colNumber = srcImage.cols();  //*srcImage.channels()

        int leftX1 = 0, leftY1 = 0, leftY2 = 0, leftStart = 0, leftDone = 0;
        int rightX1 = 0, rightY1 = 0, rightY2 = 0, rightStart = 0, rightDone = 0;
        int lineW, lineH;

        for (int i = 0; i < rowNumber; i++) {
            //从左到右，寻找第一个有值的坐标
            if (leftDone == 0) {
                for (int j = 0; j < colNumber; j++) {
                    if (srcImage.get(i, j)[0] < thld) {
                        if (leftStart == 0) {
                            leftX1 = j;
                            leftY1 = i;
                            leftStart = 1;
                        } else if (j < leftX1) {
                            leftX1 = j;
                        }
                        break;
                    } else if (j == colNumber - 1 && leftStart == 1) {
                        leftDone = 1;
                        leftStart = 0;
                        leftX1 = leftX1 > 0 ? leftX1 - 1 : leftX1;
                        leftY1 = leftY1 > 0 ? leftY1 - 1 : leftY1;
                        leftY2 = i;
                    }
                }
                if (i == rowNumber - 1 && leftStart == 1) {
                    leftDone = 1;
                    leftStart = 0;
                    leftX1 = leftX1 > 0 ? leftX1 - 1 : leftX1;
                    leftY1 = leftY1 > 0 ? leftY1 - 1 : leftY1;
                    leftY2 = i;
                }
            }

            //从右到左，寻找第一个有值的坐标
            if (rightDone == 0) {
                for (int j = colNumber - 1; j >= 0; j--) {
                    if (srcImage.get(i, j)[0] < thld) {
                        if (rightStart == 0) {
                            rightX1 = j;
                            rightY1 = i;
                            rightStart = 1;
                        } else if (j > rightX1) {
                            rightX1 = j;
                        }
                        break;
                    } else if (j == 0 && rightStart == 1) {
                        rightDone = 1;
                        rightStart = 0;
                        rightX1 = rightX1 < colNumber - 1 ? rightX1 + 1 : rightX1;
                        rightY1 = rightY1 > 0 ? rightY1 - 1 : rightY1;
                        rightY2 = i;
                    }
                }
                if (i == rowNumber - 1 && rightStart == 1) {
                    rightDone = 1;
                    rightStart = 0;
                    rightX1 = rightX1 < colNumber - 1 ? rightX1 + 1 : rightX1;
                    rightY1 = rightY1 > 0 ? rightY1 - 1 : rightY1;
                    rightY2 = i;
                }
            }
            if (leftDone == 1 && rightDone == 1) {
                leftDone = 0;
                rightDone = 0;
                if (leftY1 > rightY1) {
                    leftY1 = rightY1;
                }
                if (leftY2 > rightY2) {
                    rightY2 = leftY2;
                }
                lineW = rightX1 - leftX1 + 1;
                lineH = rightY2 - leftY1 + 1;
                if (lineH >= Constant.MIN_LINE_HEIGHT) {
                    lines.add(new Rect(leftX1, leftY1, lineW, lineH));
                }
            }
        }
        return lines;
    }

    public void example(Mat srcImage) {

        new ImageShow(srcImage, "hist").run();
        double avgground = getMatGrayAvg(srcImage);
        if (avgground < Constant.MAX_GRAY_VALUE / 2.0) {//如果背景偏暗(是否使用MAX_GRAY_VALUE/2.0，值得商量)，则反转图像灰度
            Core.bitwise_not(srcImage, srcImage);
        }
        new ImageShow(srcImage, "hist").run();

        Mat enhance = new Mat();
        Imgproc.equalizeHist(srcImage, enhance);
        new ImageShow(enhance, "enhance").run();
        System.out.println(getMatGrayAvg(srcImage));
        System.out.println(getMatGrayAvg(enhance));

        Mat tempmask = new Mat(); //要么为空，要么赋值。这种申请大小new Mat(srcImage.size(), CvType.CV_8UC1)不复制的情况，会导致计算出错
        MatOfFloat ranges = new MatOfFloat(Constant.MIN_GRAY_VALUE, Constant.MAX_GRAY_VALUE);
        MatOfInt histSize = new MatOfInt(Constant.MAX_GRAY_VALUE);
        List<Mat> images = Arrays.asList(srcImage);
        Mat hist = new Mat();

        Imgproc.calcHist(Arrays.asList(srcImage), new MatOfInt(0), tempmask, hist, histSize, ranges);
        new ImageShow(HistToImage.getHistImage(hist), "src").run();

        Imgproc.calcHist(Arrays.asList(enhance), new MatOfInt(0), tempmask, hist, histSize, ranges);
        new ImageShow(HistToImage.getHistImage(hist), "enhance").run();
    }

    @Override
    public int saveConfigFile(String cfgName) {
        try {
            if (uicfg != null) {
                ObjectMapper mapper = new ObjectMapper();
                ObjectWriter writer = mapper.writerWithView(GameUI.class);
                String json = writer.writeValueAsString(uicfg);
                CommonFunction.writeFile(cfgName, json);
            }else{
                log.error("save json error.");
                return GamerEnum.CONFIG_FILE_EMPTY;
            }
        } catch (JsonProcessingException ex) {
            log.error("save json error.", ex);
        }
        return GamerEnum.SUCCESS;
    }

    /**
     * @param srcImg the targetImg to set
     */
    public void setSrcImg(String srcImg) {
        this.srcImg = srcImg;
    }
}
