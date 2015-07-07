/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.mseeworld.gamer.world;

import com.mseeworld.gamer.ui.TaskUI;
import java.util.Date;
import java.util.List;

/**
 *
 * @author xy
 */
public class Task {

    int id;
    TaskType type;
    String name;
    String npc; //人物抽象
    String place;  //地点抽象
    int posX;
    int posY;
    Date startTime;
    Date endTime;
    String description;
    String consume; //需要消耗什么东西
    int awardMoney;
    int awardExperance;
    int awardReputation; //声望
    
    List<Task> subTasks;
}

