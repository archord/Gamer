/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */

package com.mseeworld.gamer.world;

/**
 *
 * @author xy
 */
public enum TaskType {
    
    TIME(1),
    SEQUENCE(2),
    REPEAT(3),
    SHUAGUAI(4);
    
    private final int value;
    
    private TaskType(int value){
        this.value = value;
    }
    
    public int getValue(){
        return this.value;
    }
}
