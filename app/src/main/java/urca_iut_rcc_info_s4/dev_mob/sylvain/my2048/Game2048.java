package urca_iut_rcc_info_s4.dev_mob.sylvain.my2048;

import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
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

    public int getScore() {
        return score;
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
            this.pow2[0] = 0;
            for (int i = 0; i < Tile.pow2.length; i++){
                Tile.pow2[i] = ((int)Math.pow(2, i));
            }
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

    public void addTile()
    {

        int freeCounting = 0;
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                if(this.board[i][j].getRank() == 0) {
                    freeCounting++;
                }
            }
        }

        this.rand = new Random();
        freeCounting = this.rand.nextInt(freeCounting);
        int rank = this.rand.nextInt(100);

        int placingTile = 0;
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                if(this.board[i][j].getRank() == 0){
                    if(placingTile == freeCounting){
                        if (rank > 90)
                            this.board[i][j].set(2, -1);
                        else
                            this.board[i][j].set(1, -1);
                    }
                    placingTile++;
                }
            }
        }

    }

    public void move(boolean croiss, boolean vert)
    {
        ArrayList<Integer> pile = new ArrayList<Integer>(4);

        for (int i = 0; i < 4; i++){
            pile.clear();

            for (int clearing = 0; clearing < 4; clearing++){
                pile.add(0);
            }

            int counting = 0;
            for (int j = 0; j < 4; j++){
                if(this.getTile(j, i, croiss, vert).getRank() != 0){
                    if(croiss)
                        pile.set(counting, this.getTile(j, i, croiss, vert).getRank());
                    else
                        pile.set(3-scounting, this.getTile(j, i, croiss, vert).getRank());
                    counting++;
                }
            }

            for (int index = 0; index < 4; index++){
                if(!croiss)
                    this.getTile(index, i, croiss, vert).set(pile.get(index), 0);
                else
                    this.getTile(index, i, croiss, vert).set(pile.get(3-index), 0);
            }
        }
    }

    public Tile getTile(int lc, int i, boolean croiss, boolean vert)
    {
        if(!vert){
            if (!croiss){
                return this.board[3-i][lc];
            }
            else{
                return this.board[i][lc];
            }
        }
        else {
            if (!croiss){
                return this.board[lc][3-i];
            }
            else{
                return this.board[lc][i];
            }
        }
    }

}
