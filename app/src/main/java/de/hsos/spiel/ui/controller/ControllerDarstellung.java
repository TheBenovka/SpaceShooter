package de.hsos.spiel.ui.controller;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Die Controller Darstellung definiert den Controller, also alle benoetigte Button und deren
 * Layout und Design. In diesem Fall Links, Rechts und ein Schuss Button.
 */
public class ControllerDarstellung {
    private CustomButton btnPfeilLinks;
    private CustomButton btnPfeilRechts;
    private CustomButton btnSchiessen;
    private RectF hitbox;
    private final float padding;

    public ControllerDarstellung(float links, float oben, float rechts, float unten) {
        this.hitbox = new RectF(links, oben, rechts, unten);
        this.padding = hitbox.width()*0.06f;
        setButtons();
    }

    private void setButtons() {
        setSkalierungsfaktor();
        float bildbreite = ButtonImg.SCHIESSEN.getSprite().getWidth();
        btnPfeilLinks = new CustomButton(hitbox.left+padding, hitbox.top, ButtonImg.PFEIL_LINKS.getSprite());
        btnPfeilRechts = new CustomButton(btnPfeilLinks.getHitbox().right+padding, hitbox.top, ButtonImg.PFEIL_RECHTS.getSprite());
        btnSchiessen = new CustomButton(hitbox.right-padding-bildbreite, hitbox.top, ButtonImg.SCHIESSEN.getSprite());
    }

    private void setSkalierungsfaktor() {
        float bildbreite = ButtonImg.SCHIESSEN.getSprite().getWidth();
        for (int i = 1; isGenuegendHoeheFuerBtn(bildbreite, i)
                && isGenuegendBreiteFuerDreiBtn(bildbreite, i); i++) {
            ButtonImg.setSkalierungsfaktor(i++);
        }
    }

    private boolean isGenuegendHoeheFuerBtn(float bildbreite, int i) {
        return bildbreite * i < hitbox.height() - 2 * padding;
    }

    private boolean isGenuegendBreiteFuerDreiBtn(float bildbreite, int i) {
        return bildbreite * i * 3 < hitbox.width() - 2 * padding;
    }

    public void draw(Canvas canvas) {
        btnPfeilLinks.draw(canvas);
        btnPfeilRechts.draw(canvas);
        btnSchiessen.draw(canvas);
    }

    public void setBtnPfeilLinksGedrueckt(boolean isGedrueckt) {
        this.btnPfeilLinks.setGedrueckt(isGedrueckt);
    }

    public void setBtnPfeilRechtsGedrueckt(boolean isGedrueckt) {
        this.btnPfeilRechts.setGedrueckt(isGedrueckt);
    }

    public void setBtnSchiessenGedrueckt(boolean isGedrueckt) {
        this.btnSchiessen.setGedrueckt(isGedrueckt);
    }

    public boolean beinhaltetInLinkenPfeil(float x, float y) {
        return btnPfeilLinks.beinhaltet(x,y);
    }
    public boolean beinhaltetInRechtenPfeil(float x, float y) {
        return btnPfeilRechts.beinhaltet(x,y);
    }
    public boolean beinhaltetInSchieesen(float x, float y) {
        return btnSchiessen.beinhaltet(x,y);
    }
    public boolean beinhaltet(float x, float y) {
        return hitbox.contains(x,y);
    }
}
