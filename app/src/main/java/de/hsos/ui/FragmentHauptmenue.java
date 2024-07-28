package de.hsos.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import de.hsos.R;
import de.hsos.daten.SpaceShooterRepository;
import de.hsos.spiel.SpielActivity;
import de.hsos.ui.einstellungen.FragmentEinstellungen;
import de.hsos.ui.highscore.FragmentHighscore;
import de.hsos.ui.spielerverwaltung.FragmentSpielerverwaltung;

/**
 * Die Klasse ist das Haupt Verwaltungs Fragment, das die einzelnen View Fragmente zuordnet und
 * dafür sorgt dass zwischen den einzelnen Fragmenten gewechselt wird
 */
public class FragmentHauptmenue extends Fragment {

    private FragmentSpielerverwaltung fragmentSpielerverwaltung = new FragmentSpielerverwaltung();
    private FragmentHighscore fragmentHighscore = new FragmentHighscore();
    private FragmentEinstellungen fragmentEinstellungen = new FragmentEinstellungen();
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_hauptmenue, container, false);

        view.findViewById(R.id.ss_einstellungen_startgame).setOnClickListener(v -> {
            starteSpiel();
        });
        view.findViewById(R.id.buttonSpielerVerwaltung).setOnClickListener(v -> navigateToFragment(this.fragmentSpielerverwaltung));
        view.findViewById(R.id.buttonHighscores).setOnClickListener(v -> navigateToFragment(this.fragmentHighscore));

        //view.findViewById(R.id.buttonStatistiken).setOnClickListener(v -> navigateToFragment(new Test4Fragment()));
        view.findViewById(R.id.buttonEinstellungen).setOnClickListener(v -> navigateToFragment(this.fragmentEinstellungen));

        return view;
    }

    private void starteSpiel() {
        if (SpaceShooterRepository.getAktuellerSpielername() == null || SpaceShooterRepository.getAktuellerSpielername().isEmpty()) {
            Toast.makeText(getContext(), "Erstelle oder wähle einen Spielernamen!", Toast.LENGTH_SHORT).show();
        } else {
            Intent spielIntent = new Intent(getContext(), SpielActivity.class);
            startActivity(spielIntent);
        }
    }

    private void navigateToFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_activitymain_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
