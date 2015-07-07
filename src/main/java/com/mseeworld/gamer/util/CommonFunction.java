/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.mseeworld.gamer.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opencv.core.Mat;

/**
 *
 * @author xy
 */
public class CommonFunction {

    private static final Log log = LogFactory.getLog(CommonFunction.class);

    /**
     * 最大类间方差法计算阈值
     *
     * @param srcImage
     * @return 阈值
     */
    public static int otsu(Mat srcImage) {

        int i, j;
        int tmp;

        double u0, u1, w0, w1, u, uk;

        double cov;
        double maxcov = 0.0;
        int maxthread = 0;

        int hst[] = new int[Constant.MAX_GRAY_VALUE];
        double pro_hst[] = new double[Constant.MAX_GRAY_VALUE];

        int height = srcImage.cols();
        int width = srcImage.rows();

        //统计每个灰度的数量
        for (i = 0; i < width; i++) {
            for (j = 0; j < height; j++) {
                tmp = (int) srcImage.get(i, j)[0];
                hst[tmp]++;
            }
        }

        //计算每个灰度级占图像中的概率
        for (i = Constant.MIN_GRAY_VALUE; i < Constant.MAX_GRAY_VALUE; i++) {
            pro_hst[i] = (double) hst[i] / (double) (width * height);
        }

        //计算平均灰度值
        u = 0.0;
        for (i = Constant.MIN_GRAY_VALUE; i < Constant.MAX_GRAY_VALUE; i++) {
            u += i * pro_hst[i];
        }

        double det = 0.0;
        for (i = Constant.MIN_GRAY_VALUE; i < Constant.MAX_GRAY_VALUE; i++) {
            det += (i - u) * (i - u) * pro_hst[i];
        }

        //统计前景和背景的平均灰度值，并计算类间方差
        for (i = Constant.MIN_GRAY_VALUE; i < Constant.MAX_GRAY_VALUE; i++) {

            w0 = 0.0;
            w1 = 0.0;
            u0 = 0.0;
            u1 = 0.0;
            uk = 0.0;
            for (j = Constant.MIN_GRAY_VALUE; j < i; j++) {

                uk += j * pro_hst[j];
                w0 += pro_hst[j];

            }

            u0 = uk / w0;
            w1 = 1 - w0;
            u1 = (u - uk) / (1 - w0);
            //计算类间方差
            cov = w0 * w1 * (u1 - u0) * (u1 - u0);

            if (cov > maxcov) {
                maxcov = cov;
                maxthread = i;
            }
        }
        return maxthread;
    }

    public static String readFileAsString(String path) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            log.error("can not open file: " + path, e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("close BufferedReader error. ", e);
                }
            }
        }
        return sb.toString();
    }

    public static void writeStringToFile(String path, String content) {
        BufferedWriter writer = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
            writer = new BufferedWriter(outputStreamWriter);
            writer.append(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            log.error("can not open file: " + path, e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.error("close BufferedWriter error. ", e);
                }
            }
        }
    }
}
