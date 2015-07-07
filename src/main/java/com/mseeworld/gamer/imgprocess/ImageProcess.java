/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */

package com.mseeworld.gamer.imgprocess;

import org.opencv.core.Mat;

/**
 *
 * @author xy
 */
public interface ImageProcess {
    public int initConfigFile(String cfgName);
    public int getTargetRegin();
    public int preProcess();
    public int recognition();
    public int segmentation();
    public int saveConfigFile(String cfgName);
}
