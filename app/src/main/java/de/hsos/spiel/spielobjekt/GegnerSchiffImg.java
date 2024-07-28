package de.hsos.spiel.spielobjekt;

import android.graphics.Bitmap;

import de.hsos.R;
import de.hsos.spiel.spielobjekt.SchiffImg;

/**
 * SchiffImg besitzt alle Bilder der SChiffe als Bitmap.
 * Eine Alternative waere die bitmaps in die DB abzulegen.
 * Jedoch wuerde dies den Umfang sprengen,
 * da wir uns noch mehr Wissen aneignen muessen.
 * Dies wuerde den zeitrahmen, unserer Meinung nach uebersteigen.
 * TODO: Als DB umsetzen Falls die Zeit nach dem Testen ueber bleibt.
 */
public enum GegnerSchiffImg {
    BLUE5(new SchiffImg(R.drawable.schiff_blue05)),
    RED5(new SchiffImg(R.drawable.schiff_red5)),
    BLUE3(new SchiffImg(R.drawable.schiff_blue3)),
    GREEN1(new SchiffImg(R.drawable.schiff_green1)),
    DARKGREY4(new SchiffImg(R.drawable.schiff_darkgrey4));

    private SchiffImg schiffImg;
    GegnerSchiffImg(SchiffImg schiffImg) {
        this.schiffImg = schiffImg;
    }

    public SchiffImg getSchiffImg() {
        return schiffImg;
    }

    public Bitmap getSprite() {
        return this.schiffImg.getSpriteGegner();
    }


    public Bitmap[] getVertikalerStoss() {
        return this.schiffImg.getVertikalerStossGegner();
    }


    public Bitmap getVertikalerStoss(int i) {
        return this.schiffImg.getVertikalerStossGegner(i);
    }
}

