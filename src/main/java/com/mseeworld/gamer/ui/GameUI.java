/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.mseeworld.gamer.ui;

import java.util.List;
import org.opencv.core.Rect;

/**
 *
 * @author xy
 */
public class GameUI {

    protected int id;
    protected String configFile;
    protected String name;
    protected String templatePath;
    protected String labelImage;
    protected String bgImage;
    private Rect labelRegin;
    protected Rect winRegin;
    protected List<FunctionRegin> subRegins;

    protected float avgBackground;
    protected float avgForeground;
    protected float avgground;

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
     * @return the configFile
     */
    public String getConfigFile() {
        return configFile;
    }

    /**
     * @param configFile the configFile to set
     */
    public void setConfigFile(String configFile) {
        this.configFile = configFile;
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
     * @return the labelImage
     */
    public String getLabelImage() {
        return labelImage;
    }

    /**
     * @param labelImage the labelImage to set
     */
    public void setLabelImage(String labelImage) {
        this.labelImage = labelImage;
    }

    /**
     * @return the winRegin
     */
    public Rect getWinRegin() {
        return winRegin;
    }

    /**
     * @param winRegin the winRegin to set
     */
    public void setWinRegin(Rect winRegin) {
        this.winRegin = winRegin;
    }

    /**
     * @return the subRegins
     */
    public List<FunctionRegin> getSubRegins() {
        return subRegins;
    }

    /**
     * @param subRegins the subRegins to set
     */
    public void setSubRegins(List<FunctionRegin> subRegins) {
        this.subRegins = subRegins;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id:").append(id).append(", ");
        sb.append("name:").append(name).append(", ");
        sb.append("configFile:").append(configFile).append(", ");
        sb.append("labelImage:").append(labelImage).append(", ");
        sb.append("winRegin:").append(winRegin.toString()).append(", ");
        sb.append("avgForeground:").append(avgForeground).append(", ");
        sb.append("avgBackground:").append(avgBackground).append(", ");
        sb.append("avgground:").append(avgground).append("\n");
        for (FunctionRegin sub : subRegins) {
            sb.append("\t").append(sub.toString());
        }
        return sb.toString();
    }

    /**
     * @return the templatePath
     */
    public String getTemplatePath() {
        return templatePath;
    }

    /**
     * @param templatePath the templatePath to set
     */
    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
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

    /**
     * @return the labelRegin
     */
    public Rect getLabelRegin() {
        return labelRegin;
    }

    /**
     * @param labelRegin the labelRegin to set
     */
    public void setLabelRegin(Rect labelRegin) {
        this.labelRegin = labelRegin;
    }
}
