/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */

package com.mseeworld.gamer.imgprocess;

import com.mseeworld.gamer.config.GamerEnum;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.Rect;

/**
 *
 * @author xy
 */
public class TaskProcessTest {
    
    public TaskProcessTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of initConfigFile method, of class TaskProcess.
     */
    @Test
    public void testInitConfigFile() {
        System.out.println("initConfigFile");
        String cfgName = "src/main/resources/taskui.json";
        TaskProcess instance = new TaskProcess();
        int expResult = GamerEnum.SUCCESS;
        int result = instance.initConfigFile(cfgName);
        assertEquals(expResult, result);
        //fail("The test case is a prototype.");
    }

    /**
     * Test of saveConfigFile method, of class TaskProcess.
     */
    @Test
    public void testSaveConfigFile() {
        System.out.println("saveConfigFile");
        String cfgName = "src/main/resources/taskui.json";
        String cfgName2 = "src/main/resources/taskui2.json";
        int expResult = GamerEnum.SUCCESS;
        TaskProcess instance = new TaskProcess();
        int result = instance.initConfigFile(cfgName);
        assertEquals(expResult, result);
        
        result = instance.saveConfigFile(cfgName2);
        assertEquals(expResult, result);
    }
    
}
