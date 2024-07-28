package de.hsos.spiel.ui.controller;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Eine Custom Button KLasse, diese hat eine Hitbox und ein Bitmap Sprite
 * Klasse aus vorherigem Projekt (Minesweeper) uebernommen.
 */
public class CustomButton {
    private final RectF hitbox;
    private Bitmap bitmap;
    boolean isGedrueckt;
    private boolean gedrueckt;

    public CustomButton(float links, float oben, float breite, float hoehe) {
        hitbox = new RectF(links, oben, breite + links, hoehe + oben);
    }

    public CustomButton(float links, float oben, Bitmap bitmap) {
        setBitmap(bitmap);
        hitbox = new RectF(links, oben, bitmap.getWidth() + links, bitmap.getHeight() + oben);
    }

    public final RectF getHitbox() {
        return hitbox;
    }

    public final boolean isGedrueckt() {
        return gedrueckt;
    }

    public final void setGedrueckt(final boolean gedrueckt) {
        this.gedrueckt = gedrueckt;
    }

    public final void setBitmap(final Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public void drawFilter(Canvas c) {
        Paint paint = new Paint();
        paint.setColor(Color.argb(128, 0, 0, 0));
        c.drawRect(hitbox, paint);
    }

    public void draw(Canvas c) {
        c.drawBitmap(bitmap, hitbox.left, hitbox.top, null);
        if (gedrueckt) {
            drawFilter(c);
        }
    }

    public boolean beinhaltet(float x, float y) {
        return hitbox.contains(x,y);
    }
}
