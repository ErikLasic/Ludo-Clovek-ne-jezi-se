package game.ludo.common;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Preferences;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.sun.tools.javac.util.Pair;

import java.util.ArrayList;

import game.ludo.CellState;
import game.ludo.LudoGame;

public class GameManager {

    public static final GameManager INSTANCE = new GameManager();

    private static final String INIT_COLOR_MOVE = "initColor";

    private final Preferences PREFS;
    private CellState initColor = CellState.RED;

    private ArrayList<Pair<String, Integer>> scores;

    private GameManager() {
        PREFS = Gdx.app.getPreferences(LudoGame.class.getSimpleName());
        String moveName = PREFS.getString(INIT_COLOR_MOVE, CellState.RED.name());
    }

    public CellState getInitColor() {
        return initColor;
    }

    public void setInitColor(CellState move) {
        initColor = move;

        PREFS.putString(INIT_COLOR_MOVE, move.name());
        PREFS.flush();
    }
}
