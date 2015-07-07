/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.mseeworld.gamer.ai;

import com.mseeworld.gamer.config.GamerEnum;
import com.mseeworld.gamer.imgprocess.TaskProcess;
import org.opencv.core.Rect;

/**
 *
 * @author xy
 */
public class ProcessImage {

    public int processTaskImage(String imageDir) {

        String cfgfile = "src/main/resources/taskui.json";
        String srcImg = "src/main/image/task/yj5.png";
        TaskProcess tp = new TaskProcess();
        tp.initConfigFile(cfgfile);
        tp.setSrcImg(srcImg);
        tp.getTargetRegin();

        return GamerEnum.SUCCESS;
    }
}
