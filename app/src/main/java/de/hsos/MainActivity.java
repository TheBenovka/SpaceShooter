package de.hsos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import de.hsos.daten.SpaceShooterRepository;
import de.hsos.ui.FragmentHauptmenue;
import de.hsos.ui.einstellungen.BGMManager;

/**
 * Die MainActivity sorgt dafür dass mithilfe von View Fragments zwischen den einzelnen Fragmenten gewechselt wird.
 * Zudem gibt es die Android Lifecycle Methoden um die Musik je nach Zustand passend zu steuern
 */

public class MainActivity extends AppCompatActivity {
    private FragmentHauptmenue fragmentHauptmenü = new FragmentHauptmenue();
    private static String spieler;
    public static MainActivity mainActivity;
    private BGMManager BGMManagerInstanz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SpaceShooterRepository spr = new SpaceShooterRepository(getApplication());

        if (savedInstanceState == null) {
            if (getIntent().getStringExtra("LOAD_FRAGMENT") != null && getIntent().getStringExtra("LOAD_FRAGMENT").equals("HAUPTMENU")) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_activitymain_container, this.fragmentHauptmenü)
                        .commit();
            } else {
                // Standardverhalten, wenn kein Extra gesetzt ist
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_activitymain_container, new FragmentHauptmenue())
                        .commit();
            }
        }
        BGMManagerInstanz = BGMManager.getInstanz(this, R.raw.background_music8bit);
        initSpielerSchiffe();
    }

    public void initSpielerSchiffe() {
        SpaceShooterRepository repo = new SpaceShooterRepository(getApplication());
        repo.addSchiff("Darkgrey4", R.drawable.schiff_darkgrey4);
        repo.addSchiff("Blue3", R.drawable.schiff_blue3);
        repo.addSchiff("Green1", R.drawable.schiff_green1);
    }

    protected void onPause() {
        super.onPause();
        BGMManagerInstanz.pauseBackgroundSong();
    }

    protected void onResume() {
        super.onResume();
        this.BGMManagerInstanz = BGMManager.getInstanz(this, R.raw.background_music8bit);
        BGMManagerInstanz.starteBackgroundSongWennChecked(getBaseContext());
    }

    protected void onDestroy() {
        super.onDestroy();
        BGMManagerInstanz.stoppeBackgroundSong();
    }

    /**
     * Methode: Wenn die App in den onWindowFocusChanged Zustand geht, also die "recent apps"
     * geöffnet werden dann soll die Musik pausiert werden
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) BGMManagerInstanz.pauseBackgroundSong(); //recent apps geoffnet
        else BGMManagerInstanz.starteBackgroundSongWennChecked(getBaseContext());
    }

    public static String getSpieler() {
        return spieler;
    }

    public static void setSpieler(String neuerSpieler) {
        spieler = neuerSpieler;
    }
}