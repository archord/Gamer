/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.mseeworld.gamer;

import com.mseeworld.gamer.ai.ProcessImage;
import com.mseeworld.gamer.ui.GameUI;
import com.mseeworld.gamer.ui.TaskUI;
import static java.lang.System.exit;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import static org.opencv.highgui.Highgui.CV_LOAD_IMAGE_GRAYSCALE;
import static org.opencv.highgui.Highgui.imread;
import static org.opencv.highgui.Highgui.imwrite;
import static org.opencv.imgproc.Imgproc.threshold;

/**
 *
 * @author xy
 */
public class Gamer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
                
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String imageDir = "E:\\program_src\\image\\test2";
        
        ProcessImage pi = new ProcessImage();
        pi.processTaskImage(imageDir);
    }
}
