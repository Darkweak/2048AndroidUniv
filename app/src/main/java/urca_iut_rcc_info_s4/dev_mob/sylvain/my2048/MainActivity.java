package urca_iut_rcc_info_s4.dev_mob.sylvain.my2048;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    TextView[][] box = new TextView[4][4];
    private Game2048 game;
    private TextView score;
    private TextView lastP;
    private RatingBar bestT;
    private boolean hasWon;
    private int bestTileScore;

    private float x1,x2,y1,y2;
    static final int MIN_DISTANCE = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int[][] boxId = new int[4][4];
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                boxId[i][j] = parseInt(i + "" + j);
            }
        }

        OrientationEventListener mOrientationListener = new OrientationEventListener(this,
                SensorManager.SENSOR_DELAY_NORMAL) {

            @Override
            public void onOrientationChanged(int orientation) {
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    View slo = findViewById(R.id.scoresLO);
                    View glo = findViewById(R.id.globalLO);
                    View clo = findViewById(R.id.controlLO);
                    View blo = findViewById(R.id.boardLO);

                    blo.setLayoutParams(new LinearLayout.LayoutParams(glo.getMeasuredWidth()/2, glo.getMeasuredHeight()));

                    slo.setLayoutParams(new LinearLayout.LayoutParams(glo.getMeasuredWidth()/2, (int)(glo.getMeasuredHeight()*0.2)));
                    clo.setLayoutParams(new LinearLayout.LayoutParams(glo.getMeasuredWidth()/2, (int)(glo.getMeasuredHeight()*0.8)));
                    
                }
            }
        };

        if (mOrientationListener.canDetectOrientation() == true) {
            mOrientationListener.enable();
        } else {
            mOrientationListener.disable();
        }
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){

                int my_dynamic_id = getResources().getIdentifier(("box" + boxId[i][j]), "id", getPackageName());
                if(i < 1)
                    my_dynamic_id = getResources().getIdentifier(("box0" + boxId[i][j]), "id", getPackageName());


                this.box[i][j] = (findViewById(my_dynamic_id));


                if(i < 1)
                    this.box[i][j].setText(("lc=0"+boxId[i][j]));
                else
                    this.box[i][j].setText(("lc="+boxId[i][j]));
            }
        }
        this.game = new Game2048();
        this.bestT = findViewById(R.id.bestTRB);
        this.lastP = findViewById(R.id.lastPTV);
        this.score = findViewById(R.id.scoreTV);

        findViewById(R.id.buttonT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.tryMove(1);
            }
        });
        findViewById(R.id.buttonB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.tryMove(3);
            }
        });
        findViewById(R.id.buttonL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.tryMove(0);
            }
        });
        findViewById(R.id.buttonR).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.tryMove(2);
            }
        });

        //this.game.initTest();
        this.read_datas(this);
        this.update();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(this.game.getHastLost()){
            if(!this.game.hasMovementAvailable()){
                return true;
            }
        }
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                float deltaX = x2 - x1;
                float deltaY = y2 - y1;
                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    if(deltaX > 0)
                        tryMove(2);
                    else
                        tryMove(0);
                }
                else if(Math.abs(deltaY) > MIN_DISTANCE){
                    if(deltaY > 0)
                        tryMove(3);
                    else
                        tryMove(1);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_new) {
            this.game = new Game2048();
            Toast.makeText(MainActivity.this, R.string.new_game, Toast.LENGTH_LONG).show();
            findViewById(R.id.buttonT).setEnabled(true);
            findViewById(R.id.buttonB).setEnabled(true);
            findViewById(R.id.buttonL).setEnabled(true);
            findViewById(R.id.buttonR).setEnabled(true);
            this.game.init();
            this.update();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWindowFocusChanged(boolean focus) {
        super.onWindowFocusChanged(focus);
        View slo = this.findViewById(R.id.scoresLO);
        View glo = this.findViewById(R.id.globalLO);
        View clo = this.findViewById(R.id.controlLO);
        View blo = this.findViewById(R.id.boardLO);

        blo.setLayoutParams(new LinearLayout.LayoutParams(blo.getMeasuredWidth(), blo.getMeasuredWidth()));

        int generalValueRest = glo.getMeasuredHeight() - blo.getMeasuredWidth();
        slo.setLayoutParams(new LinearLayout.LayoutParams(slo.getMeasuredWidth(), (int)(generalValueRest*0.2)));
        clo.setLayoutParams(new LinearLayout.LayoutParams(clo.getMeasuredWidth(), (int)(generalValueRest*0.8)));


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Toast.makeText(MainActivity.this, "Changed", Toast.LENGTH_SHORT).show();
    }

    public void update()
    {
        if(this.game.getHastLost()){
            if(!this.game.hasMovementAvailable()){
                Toast.makeText(MainActivity.this, R.string.lost, Toast.LENGTH_LONG).show();
                findViewById(R.id.buttonT).setEnabled(false);
                findViewById(R.id.buttonB).setEnabled(false);
                findViewById(R.id.buttonL).setEnabled(false);
                findViewById(R.id.buttonR).setEnabled(false);
            }
        }

        String values = "";

        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                this.box[i][j].setText(this.game.getTile(i,j).toString());
                int racine = this.game.getTile(i,j).r;
                if(!hasWon && racine == 11){
                    this.hasWon = true;
                    Toast.makeText(MainActivity.this, R.string.win, Toast.LENGTH_LONG).show();
                }

                if(racine > this.bestTileScore)
                    this.bestTileScore = racine;

                String name = "col" + racine;

                if(racine < 10)
                    name = "col0" + racine;
                try{
                    this.box[i][j].setBackgroundColor(getResources().getColor(R.color.class.getField(name).getInt(null)));
                    if(this.game.getTile(i,j).isNew()){
                        this.box[i][j].setTextColor(getResources().getColor(R.color.class.getField("col18").getInt(null)));
                    }
                    else if(this.game.getTile(i,j).value() < 16){
                        this.box[i][j].setTextColor(getResources().getColor(R.color.class.getField("col19").getInt(null)));
                    }
                    else{
                        this.box[i][j].setTextColor(getResources().getColor(R.color.class.getField("col20").getInt(null)));
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                values += this.game.getTile(i,j).getRank() + ",";
            }

            values += "\r\n";

        }

        this.write_to_file(values, this);
        this.bestT.setRating(this.bestTileScore);
        this.score.setText("" + this.game.getScore());
        this.lastP.setText(this.game.getLastP());
    }

    public void tryMove(int direction){
        if (direction == 0){
            this.game.move(false, false);
            this.update();
        }
        else if (direction == 1){
            this.game.move(false, true);
            this.update();
        }
        else if (direction == 2){
            this.game.move(true, false);
            this.update();
        }
        else if (direction == 3){
            this.game.move(true, true);
            this.update();
        }
    }

    private void write_to_file(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("save.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void read_datas(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("save.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                int i = 0;

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    String[] datas = receiveString.split(",");
                    for(int j = 0; j < 4; j++){
                        Log.i("datas ", datas[j]);
                        this.game.getTile(i,j).r = parseInt(datas[j]);
                    }
                    i++;
                }

                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }
}
