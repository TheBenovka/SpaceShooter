package de.hsos.ui.highscore;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hsos.R;
import de.hsos.daten.entitaeten.ESchiff;
import de.hsos.daten.entitaeten.ESpiel;
import de.hsos.daten.entitaeten.ESpieler;
/**
 * Die Klasse ist das View Fragment für den Highscore. Es ordnet das passende XML Layout zu
 * und bietet passende Methoden für die Verarbeitung von Daten für die Buttons an. Außerdem initialisiert es die Methoden der RecyclerView,
 * die für den RecyclerAdapter vom Highscore benötigt werden. Es hält dabei die LiveData Listen von allen Entitäten(Spieler, Spiel, Schiff) damit im RecyclerAdapter
 * sämtlicher Zugriff auf Daten wie die BildNr des SChiffes etc moeglich ist
 */
public class FragmentHighscore extends Fragment {

    private RecyclerView recyclerView;
    private HighscoreSpielerverwaltung highscoreSpielerVerwaltung;
    private RecyclerAdapterHighscore recyclerAdapterHighscore;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view =  inflater.inflate(R.layout.fragment_highscore_tabelle, container, false);

        this.highscoreSpielerVerwaltung = new HighscoreSpielerverwaltung(getActivity().getApplication());
        LiveData<List<ESpiel>> spielListe = highscoreSpielerVerwaltung.getListe100Spiele();

        LiveData<List<ESpieler>> eSpielerListe = highscoreSpielerVerwaltung.getListeEspieler();
        LiveData<List<ESchiff>> eSchiffListe = highscoreSpielerVerwaltung.getlisteEschiffe();
        this.recyclerAdapterHighscore = new RecyclerAdapterHighscore(getContext(),
                spielListe.getValue(), eSpielerListe.getValue(), eSchiffListe.getValue());

        setRecyclerView();
        setLiveDataObserverForSpieleHighScore();
        setLiveDataObserverForESpielerHighScore();
        setLiveDataObserverForESchiffeHighScore();
        return this.view;
    }

    private void setRecyclerView() {
        recyclerView = view.findViewById(R.id.ss_highsore_recyclerview);
        recyclerView.setAdapter(this.recyclerAdapterHighscore);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setLiveDataObserverForSpieleHighScore() {
        this.highscoreSpielerVerwaltung.getListe100Spiele().observe(getViewLifecycleOwner(), spiel ->
                this.recyclerAdapterHighscore.updateSpielListe(spiel));
    }
    private void setLiveDataObserverForESpielerHighScore() {
        this.highscoreSpielerVerwaltung.getListeEspieler().observe(getViewLifecycleOwner(), spieler ->
                this.recyclerAdapterHighscore.updateEspielerListe(spieler));
    }
    private void setLiveDataObserverForESchiffeHighScore() {
        this.highscoreSpielerVerwaltung.getlisteEschiffe().observe(getViewLifecycleOwner(), schiffe ->
                this.recyclerAdapterHighscore.updateEschiffeListe(schiffe));
    }
}
