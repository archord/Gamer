/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.mseeworld.gamer.ui;

import org.opencv.core.Rect;

/**
 *
 * @author xy
 */
public class FunctionRegin {

    private int id;
    private String name;
    private Rect regin;  //功能区域
    private String bgImage;
    private String fontName;
    private int fontSize;
    private float avgBackground;
    private float avgForeground;
    private float avgground;
    private String value;  //解析出来的结果

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id:").append(id).append(", ");
        sb.append("name:").append(name).append(", ");
        if (regin != null) {
            sb.append("regin:").append(regin.toString()).append(", ");
        } else {
            sb.append("regin:null, ");
        }
        sb.append("fontName:").append(fontName).append(", ");
        sb.append("fontSize:").append(fontSize).append(", ");
        sb.append("avgBackground:").append(avgBackground).append(", ");
        sb.append("avgForeground:").append(avgForeground).append(", ");
        sb.append("avgground:").append(avgground).append(", ");
        sb.append("value:").append(value).append("\n ");
        return sb.toString();
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the regin
     */
    public Rect getRegin() {
        return regin;
    }

    /**
     * @param regin the regin to set
     */
    public void setRegin(Rect regin) {
        this.regin = regin;
    }

    /**
     * @return the fontName
     */
    public String getFontName() {
        return fontName;
    }

    /**
     * @param fontName the fontName to set
     */
    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    /**
     * @return the fontSize
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * @param fontSize the fontSize to set
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * @return the avgBackground
     */
    public float getAvgBackground() {
        return avgBackground;
    }

    /**
     * @param avgBackground the avgBackground to set
     */
    public void setAvgBackground(float avgBackground) {
        this.avgBackground = avgBackground;
    }

    /**
     * @return the avgForeground
     */
    public float getAvgForeground() {
        return avgForeground;
    }

    /**
     * @param avgForeground the avgForeground to set
     */
    public void setAvgForeground(float avgForeground) {
        this.avgForeground = avgForeground;
    }

    /**
     * @return the avgground
     */
    public float getAvgground() {
        return avgground;
    }

    /**
     * @param avgground the avgground to set
     */
    public void setAvgground(float avgground) {
        this.avgground = avgground;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the bgImage
     */
    public String getBgImage() {
        return bgImage;
    }

    /**
     * @param bgImage the bgImage to set
     */
    public void setBgImage(String bgImage) {
        this.bgImage = bgImage;
    }
}
