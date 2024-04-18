package com.usacamp.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

/**
 * Created by Administrator on 1/7/2019.
 */

public class QRGenerater {
    public static Bitmap QRGenerater(String str, int width , int height)
    {
        MultiFormatWriter gen = new MultiFormatWriter();
        Bitmap bitmap= null;
        try {

            BitMatrix bytemap = gen.encode(str, BarcodeFormat.QR_CODE, width, height);
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            int qrWidth = bytemap.getWidth();
            int qrHeight = bytemap.getHeight();
            int[] pixels = new int[width * height];

            for (int y = 0 ; y < qrWidth ; y++) {
                int offset = y * width;
                for (int x = 0; x < qrHeight; x++) {
                    //bitmap.setPixel(i, j, bytemap.get(i,j) ? Color.BLACK : Color.WHITE);
                    pixels[offset + x] = bytemap.get(x, y) ?
                            Color.BLACK : Color.WHITE;
                }
            }

            bitmap.setPixels(pixels, 0, qrWidth, 0, 0, qrWidth, qrHeight);
            //setting bitmap to image view

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static Bitmap mergeBitmaps(Bitmap overlay, Bitmap bitmap) {

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        Bitmap combined = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        canvas.drawBitmap(bitmap, new Matrix(), null);

        int centreX = (canvasWidth  - overlay.getWidth()) /2;
        int centreY = (canvasHeight - overlay.getHeight()) /2 ;
        canvas.drawBitmap(overlay, centreX, centreY, null);

        return combined;
    }
}
