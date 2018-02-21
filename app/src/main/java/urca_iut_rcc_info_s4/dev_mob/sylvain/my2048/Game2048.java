package urca_iut_rcc_info_s4.dev_mob.sylvain.my2048;

import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by sylvain on 12/02/18.
 */

public class Game2048 {

    private Tile[][] board = new Tile[4][4];

    private int score = 0;
    private int bestR = 0;
    private String lastP = "";
    private Random rand;
    private int nbv;
    private boolean hasLost;

    public int getScore() {
        return score;
    }

    public boolean getHastLost() {
        return hasLost;
    }

    public int getBestR() {
        return bestR;
    }

    public String getLastP() {
        return lastP;
    }

    public static class Tile{
        public int flag;
        public int r;

        private static int[] pow2 = new int[18];

        public Tile()
        {
            this.flag = -1;
            for (int i = 0; i < Tile.pow2.length; i++){
                Tile.pow2[i] = ((int)Math.pow(2, i));
            }
            this.pow2[0] = 0;
        }

        private void set(int rk, int fl){
            this.flag = fl;
            this.r = rk;
        }

        public int getRank()
        {
            return this.r;
        }

        public int value()
        {
            return Tile.pow2[r];
        }

        public boolean isNew()
        {
            return (this.flag == -1);
        }

        public boolean isFusion()
        {
            return (this.flag == 1);
        }

        @Override
        public String toString()
        {
            if(this.r == 0){
                return "";
            }
            else{
                return "" + this.value();
            }
        }
    }

    public Game2048()
    {
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                this.board[i][j] = new Tile();
            }
        }
    }

    public Tile getTile(int l, int c)
    {
        return this.board[l][c];
    }

    public void init()
    {
        this.nbv = 16;
        this.score = 0;
        this.lastP = "";
        this.bestR = 0;

        this.addTile();
        this.addTile();
    }

    public void initTest()
    {
        this.board[0][0].r = 1;
        this.board[0][1].r = 3;
        this.board[0][3].r = 1;
        this.board[1][1].r = 1;
        this.board[1][2].r = 1;
        this.board[2][0].r = 1;
        this.board[2][1].r = 1;
        this.board[2][2].r = 1;
        this.board[2][3].r = 1;
        this.board[3][0].r = 2;
        this.board[3][1].r = 1;
        this.board[3][2].r = 2;
        this.board[3][3].r = 2;
    }

    public boolean addTile()
    {

        boolean res = false;
        int freeCounting = 0;
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                if(this.board[i][j].getRank() == 0) {
                    freeCounting++;
                }
            }
        }

        this.rand = new Random();
        if(freeCounting != 0)
            freeCounting = this.rand.nextInt(freeCounting);
        int rank = this.rand.nextInt(100);

        int placingTile = 0;
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                if(this.board[i][j].getRank() == 0){
                    if(placingTile == freeCounting){
                        if (rank > 90){
                            this.board[i][j].set(2, -1);
                            res = true;
                        }
                        else{
                            this.board[i][j].set(1, -1);
                            res = true;
                        }
                    }
                    placingTile++;
                }
            }
        }

        return res;

    }

    public void move(boolean croiss, boolean vert)
    {
        ArrayList<Integer> pile = new ArrayList<Integer>(4);
        this.score = 0;

        for (int i = 0; i < 4; i++){
            pile.clear();

            for (int clearing = 0; clearing < 4; clearing++){
                pile.add(0);
            }

            int counting = 0;
            for (int j = 0; j < 4; j++){
                if(this.getTile(j, i, croiss, vert).getRank() != 0){

                    if(j < 3 && this.getTile(j, i, croiss, vert).getRank() == this.getTile(j+1, i, croiss, vert).getRank()){
                        pile.set(counting, this.getTile(j, i, croiss, vert).getRank()+1);
                        this.getTile(j+1, i, croiss, vert).set(0, 0);
                    }
                    else if(j < 2 && this.getTile(j, i, croiss, vert).getRank() == this.getTile(j+2, i, croiss, vert).getRank() && this.getTile(j+1, i, croiss, vert).getRank() == 0){
                        pile.set(counting, this.getTile(j, i, croiss, vert).getRank()+1);
                        this.getTile(j+2, i, croiss, vert).set(0, 0);
                    }
                    else if(j < 1 && this.getTile(j, i, croiss, vert).getRank() == this.getTile(j+3, i, croiss, vert).getRank() && this.getTile(j+2, i, croiss, vert).getRank() == 0){
                        pile.set(counting, this.getTile(j, i, croiss, vert).getRank()+1);
                        this.getTile(j+3, i, croiss, vert).set(0, 0);
                    }
                    else{
                        pile.set(counting, this.getTile(j, i, croiss, vert).getRank());
                    }
                    counting++;
                }
            }

            for (int index = 0; index < 4; index++){
                this.getTile(index, i, croiss, vert).set(pile.get(index), 0);
                if (this.getTile(index, i, croiss, vert).value() != 0){
                    this.score += this.getTile(index, i, croiss, vert).value();
                    //this.lastP += "" + this.getTile(index, i, croiss, vert).value() + "+"; Must fix overriding text
                }
            }
        }

        this.hasLost = !(this.addTile());
    }

    public Tile getTile(int lc, int i, boolean croiss, boolean vert)
    {
        if(!vert){

            if(croiss){
                return this.board[i][3-lc];
            }
            else{
                return this.board[i][lc];
            }

        }
        else{

            if(croiss){
                return this.board[3-lc][i];
            }
            else {
                return this.board[lc][i];
            }
        }
    }

    public boolean hasMovementAvailable()
    {
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                if((i < 3 && this.getTile(i, j).getRank() == this.getTile(i+1, j).getRank()) || (j < 3 && this.getTile(i, j).getRank() == this.getTile(i, j+1).getRank()))
                    return true;
            }
        }
        return false;
    }

}
