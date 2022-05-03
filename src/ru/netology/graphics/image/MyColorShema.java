package ru.netology.graphics.image;

public class MyColorShema implements TextColorSchema{
    private static final char[] CHARS = {'#', '$', '@', '%', '*', '+', '-', '"'};
    private static  final int COLORSTEP = 32;

    @Override
    public char convert(int color) {
        return CHARS[color/COLORSTEP];
    }
}
