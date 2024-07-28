package de.hsos.ui.einstellungen;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Random;

import de.hsos.R;
import de.hsos.spiel.sfx.SoundEffektManager;

/**
 * Die Klasse ist das View Fragment für die Einstellungen. Es ordnet das passende XML Layout zu
 * und initialisiert die checkBoxen für die Musik. Zudem hat es Methoden für die Checkboxen um zu pruefen ob
 */
public class FragmentEinstellungen extends Fragment {
    private View view;
    private CheckBox checkBoxMusik;
    private CheckBox checkBoxSFX;
    private BGMManager BGMManagerInstanz;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "EinstellungenPrefs";
    private static final String MUSIC_CHECKED_KEY = "musicChecked";
    private SoundEffektManager sfxManager;
    private static final String SFX_CHECKED_KEY = "sfxChecked";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view =  inflater.inflate(R.layout.fragment_einstellungen, container, false);
        this.checkBoxMusik = this.view.findViewById(R.id.ss_einstellungen_checkbox_music);
        this.checkBoxSFX = this.view.findViewById(R.id.ss_einstellungen_checkbox_soundeffekte);

        BGMManagerInstanz = BGMManager.getInstanz(getContext(), R.raw.background_music8bit);
        sfxManager = SoundEffektManager.getInstance(getContext());
        sharedPreferences = getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        boolean isChecked = sharedPreferences.getBoolean(MUSIC_CHECKED_KEY, false);
        this.checkBoxMusik.setChecked(isChecked);
        isChecked = sharedPreferences.getBoolean(SFX_CHECKED_KEY, false);
        this.checkBoxSFX.setChecked(isChecked);

        pruefeMusikBoxAngeklickt(this.getCheckBoxMusik());
        pruefeSFXBoxAngeklickt(this.getCheckBoxSFX());
        return this.view;
    }
    public CheckBox getCheckBoxMusik(){
        return this.checkBoxMusik;
    }
    public CheckBox getCheckBoxSFX(){
        return this.checkBoxSFX;
    }
    public void pruefeMusikBoxAngeklickt(CheckBox checkBox){
        if(checkBox != null){
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(MUSIC_CHECKED_KEY, isChecked);
                editor.apply();
                if (isChecked) {
                    BGMManagerInstanz.starteBackgroundSong();
                } else {
                    BGMManagerInstanz.pauseBackgroundSong();
                }
            });
        }
    }

    public void pruefeSFXBoxAngeklickt(CheckBox checkBox){
        if(checkBox != null){
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SFX_CHECKED_KEY, isChecked);
                editor.apply();
                if (isChecked) {
                    Random rnd = new Random();
                    int rndWert = rnd.nextInt(2);
                    if (rndWert == 0) {
                        sfxManager.spielSchussSound();
                    } else if (rndWert == 1) {
                        sfxManager.spielTrefferSound();
                    } else {
                        sfxManager.spielExplosionSound();
                    }
                }
            });
        }
    }
}
