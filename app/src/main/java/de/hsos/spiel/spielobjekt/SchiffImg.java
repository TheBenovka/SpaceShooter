package de.hsos.spiel.spielobjekt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import de.hsos.R;
import de.hsos.spiel.SpielActivity;

/**
 * Die Klasse war ursprünglich ein Enum, jedoch ist eine Klasse vorteilhafter.
 * Damit man leichter dynamisch ein Sprite erstellen kann,
 * was mit der DB Anbindung und Speicherung der Schiffe zsm. haengt.
 */
public class SchiffImg {
    private BitmapFactory.Options optionen = new BitmapFactory.Options();
    private Bitmap spriteSpieler;
    private Bitmap spriteGegner;
    private Bitmap vertikalerStossSpieler[] = new Bitmap[4];
    private Bitmap vertikalerStossGegner[] = new Bitmap[4];

    public Bitmap[] getVertikalerStossSpieler() {
        return vertikalerStossSpieler;
    }

    public Bitmap[] getVertikalerStossGegner() {
        return vertikalerStossGegner;
    }

    public Bitmap getVertikalerStossSpieler(int i) {
        return vertikalerStossSpieler[i];
    }

    public Bitmap getVertikalerStossGegner(int i) {
        return vertikalerStossGegner[i];
    }
    public static int getSkalierungsfaktor() {
        return skalierungsfaktor;
    }

    private static final int skalierungsfaktor = 5;
    public SchiffImg(int resID) {
        optionen.inScaled = false;
        spriteSpieler = BitmapFactory.decodeResource(SpielActivity.getSpielKontext().getResources(), resID, optionen);
        Matrix matrix = new Matrix();
        matrix.postRotate(180);
        spriteGegner = Bitmap.createBitmap(spriteSpieler,0,0, spriteSpieler.getWidth(), spriteSpieler.getHeight(),matrix, true);
        setVertikalerStoss();
    }

    private void setVertikalerStoss() {
        vertikalerStossSpieler[0] =  BitmapFactory.decodeResource(SpielActivity.getSpielKontext().getResources(), R.drawable.schiff_vertikaler_stoss1, optionen);
        vertikalerStossSpieler[1] =  BitmapFactory.decodeResource(SpielActivity.getSpielKontext().getResources(), R.drawable.schiff_vertikaler_stoss2, optionen);
        vertikalerStossSpieler[2] =  BitmapFactory.decodeResource(SpielActivity.getSpielKontext().getResources(), R.drawable.schiff_vertikaler_stoss3, optionen);
        vertikalerStossSpieler[3] =  BitmapFactory.decodeResource(SpielActivity.getSpielKontext().getResources(), R.drawable.schiff_vertikaler_stoss4, optionen);
        Matrix matrix = new Matrix();
        matrix.postRotate(180);
        for (int i = 0; i < 4; ++i) {
            vertikalerStossSpieler[i] = Bitmap.createScaledBitmap(vertikalerStossSpieler[i],
                    vertikalerStossSpieler[i].getWidth()*skalierungsfaktor,
                    vertikalerStossSpieler[i].getHeight()*skalierungsfaktor,
                    false);
            vertikalerStossGegner[i] = Bitmap.createBitmap(vertikalerStossSpieler[i],
                    0,0,
                    vertikalerStossSpieler[i].getWidth(),
                    vertikalerStossSpieler[i].getHeight(),
                    matrix,
                    true);
        }
    }

    public Bitmap getSpriteSpieler() {
        return Bitmap.createScaledBitmap(spriteSpieler, spriteSpieler.getWidth()*skalierungsfaktor, spriteSpieler.getHeight()*skalierungsfaktor, false);
    }
    public Bitmap getSpriteGegner() {
        return Bitmap.createScaledBitmap(spriteGegner, spriteGegner.getWidth()*skalierungsfaktor, spriteGegner.getHeight()*skalierungsfaktor, false);
    }
}
