package de.hsos.spiel.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import de.hsos.R;
import de.hsos.spiel.SpielActivity;

/**
 * HintergrundImg haelt das Hintergrundbild als Bitmap.
 */
public enum HintergrundImg {
    STERNE(R.drawable.hintergrund_sterne);
    private static int skalierungsfaktor = 1;
    private Bitmap sprite;
    HintergrundImg(int resID) {
        BitmapFactory.Options optionen = new BitmapFactory.Options();
        optionen.inScaled = false;
        sprite = BitmapFactory.decodeResource(SpielActivity.getSpielKontext().getResources(), resID, optionen);
    }

    static void setSkalierungsfaktor(int faktor) {
        skalierungsfaktor = faktor;
    }

    static int getSkalierungsfaktor() {
        return skalierungsfaktor;
    }

    public Bitmap getSprite() {
        return Bitmap.createScaledBitmap(sprite, sprite.getWidth()*skalierungsfaktor, sprite.getHeight()*skalierungsfaktor, false);
    }
}
