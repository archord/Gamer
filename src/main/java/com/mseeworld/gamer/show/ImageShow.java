package com.mseeworld.gamer.show;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

public class ImageShow implements Runnable {

    final int minWidth = 200;
    final int minHieght = 150;

    Mat mat;
    String title;

    public ImageShow(Mat mat, String title) {
        this.mat = mat;
        this.title = title;
    }

    public void shwoMat() {

        BufferedImage bufImage = getBufferedImage(mat);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(title);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);

        // Inserts the image icon
        ImageIcon image1 = new ImageIcon(bufImage);
        int width = image1.getIconWidth() + 30;
        int height = image1.getIconHeight() + 50;
        width = width < minWidth ? minWidth : width;
        height = height < minHieght ? minHieght : height;
        frame.setSize(width, height);
        // Draw the Image data into the BufferedImage
        JLabel label1 = new JLabel(image1, JLabel.CENTER);
        frame.getContentPane().add(label1);

        frame.validate();
        frame.setVisible(true);
    }

    public BufferedImage getBufferedImage(Mat mat) {

        BufferedImage bufImage;
        MatOfByte mob = new MatOfByte();
        Highgui.imencode(".png", mat, mob);
        //convert the "matrix of bytes" into a byte array
        byte[] byteArray = mob.toArray();
        bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufImage;
    }

    @Override
    public void run() {
        shwoMat();
    }
}
