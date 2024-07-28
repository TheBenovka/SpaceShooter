package de.hsos.spiel.ui.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import de.hsos.R;
import de.hsos.spiel.SpielActivity;

/**
 * Enum fuer die Controller Button Sprites.
 */
enum ButtonImg {
    PFEIL_LINKS(R.drawable.btn_pfeil_links),
    PFEIL_RECHTS(R.drawable.btn_pfeil_rechts),
    SCHIESSEN(R.drawable.btn_shiessen);
    private static int skalierungsfaktor = 1;
    private BitmapFactory.Options optionen = new BitmapFactory.Options();
    private Bitmap sprite;
    ButtonImg(int resID) {
        sprite = BitmapFactory.decodeResource(SpielActivity.getSpielKontext().getResources(), resID, optionen);
    }

    static void setSkalierungsfaktor(int faktor) {
        skalierungsfaktor = faktor;
    }

    static int getSkalierungsfaktor() {
        return skalierungsfaktor;
    }

    Bitmap getSprite() {
        return Bitmap.createScaledBitmap(sprite, sprite.getWidth()*skalierungsfaktor, sprite.getHeight()*skalierungsfaktor, false);
    }
}
