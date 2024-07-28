package de.hsos.spiel.ui;

import android.graphics.RectF;

/**
 * Hintergrund verwaltet das Hintergrundbild des Spiels.
 */
public class Hintergrund {
    public RectF getHitbox() {
        return hitbox;
    }
    private RectF hitbox;
    private final float geschwindigkeit = 30f;

    public Hintergrund(float mitteX, float mitteY) {
        float bildBreite = HintergrundImg.STERNE.getSprite().getWidth();
        float bildHoehe = HintergrundImg.STERNE.getSprite().getHeight();
        float links = mitteX - bildBreite/2f;
        float rechts = mitteX + bildBreite/2f;
        float oben = mitteY - bildHoehe/2f;
        float unten = mitteY + bildHoehe/2f;
        hitbox = new RectF(links, oben, rechts, unten);
    }
    public void bewegen(float delta) {
        hitbox.offset(0,geschwindigkeit*delta);
    }
}
