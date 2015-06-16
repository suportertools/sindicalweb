/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rtools.utilitarios;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageConverter {

    private static String EXTENSION;
    private static String INPUT_FILE;
    private static String OUTPUT_FILE;

    public ImageConverter() {
        ImageConverter.EXTENSION = "";
        ImageConverter.INPUT_FILE = "";
        ImageConverter.OUTPUT_FILE = "";
    }

    public static void execute() {
        BufferedImage bufferedImage;
        try {
            //read image file
            File file = new File(INPUT_FILE);
            bufferedImage = ImageIO.read(file);
            // create a blank, RGB, same width and height, and a white background
            BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                    bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
            // write to jpeg file
            String out = INPUT_FILE.substring(INPUT_FILE.indexOf("."), INPUT_FILE.length());
            if (EXTENSION.equals("png")) {
                OUTPUT_FILE = INPUT_FILE.replace(out + ".png", EXTENSION);
            } else if (EXTENSION.equals("jpg")) {
                OUTPUT_FILE = INPUT_FILE.replace(out + ".jpg", EXTENSION);
            } else if (EXTENSION.equals("jpeg")) {
                OUTPUT_FILE = INPUT_FILE.replace(out + ".jpeg", EXTENSION);
            } else if (EXTENSION.equals("gif")) {
                OUTPUT_FILE = INPUT_FILE.replace(out + ".gif", EXTENSION);
            }
            ImageIO.write(newBufferedImage, EXTENSION, new File(OUTPUT_FILE));
        } catch (IOException e) {
        }

    }

    public static String getEXTENSION() {
        return EXTENSION;
    }

    public static void setEXTENSION(String aEXTENSION) {
        EXTENSION = aEXTENSION;
    }

    public static String getINPUT_FILE() {
        return INPUT_FILE;
    }

    public static void setINPUT_FILE(String aINPUT_FILE) {
        INPUT_FILE = aINPUT_FILE;
    }

    public static String getOUTPUT_FILE() {
        return OUTPUT_FILE;
    }

    public static void setOUTPUT_FILE(String aOUTPUT_FILE) {
        OUTPUT_FILE = aOUTPUT_FILE;
    }
}
