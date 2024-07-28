package de.hsos.spiel.spielobjekt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import de.hsos.R;
import de.hsos.spiel.SpielActivity;

/**
 * GeschossImg haelt alle Geschoss Bilder, welche um weitere Sprites erweitert werden kann.
 */
public enum GeschossImg {
    FEUERBALL(R.drawable.spritesheet_geschoss);
    private Bitmap spritesSpieler[] = new Bitmap[4];
    private Bitmap spritesGegner[] = new Bitmap[4];
    private Bitmap spritesheetSpieler;
    private Bitmap spritesheetGegner;
    private static final int skalierungsfaktor = 5;
    private static final int BIT_GROESSE = 9;

    GeschossImg(int resID) {
        BitmapFactory.Options optionen = new BitmapFactory.Options();
        Matrix matrix = new Matrix();
        matrix.postRotate(270);
        optionen.inScaled = false;
        Bitmap spritesheetScaled = BitmapFactory.decodeResource(SpielActivity.getSpielKontext().getResources(), resID, optionen);
        float b = getBreite();
        spritesheetSpieler = Bitmap.createBitmap(spritesheetScaled,0,0, spritesheetScaled.getWidth(), spritesheetScaled.getHeight(),matrix, true);
        matrix = new Matrix();
        matrix.postRotate(90);
        spritesheetGegner = Bitmap.createBitmap(spritesheetScaled,0,0, spritesheetScaled.getWidth(), spritesheetScaled.getHeight(),matrix, true);
        initialisiereBitmapArray();
    }

    private void initialisiereBitmapArray() {
        for (int i = 0; i < spritesSpieler.length; ++i) {
            spritesSpieler[spritesSpieler.length-i-1] = Bitmap.createBitmap(spritesheetSpieler
                    , 0
                    , BIT_GROESSE * i
                    , BIT_GROESSE, BIT_GROESSE);
            spritesGegner[i] = Bitmap.createBitmap(spritesheetGegner
                    , 0
                    , BIT_GROESSE * i
                    , BIT_GROESSE, BIT_GROESSE);
        }
    }

    public static float getBreite() {
        return BIT_GROESSE * skalierungsfaktor;
    }

    public Bitmap getSpriteSpieler(int i) {
        return Bitmap.createScaledBitmap(spritesSpieler[i], spritesSpieler[i].getWidth() * skalierungsfaktor, spritesSpieler[i].getHeight() * skalierungsfaktor, false);
    }

    public Bitmap getSpriteGegner(int i) {
        return Bitmap.createScaledBitmap(spritesGegner[i], spritesGegner[i].getWidth() * skalierungsfaktor, spritesGegner[i].getHeight() * skalierungsfaktor, false);
    }
}
