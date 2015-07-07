/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */

package com.mseeworld.gamer.ai;

import com.mseeworld.gamer.world.Task;
import java.util.List;

/**
 * 任务调度，维护任务队列
 * 任务类型有：时间任务，顺序任务，重复任务，刷怪模式
 * 工作流程：
 * 1，分别扫描任务队列，获取当前最适合的任务
 * 2，开始执行任务，控制鼠标键盘动作
 * 3，每1秒截图一次，截图前会有对应的鼠标或键盘动作，比如打开任务界面，任务属性界面等。
 * 4，解析图片，判断任务执行状态（行走当中，NPC对话，停止不动等等）
 * 5，当前任务完成，跳转到1，继续下一个任务
 * @author xy
 */
public class Scheduler {
    
    List<Task> timeTask;
    List<Task> sequenceTask;
    List<Task> repeatTask;
    
    String ImagePath;
    String curImageName;
}
