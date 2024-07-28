package de.hsos.ui.spielerverwaltung;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hsos.MainActivity;
import de.hsos.R;
import de.hsos.daten.entitaeten.ESchiff;

/**
 * Die Klasse ist das View Fragment SpielerVerwaltung. Es ordnet das passende XML Layout zu
 * und bietet passende Methoden für die Verarbeitung von Daten für die Buttons an. Außerdem initialisiert es die Methoden der RecyclerView,
 * die für den RecyclerAdapter vom Spieler benötigt werden. Auch der Android Lifecylce wird hier berücksichtigt um Elemente der RecclerView passend darzustellen
 */
public class FragmentSpielerverwaltung extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerAdapterSpielerverwaltung recyclerAdapterSpielerverwaltung;
    private SpielerVerwaltung spielerVerwaltung;
    private SchiffVerwaltung schiffVerwaltung;
    private LinearLayoutManager layoutManager;
    private View view, activityRootView;
    private LinearSnapHelper linearSnapHelper;
    private EditText et_spielerNameEingabe;
    private Button buttonSpielerNameUeberschrift, buttonPfeilLinks, buttonPfeilRechts, buttonSpielerHinzufuegen, buttonSpielerEntfernen;
    private boolean linearLayoutBreiteAngepasst = false;
    public static int elementPositionRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_spielerverwaltung, container, false);

        this.spielerVerwaltung = new SpielerVerwaltung(MainActivity.mainActivity.getApplication());;
        this.schiffVerwaltung = new SchiffVerwaltung(MainActivity.mainActivity.getApplication());
        LiveData<List<ESchiff>> raumschiffe = schiffVerwaltung.getListeAlleVerfuegbarenSchiffe();
        this.recyclerAdapterSpielerverwaltung =
                new RecyclerAdapterSpielerverwaltung(MainActivity.mainActivity, raumschiffe.getValue());


        this.buttonSpielerNameUeberschrift = this.view.findViewById(R.id.ss_spielerverwaltung_spielernametext);
        this.et_spielerNameEingabe = this.view.findViewById(R.id.ss_spielerverwaltung_spielernameeingeben);
        this.buttonSpielerHinzufuegen = this.view.findViewById(R.id.ss_spielerverwaltung_bestaetigen);
        this.buttonSpielerEntfernen = this.view.findViewById(R.id.ss_spielerverwaltung_deleteAcc);

        this.activityRootView = getActivity().findViewById(android.R.id.content);
        this.activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);

        setRecyclerView();
        zeigeNurErstesRecyclerViewElement();
        setLiveDataObserverForRV();
        setLiveDataObserverForSpieler();
        buttonPfeilLinks();
        buttonPfeilRechts();
        createBtnSpielerHinzufuegen();
        loescheButtonSpieler();
        return view;
    }
    @Override
    public void onStop() {
        super.onStop();
        elementPositionRecyclerView = 0;
    }

    @Override
    public void onPause() {
        super.onPause();
        elementPositionRecyclerView = 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        elementPositionRecyclerView = 0;
    }

    private void setRecyclerView() {
        recyclerView = view.findViewById(R.id.ss_spielerverwaltung_recyclerview);
        recyclerView.setAdapter(this.recyclerAdapterSpielerverwaltung);

        this.layoutManager = this.scrollBarsDeaktivieren();
        recyclerView.setLayoutManager(this.layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.linearSnapHelper = new LinearSnapHelper();
        this.linearSnapHelper.attachToRecyclerView(recyclerView);
    }
    /**
     * Methode: Es soll immer nur 1 Element der RecyclerView angezeigt werden.
     * Der Benutzer soll manuell mit Buttons zwischen den Raumschiff Elementen der RecyclerView wechseln können
     */
    private void zeigeNurErstesRecyclerViewElement() {
        recyclerView.scrollToPosition(0);
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                if (recyclerView.getChildAdapterPosition(view) == 0) {
                    recyclerView.removeOnChildAttachStateChangeListener(this);
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            View itemView = layoutManager.findViewByPosition(0);
                            if (itemView != null) {
                                ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                                layoutParams.width = itemView.getMeasuredWidth();
                                recyclerView.setLayoutParams(layoutParams);
                            }
                        }
                    });
                }
            }
            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                //Hier muss nichts gemacht werden
            }
        });
    }

    /**
     * Methode: Beim öffnen des EditText für die Spieler Namen Eingabe sollen die Hinzufuegen und Löschen Buttons entfernt werden um ein Eingeben zu ermöglichen.
     * Dies wird mithilfe eines ViewTreeObserver gemacht, der basierend auf der Höhe der angezeigten Tastatur die Buttons solange versteckt.
     */
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
            if (heightDiff > dpToPx(200)) {
                buttonSpielerHinzufuegen.setVisibility(View.GONE);
                buttonSpielerEntfernen.setVisibility(View.GONE);

            } else {
                buttonSpielerHinzufuegen.setVisibility(View.VISIBLE);
                buttonSpielerEntfernen.setVisibility(View.VISIBLE);
            }
        }
    };
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        activityRootView.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
    }
    private void createBtnSpielerHinzufuegen(){
        this.buttonSpielerHinzufuegen.setOnClickListener(view -> {
            String spielerName = et_spielerNameEingabe.getText().toString();
            spielerName = spielerName.toUpperCase();
            if(spielerName != null || spielerName.isEmpty()){
                spielerVerwaltung.addSpieler(spielerName,
                            recyclerAdapterSpielerverwaltung.getAktuellesShiff());
                spielerVerwaltung.updateSpielerSchiff(spielerName, recyclerAdapterSpielerverwaltung.getAktuellesShiff());
                spielerVerwaltung.setAktuellesSchiff(recyclerAdapterSpielerverwaltung.getAktuellesShiff());
                spielerVerwaltung.setAktuellerSpieler(spielerName);
            }
        });
    }
    private void loescheButtonSpieler(){
        this.buttonSpielerEntfernen.setOnClickListener(view ->{
            String spielerName = et_spielerNameEingabe.getText().toString();
            spielerName = spielerName.toUpperCase();
            if(spielerName != null || spielerName.isEmpty()){
                spielerVerwaltung.loescheSpieler(spielerName);
            }
        });
    }
    private void setLiveDataObserverForRV() {
        this.schiffVerwaltung.getListeAlleVerfuegbarenSchiffe().observe(getViewLifecycleOwner(), raumschiff ->
                this.recyclerAdapterSpielerverwaltung.updateRaumschiffListe(raumschiff));
    }

    private void setLiveDataObserverForSpieler(){
        this.spielerVerwaltung.getListeSpieler().observe(getViewLifecycleOwner(), spieler ->
                this.recyclerAdapterSpielerverwaltung.updateSpielerListe(spieler));
    }
    /**
     * Methode: Damit ein manuelles Wechseln zwischen den Raumschiff Elementen der RecylcerView garantiert ist, muss die Scroll Funktion deaktiviert werden.
     */
    private LinearLayoutManager scrollBarsDeaktivieren(){
        return new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,
                false) {
            @Override
            public boolean canScrollVertically() {return false;}
            @Override
            public boolean canScrollHorizontally() {return false;}
        };
    }

    private void buttonPfeilLinks() {
        this.buttonPfeilLinks = view.findViewById(R.id.ss_spielerverwaltung_button_pfeil_links);
        this.buttonPfeilLinks.setOnClickListener(view -> {
            int aktuellePosRecyclerView = this.layoutManager.findFirstVisibleItemPosition();
            if (aktuellePosRecyclerView > 0) {
                recyclerView.scrollToPosition(aktuellePosRecyclerView - 1);
                elementPositionRecyclerView = aktuellePosRecyclerView -1;
            }
        });

    }

    private void buttonPfeilRechts() {
        this.buttonPfeilRechts = view.findViewById(R.id.ss_spielerverwaltung_button_pfeil_rechts);
        this.buttonPfeilRechts.setOnClickListener(view -> {
            int aktuellePosRecyclerView = this.layoutManager.findFirstVisibleItemPosition();
            if (aktuellePosRecyclerView < recyclerAdapterSpielerverwaltung.getItemCount() - 1) {
                recyclerView.scrollToPosition(aktuellePosRecyclerView + 1);
                elementPositionRecyclerView = aktuellePosRecyclerView + 1;
            }
        });
    }
}