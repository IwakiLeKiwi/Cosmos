package fr.iwaki.cosmos;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class CustomFonts {

    private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = CustomFonts.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    private Font CustomFont(String path) {
        Font customFont = loadFont(path, 24f);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(customFont);
        return customFont;

    }

    private Font loadFont(String path, float size){
        try {
            Font myFont = Font.createFont(Font.TRUETYPE_FONT, getFileFromResourceAsStream(path));
            return myFont.deriveFont(Font.PLAIN, size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public Font kollektifFont;
    public Font kollektifBoldFont;
    public Font kollektifBoldItalicFont;
    public Font kollektifItalicFont;
    public Font minecraftiaFont;
    private static final String FONT_PATH_KOLLEKTIF = "fonts/Kollektif.ttf";
    private static final String FONT_PATH_KOLLEKTIF_BOLD = "fonts/Kollektif-Bold.ttf";
    private static final String FONT_PATH_KOLLEKTIF_BOLD_ITALIC = "fonts/Kollektif-Bold-Italic.ttf";
    private static final String FONT_PATH_KOLLEKTIF_ITALIC = "fonts/Kollektif-Italic.ttf";
    private static final String FONT_PATH_MINECRAFTIA = "fonts/Minecraftia.ttf";

    public void initFonts() {
        kollektifFont = CustomFont(FONT_PATH_KOLLEKTIF);
        kollektifBoldFont = CustomFont(FONT_PATH_KOLLEKTIF_BOLD);
        kollektifBoldItalicFont = CustomFont(FONT_PATH_KOLLEKTIF_BOLD_ITALIC);
        kollektifItalicFont = CustomFont(FONT_PATH_KOLLEKTIF_ITALIC);
        minecraftiaFont = CustomFont(FONT_PATH_MINECRAFTIA);
    }
}
