package de.hsos.spiel;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;

import de.hsos.MainActivity;
import de.hsos.R;
import de.hsos.daten.SpaceShooterRepository;
import de.hsos.spiel.dialog.SpielEndeDialog;
import de.hsos.spiel.helfer.IOnSpielEnde;
import de.hsos.spiel.helfer.Spielwerte;
import de.hsos.ui.einstellungen.BGMManager;

/**
 * Die SpielActivity erstellt ein neues SpielPanel, mit diesem wird das Spiel, SpielSchleife etc. erstellt.
 * Des weiteren wird die Methode onSpielBeendet vom Interface ISpielEventListener aufgerufen. Sobald
 * das Spiel beendet ist.
 */
public class SpielActivity extends AppCompatActivity implements IOnSpielEnde {
    private static Context spielKontext;
    private SpielPanel spielPanel;
    private SpaceShooterRepository repository;
    private SpielActivity spielActivity;
    private SpielEndeDialog spielEndeDialog;
    private String spielerName;
    private BGMManager BGMManagerInstanz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spielKontext = this;
        spielActivity = this;
        repository = new SpaceShooterRepository(this.getApplication());
        spielerName = MainActivity.getSpieler();
        BGMManagerInstanz = BGMManager.getInstanz(this, R.raw.background_music8bit);
        BGMManagerInstanz.starteBackgroundSongWennChecked(getSpielKontext());
        initSpiel();
    }

    public void initSpiel() {
        spielPanel = new SpielPanel(spielKontext, this);
        setContentView(spielPanel);
    }

    public static Context getSpielKontext() {
        return spielKontext;
    }

    public static int getScreenBreite() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHoehe() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public void onSpielBeendet(Spielwerte spielwerte) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spielEndeDialog = new SpielEndeDialog(spielKontext, repository, spielActivity, spielwerte, spielerName);
                spielEndeDialog.show();
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (spielPanel != null) {
            spielPanel.pausiereSpiel();
        }
        BGMManagerInstanz.pauseBackgroundSong();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (spielPanel != null) {
            spielPanel.setzeSpielfort();
        }
        this.BGMManagerInstanz = BGMManager.getInstanz(this, R.raw.background_music8bit);
        BGMManagerInstanz.starteBackgroundSongWennChecked(getBaseContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (spielPanel != null) {
            spielPanel.stoppeSpiel();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            if (spielPanel != null) {
                spielPanel.pausiereSpiel();
            }
            BGMManagerInstanz.pauseBackgroundSong(); // recent apps geöffnet
        } else {
            if (spielPanel != null) {
                spielPanel.setzeSpielfort();
            }
            BGMManagerInstanz.starteBackgroundSongWennChecked(getBaseContext());
        }
    }
}
