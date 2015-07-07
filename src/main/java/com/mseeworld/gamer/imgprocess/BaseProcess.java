/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */

package com.mseeworld.gamer.imgprocess;

import com.mseeworld.gamer.imgprocess.ImageProcess;
import com.mseeworld.gamer.ui.GameUI;
import org.opencv.core.Mat;

/**
 *
 * @author xy
 */
public class BaseProcess implements ImageProcess{

    private GameUI gameUI;
    protected String srcImg;
    protected String bgImg;
    protected String labelImg;
    protected Mat srcMat;
    protected Mat bgMat;
    protected Mat labelMat;
    protected Mat targetRegin;
    
    public BaseProcess(){
        
    }
    
    public BaseProcess(String srcImg){
        this.srcImg = srcImg;
    }

    @Override
    public int initConfigFile(String cfgName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getTargetRegin() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int preProcess() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int recognition() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int segmentation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int saveConfigFile(String cfgName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @param srcImg the targetImg to set
     */
    public void setSrcImg(String srcImg) {
        this.srcImg = srcImg;
    }
}
