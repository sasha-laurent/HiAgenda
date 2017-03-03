package com.sashalaurent.example.testapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private SpeechRecognizer speechRecognizer;
    private Intent intent;
    private TextToSpeech tts;
    private Button buttonListen;
    private EditText editText;
    private int nbMots = 5;

    public String action = "";
    public String description= "";
    public String heureDebut= "";

    private Date currentDate;
    private String currentDateText = "Today";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String jours[] = { "Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};
        String mois[] = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};

        currentDate = new Date();
        currentDateText = jours[currentDate.getDay()] + " " + currentDate.getDate() + " " + mois[currentDate.getMonth()] + " " + (1900 + currentDate.getYear());
        setTitle(currentDateText);

        final RecyclerView rv = (RecyclerView) findViewById(R.id.list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter adapter = new MyAdapter();
        rv.setAdapter(adapter);

        initVoiceRecognizer();
        tts = new TextToSpeech(this, this);
        FloatingActionButton speakButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        speakButton.setOnClickListener(new View.OnClickListener() {



            public String choiceMade = "";

            public void setChoiceMade(String choice){
                this.choiceMade = choice;
            }

            public String getChoiceMade(){
                return this.choiceMade;
            }

            @Override
            public void onClick(View v) {

                /*final CharSequence[] choices = {"Consulter", "Ajouter", "Editer", "Supprimer"};


                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Que voulez-vous faire?")
                        .setSingleChoiceItems(choices, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setChoiceMade((String) choices[which]);
                                Log.i("choice_made_is :", getChoiceMade());
                            }
                        })
                        .setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("final_choice_is :", getChoiceMade());
                                // TODO : faire un truc en fonction du choix final
                            }
                        })
                        .setIcon(android.R.drawable.ic_btn_speak_now)
                        .show();*/

                // TODO : ajouter la partie de Jordan ici
                startListening();
            }
        });

        JSONAsyncTask task = new JSONAsyncTask(adapter);
        task.execute("http://sashalaurent.com/test_event2.json");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent();
        switch (item.getItemId()){
            case R.id.add_event:
                myIntent = new Intent(MainActivity.this, NewEventActivity.class);
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                myIntent = new Intent(MainActivity.this, MainActivity.class);
                break;
        }

        startActivity(myIntent);
        return super.onOptionsItemSelected(item);
    }

    private void initVoiceRecognizer() {
        speechRecognizer = getSpeechRecognizer();
        intent = new  Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR");
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, nbMots);
    }

    public void startListening() {
        while (tts.isSpeaking()){

        }
        if (speechRecognizer!=null) {
            speechRecognizer.cancel();
        }
        speechRecognizer.startListening(intent);
    }

    private SpeechRecognizer getSpeechRecognizer(){
        if (speechRecognizer == null) {
            speechRecognizer =
                    SpeechRecognizer.createSpeechRecognizer(this);
            speechRecognizer.setRecognitionListener(new VoiceListener());
        }
        return speechRecognizer;
    }


    private void speakOut(String str) {
        tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.FRANCE);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    class VoiceListener implements RecognitionListener {
        String TAG = "TAG";

        public void onReadyForSpeech(Bundle params) {}
        public void onBeginningOfSpeech() {}
        public void onRmsChanged(float rmsdB) {}
        public void onBufferReceived(byte[] buffer) {}
        public void onEndOfSpeech() {
            Log.d(TAG,"onEndofSpeech");
        }
        public void onError(int error) {
            Log.v(TAG, "error "+ error);
        }

        public void onResults(Bundle results) {
            String str = new String();
            Log.v(TAG,"onResults"+ results);
            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            speechRecognizer.stopListening();
            speechRecognizer.cancel();
            //float [] confidence = results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);
            str = data.get(0).toString();
            //for (int i = 0; i < data.size(); i++) {
            //    Log.v(TAG,"result "+ data.get(i));
            //    str += data.get(i) + " (confidence : " + confidence[i]+")\n";
            //}
            analyseString(str);
        }

        public void onPartialResults(Bundle partialResults) {}
        public void onEvent(int eventType, Bundle params) {}

        public void analyseString(String str){
            switch (action)
            {
                case "":
                    if (str.toLowerCase().contains("ajout") || str.toLowerCase().contains("crée")){
                        action = "ajouter";
                        speakOut("Veuillez donner la description de votre événement");

                        startListening();}
                    if (str.toLowerCase().contains("supprim") || str.toLowerCase().contains("retir")){
                        action = "supprimer";
                        speakOut("Quelle évènement voulez vous supprimer ?");

                        startListening();}
                    if (str.toLowerCase().contains("modifi") || str.toLowerCase().contains("retir")){
                        action = "modifier";
                        speakOut("Quelle évènement voulez vous modifier ?");

                        startListening();}
                    if (str.toLowerCase().contains("consult")){
                        action = "consulter";
                        speakOut("Vous souhaitez accéder a l'événement de quelle heure ?");

                        startListening();}
                    break;
                case "ajouter":
                    switch (description)
                    {
                        case "":
                            description=str;

                            speakOut("A quel heure à lieu votre événement ?");
                            startListening();
                            break;
                        default:
                            switch (heureDebut)
                            {
                                case "":
                                    heureDebut = str;

                                    // ajouter evenement
                                    RecyclerView rv = (RecyclerView)findViewById(R.id.list);
                                    MyAdapter adapter1 = (MyAdapter)rv.getAdapter();

                                    int i = 0;
                                    while (i < heureDebut.length() && !Character.isDigit(heureDebut.charAt(i))) i++;
                                    int j = i;
                                    while (j < heureDebut.length() && Character.isDigit(heureDebut.charAt(j))) j++;
                                    heureDebut = heureDebut.substring(i,j);

                                    int position = Integer.parseInt(heureDebut);

                                    speakOut("Vous venez d'" + action + " votre " + description + " au calendrier. Votre événement aura lieu a "  + heureDebut +  " heure");

                                    adapter1.setCharacters(position, description);
                                    rv.getAdapter().notifyDataSetChanged();

                                    reinitilaiserEvenement();
                                    break;
                                default:
                                    reinitilaiserEvenement();
                            }


                    }
                    break;
                case "supprimer":
                    reinitilaiserEvenement();
                    break;
                case "modifier":
                    reinitilaiserEvenement();
                    break;
                case "consulter":
                    switch (heureDebut)
                    {
                        case "":
                            heureDebut=str;

                            RecyclerView rv = (RecyclerView)findViewById(R.id.list);
                            MyAdapter adapter1 = (MyAdapter)rv.getAdapter();

                            int i = 0;
                            while (i < heureDebut.length() && !Character.isDigit(heureDebut.charAt(i))) i++;
                            int j = i;
                            while (j < heureDebut.length() && Character.isDigit(heureDebut.charAt(j))) j++;
                            heureDebut = heureDebut.substring(i,j);
                            int position = Integer.parseInt(heureDebut);

                            description = adapter1.getDescription(position);
                            Log.i("consult_description", description);
                            if(description.equals("Pas d'événement")){
                                speakOut("Vous n'avez rien de prévu à " + heureDebut + " heures");
                            }
                            else{
                                speakOut("L'événement prévu à " + heureDebut + " heures est " + description);
                            }

                            reinitilaiserEvenement();

                        default:
                            reinitilaiserEvenement();

                    }
                    break;
                default:
                    reinitilaiserEvenement();
            }
        }
    }

    private void reinitilaiserEvenement() {
        action = "";
        description= "";
        heureDebut= "";
    }
}
