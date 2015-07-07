/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.mseeworld.gamer.ai;

import com.mseeworld.gamer.config.GamerEnum;
import com.mseeworld.gamer.imgprocess.TaskProcess;

/**
 *
 * @author xy
 */
public class ProcessImage {

    public int processTaskImage(String imageDir) {

        String cfgfile = "src/main/resources/taskui.json";
        String cfgfile2 = "src/main/resources/taskui2.json";
        TaskProcess tp = new TaskProcess();
        tp.preProcess();
//        tp.initConfigFile(cfgfile);
//        tp.saveConfigFile(cfgfile2);

        return GamerEnum.SUCCESS;
    }
}
