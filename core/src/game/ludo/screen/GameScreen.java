package game.ludo.screen;

import static com.badlogic.gdx.math.MathUtils.random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.tools.javac.util.Pair;

import org.graalvm.compiler.phases.common.inlining.policy.GreedyInliningPolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import game.ludo.CellActor;
import game.ludo.CellState;
import game.ludo.LudoGame;
import game.ludo.assets.AssetDescriptors;
import game.ludo.assets.RegionNames;
import game.ludo.common.GameManager;
import game.ludo.config.GameConfig;

public class GameScreen extends ScreenAdapter {

    private final LudoGame game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Viewport hudViewport;

    private Stage gameplayStage;
    private Stage hudStage;

    private Skin skin;
    private TextureAtlas gameplayAtlas;

    private CellState move = GameManager.INSTANCE.getInitColor();
    private Image infoImage;
    //private Texture texture = new Texture(Gdx.files.internal("trans_img.png"));
    //private TextureRegion trans = new TextureRegion(texture, 20, 20, 50, 50);

    private TextureRegion trans = new TextureRegion(new Texture(Gdx.files.internal("trans_img.png")));

    public GameScreen(LudoGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);

        gameplayStage = new Stage(viewport, game.getBatch());
        hudStage = new Stage(hudViewport, game.getBatch());

        skin = assetManager.get(AssetDescriptors.UI_SKIN);
        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);

        gameplayStage.addActor(createGrid(15, 15, 4));
        hudStage.addActor(createInfo());
        hudStage.addActor(createMenuButton());

        Gdx.input.setInputProcessor(new InputMultiplexer(gameplayStage, hudStage));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(195 / 255f, 195 / 255f, 195 / 255f, 0f);

        // update
        gameplayStage.act(delta);
        hudStage.act(delta);

        // draw
        gameplayStage.draw();
        hudStage.draw();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        gameplayStage.dispose();
        hudStage.dispose();
    }

    private Actor createGrid(final int rows, final int columns, final float cellSize) {
        final Table table = new Table();
        TextureRegion backgroundRegion = gameplayAtlas.findRegion(RegionNames.BACKGROUND);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));
        table.setDebug(false);   // turn on all debug lines (table, cell, and widget)

        final Table grid = new Table();
        grid.defaults().size(cellSize);   // all cells will be the same size
        grid.setDebug(false);

        final TextureRegion red = gameplayAtlas.findRegion(RegionNames.RED_PLAYER);
        final TextureRegion green = gameplayAtlas.findRegion(RegionNames.GREEN_PLAYER);
        final TextureRegion blue = gameplayAtlas.findRegion(RegionNames.BLUE_PLAYER);
        final TextureRegion yellow = gameplayAtlas.findRegion(RegionNames.YELLOW_PLAYER);
        final TextureRegion dice_1 = gameplayAtlas.findRegion(RegionNames.DICE_ONE);
        final TextureRegion dice_2 = gameplayAtlas.findRegion(RegionNames.DICE_TWO);
        final TextureRegion dice_3 = gameplayAtlas.findRegion(RegionNames.DICE_THREE);
        final TextureRegion dice_4 = gameplayAtlas.findRegion(RegionNames.DICE_FOUR);
        final TextureRegion dice_5 = gameplayAtlas.findRegion(RegionNames.DICE_FIVE);
        final TextureRegion dice_6 = gameplayAtlas.findRegion(RegionNames.DICE_SIX);

        if (move == CellState.RED) {
            infoImage = new Image(red);
        } else if (move == CellState.GREEN) {
            infoImage = new Image(green);
        } else if (move == CellState.BLUE) {
            infoImage = new Image(blue);
        } else if (move == CellState.YELLOW) {
            infoImage = new Image(yellow);
        }

        final CellActor[][] board = new CellActor[rows][columns];

        final Map<Pair<Integer, Integer>,Integer> boardPoints = new HashMap<>();

        boardPoints.put(new Pair(6,1),1);
        boardPoints.put(new Pair(6,2), 2);
        boardPoints.put(new Pair(6,3), 3);
        boardPoints.put(new Pair(6,4), 4);
        boardPoints.put(new Pair(6,5), 5);
        boardPoints.put(new Pair(5,6), 6);
        boardPoints.put(new Pair(4,6), 7);
        boardPoints.put(new Pair(3,6), 8);
        boardPoints.put(new Pair(2,6), 9);
        boardPoints.put(new Pair(1,6), 10);
        boardPoints.put(new Pair(0,6), 11);
        boardPoints.put(new Pair(0,7), 12);
        boardPoints.put(new Pair(0,8), 13);
        boardPoints.put(new Pair(1,8), 14);
        boardPoints.put(new Pair(2,8), 15);
        boardPoints.put(new Pair(3,8), 16);
        boardPoints.put(new Pair(4,8), 17);
        boardPoints.put(new Pair(5,8), 18);
        boardPoints.put(new Pair(6,9), 19);
        boardPoints.put(new Pair(6,10), 20);
        boardPoints.put(new Pair(6,11), 21);
        boardPoints.put(new Pair(6,12), 22);
        boardPoints.put(new Pair(6,13), 23);
        boardPoints.put(new Pair(6,14), 24);
        boardPoints.put(new Pair(7,14), 25);
        boardPoints.put(new Pair(8,14), 26);
        boardPoints.put(new Pair(8,13), 27);
        boardPoints.put(new Pair(8,12), 28);
        boardPoints.put(new Pair(8,11), 29);
        boardPoints.put(new Pair(8,10), 30);
        boardPoints.put(new Pair(8,9), 31);
        boardPoints.put(new Pair(9,8), 32);
        boardPoints.put(new Pair(10,8), 33);
        boardPoints.put(new Pair(11,8), 34);
        boardPoints.put(new Pair(12,8), 35);
        boardPoints.put(new Pair(13,8), 36);
        boardPoints.put(new Pair(14,8), 37);
        boardPoints.put(new Pair(14,7), 38);
        boardPoints.put(new Pair(14,6), 39);
        boardPoints.put(new Pair(13,6), 40);
        boardPoints.put(new Pair(12,6), 41);
        boardPoints.put(new Pair(11,6), 42);
        boardPoints.put(new Pair(10,6), 43);
        boardPoints.put(new Pair(9,6), 44);
        boardPoints.put(new Pair(8,5), 45);
        boardPoints.put(new Pair(8,4), 46);
        boardPoints.put(new Pair(8,3), 47);
        boardPoints.put(new Pair(8,2), 48);
        boardPoints.put(new Pair(8,1), 49);
        boardPoints.put(new Pair(8,0), 50);
        boardPoints.put(new Pair(7,0), 51);
        boardPoints.put(new Pair(6,0), 0);


        final List<Pair<Integer, Integer>> redHome = new ArrayList<>();
        final List<Pair<Integer, Integer>> greenHome = new ArrayList<>();
        final List<Pair<Integer, Integer>> yellowHome = new ArrayList<>();
        final List<Pair<Integer, Integer>> blueHome = new ArrayList<>();

        redHome.add(new Pair(11,2));
        redHome.add(new Pair(11,3));
        redHome.add(new Pair(12,2));
        redHome.add(new Pair(12,3));


        greenHome.add(new Pair(2,2));
        greenHome.add( new Pair(2, 3));
        greenHome.add(new Pair(3, 3));
        greenHome.add(new Pair(3, 2));


        yellowHome.add(new Pair(2,11));
        yellowHome.add(new Pair(2, 12));
        yellowHome.add(new Pair(3, 11));
        yellowHome.add(new Pair(3, 12));

        blueHome.add(new Pair(11,11));
        blueHome.add(new Pair(11, 12));
        blueHome.add(new Pair(12, 11));
        blueHome.add(new Pair(12, 12));


        final int[] diceNumber = {0};
        final boolean[] canMove = {false};

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                board[row][column] = new CellActor();

                final Pair<Integer, Integer> currentField = new Pair<>(row, column);


                if (greenHome.contains(currentField)) {
                    board[row][column].setDrawable(green);
                    board[row][column].setState(CellState.GREEN);
                    board[row][column].addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            final CellActor clickedCell = (CellActor) event.getTarget();
                            if (canMove[0] && diceNumber[0] == 6 && move == CellState.GREEN) {
                                if (clickedCell.getState() == CellState.GREEN) {
                                    for (Pair<Integer, Integer> i : boardPoints.keySet()) {
                                        if (boardPoints.get(i) == 1) {
                                            if (board[i.fst][i.snd].getState() == CellState.GREEN) {
                                                break;
                                            } else if (board[i.fst][i.snd].getState() == CellState.EMPTY) {
                                                clickedCell.setDrawable(trans);
                                                clickedCell.setState(CellState.EMPTY);
                                                board[i.fst][i.snd].setDrawable(green);
                                                board[i.fst][i.snd].setState(CellState.GREEN);
                                                canMove[0] = false;
                                            } else {
                                                switch(board[i.fst][i.snd].getState()) {
                                                    case RED:
                                                        for (Pair<Integer, Integer> r : redHome) {
                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                board[r.fst][r.snd].setDrawable(red);
                                                                board[r.fst][r.snd].setState(CellState.RED);
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                    case YELLOW:
                                                        for (Pair<Integer, Integer> r : yellowHome) {
                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                board[r.fst][r.snd].setDrawable(yellow);
                                                                board[r.fst][r.snd].setState(CellState.YELLOW);
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                    case BLUE:
                                                        for (Pair<Integer, Integer> r : blueHome) {
                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                board[r.fst][r.snd].setDrawable(blue);
                                                                board[r.fst][r.snd].setState(CellState.BLUE);
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                }
                                                clickedCell.setDrawable(trans);
                                                clickedCell.setState(CellState.EMPTY);
                                                board[i.fst][i.snd].setDrawable(green);
                                                board[i.fst][i.snd].setState(CellState.GREEN);
                                                canMove[0] = false;
                                            }
                                            for (Pair<Integer, Integer> s : greenHome) {
                                                if (board[s.fst][s.snd].getState() == CellState.GREEN) {
                                                    return;
                                                }
                                            }
                                            game.setScreen(new MenuScreen(game));
                                        }
                                    }
                                }
                            }
                        }
                    });
                } else if(redHome.contains(currentField)) {
                    board[row][column].setDrawable(red);
                    board[row][column].setState(CellState.RED);
                    board[row][column].addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            final CellActor clickedCell = (CellActor) event.getTarget();
                            if (canMove[0] && diceNumber[0] == 6 && move == CellState.RED) {
                                if (clickedCell.getState() == CellState.RED) {
                                    for (Pair<Integer, Integer> i : boardPoints.keySet()) {
                                        if (boardPoints.get(i) == 40) {
                                            if (board[i.fst][i.snd].getState() == CellState.RED) {
                                                break;
                                            } else if (board[i.fst][i.snd].getState() == CellState.EMPTY) {
                                                clickedCell.setDrawable(trans);
                                                clickedCell.setState(CellState.EMPTY);
                                                board[i.fst][i.snd].setDrawable(red);
                                                board[i.fst][i.snd].setState(CellState.RED);
                                                canMove[0] = false;
                                            } else {
                                                switch(board[i.fst][i.snd].getState()) {
                                                    case GREEN:
                                                        for (Pair<Integer, Integer> r : greenHome) {
                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                board[r.fst][r.snd].setDrawable(green);
                                                                board[r.fst][r.snd].setState(CellState.GREEN);
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                    case YELLOW:
                                                        for (Pair<Integer, Integer> r : yellowHome) {
                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                board[r.fst][r.snd].setDrawable(yellow);
                                                                board[r.fst][r.snd].setState(CellState.YELLOW);
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                    case BLUE:
                                                        for (Pair<Integer, Integer> r : blueHome) {
                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                board[r.fst][r.snd].setDrawable(blue);
                                                                board[r.fst][r.snd].setState(CellState.BLUE);
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                }
                                                clickedCell.setDrawable(trans);
                                                clickedCell.setState(CellState.EMPTY);
                                                board[i.fst][i.snd].setDrawable(red);
                                                board[i.fst][i.snd].setState(CellState.RED);
                                                canMove[0] = false;
                                            }
                                            for (Pair<Integer, Integer> s : redHome) {
                                                if (board[s.fst][s.snd].getState() == CellState.RED) {
                                                    return;
                                                }
                                            }
                                            game.setScreen(new MenuScreen(game));
                                        }
                                    }
                                }
                            }
                        }
                    });
                } else if(yellowHome.contains(currentField)) {
                    board[row][column].setDrawable(yellow);
                    board[row][column].setState(CellState.YELLOW);
                    board[row][column].addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            final CellActor clickedCell = (CellActor) event.getTarget();
                            if (canMove[0] && diceNumber[0] == 6  && move == CellState.YELLOW) {
                                if (clickedCell.getState() == CellState.YELLOW) {
                                    for (Pair<Integer, Integer> i : boardPoints.keySet()) {
                                        if (boardPoints.get(i) == 14) {
                                            if (board[i.fst][i.snd].getState() == CellState.YELLOW) {
                                                break;
                                            } else if (board[i.fst][i.snd].getState() == CellState.EMPTY) {
                                                clickedCell.setDrawable(trans);
                                                clickedCell.setState(CellState.EMPTY);
                                                board[i.fst][i.snd].setDrawable(yellow);
                                                board[i.fst][i.snd].setState(CellState.YELLOW);
                                                canMove[0] = false;
                                            } else {
                                                switch(board[i.fst][i.snd].getState()) {
                                                    case RED:
                                                        for (Pair<Integer, Integer> r : redHome) {
                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                board[r.fst][r.snd].setDrawable(red);
                                                                board[r.fst][r.snd].setState(CellState.RED);
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                    case GREEN:
                                                        for (Pair<Integer, Integer> r : greenHome) {
                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                board[r.fst][r.snd].setDrawable(green);
                                                                board[r.fst][r.snd].setState(CellState.GREEN);
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                    case BLUE:
                                                        for (Pair<Integer, Integer> r : blueHome) {
                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                board[r.fst][r.snd].setDrawable(blue);
                                                                board[r.fst][r.snd].setState(CellState.BLUE);
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                }
                                                clickedCell.setDrawable(trans);
                                                clickedCell.setState(CellState.EMPTY);
                                                board[i.fst][i.snd].setDrawable(yellow);
                                                board[i.fst][i.snd].setState(CellState.YELLOW);
                                                canMove[0] = false;
                                            }
                                            for (Pair<Integer, Integer> s : yellowHome) {
                                                if (board[s.fst][s.snd].getState() == CellState.YELLOW) {
                                                    return;
                                                }
                                            }
                                            game.setScreen(new MenuScreen(game));
                                        }
                                    }
                                }
                            }
                        }
                    });
                } else if(blueHome.contains(currentField)) {
                    board[row][column].setDrawable(blue);
                    board[row][column].setState(CellState.BLUE);
                    board[row][column].addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            final CellActor clickedCell = (CellActor) event.getTarget();
                            if (canMove[0] && diceNumber[0] == 6 && move == CellState.BLUE) {
                                if (clickedCell.getState() == CellState.BLUE) {
                                    for (Pair<Integer, Integer> i : boardPoints.keySet()) {
                                        if (boardPoints.get(i) == 27) {
                                            if (board[i.fst][i.snd].getState() == CellState.BLUE) {
                                                break;
                                            } else if (board[i.fst][i.snd].getState() == CellState.EMPTY) {
                                                clickedCell.setDrawable(trans);
                                                clickedCell.setState(CellState.EMPTY);
                                                board[i.fst][i.snd].setDrawable(blue);
                                                board[i.fst][i.snd].setState(CellState.BLUE);
                                                canMove[0] = false;
                                            } else {
                                                switch(board[i.fst][i.snd].getState()) {
                                                    case RED:
                                                        for (Pair<Integer, Integer> r : redHome) {
                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                board[r.fst][r.snd].setDrawable(red);
                                                                board[r.fst][r.snd].setState(CellState.RED);
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                    case YELLOW:
                                                        for (Pair<Integer, Integer> r : yellowHome) {
                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                board[r.fst][r.snd].setDrawable(yellow);
                                                                board[r.fst][r.snd].setState(CellState.YELLOW);
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                    case GREEN:
                                                        for (Pair<Integer, Integer> r : greenHome) {
                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                board[r.fst][r.snd].setDrawable(green);
                                                                board[r.fst][r.snd].setState(CellState.GREEN);
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                }
                                                clickedCell.setDrawable(trans);
                                                clickedCell.setState(CellState.EMPTY);
                                                board[i.fst][i.snd].setDrawable(blue);
                                                board[i.fst][i.snd].setState(CellState.BLUE);
                                                canMove[0] = false;
                                            }
                                            for (Pair<Integer, Integer> s : blueHome) {
                                                if (board[s.fst][s.snd].getState() == CellState.BLUE) {
                                                    return;
                                                }
                                            }
                                            game.setScreen(new MenuScreen(game));
                                        }
                                    }
                                }
                            }
                        }
                    });
                } else if (boardPoints.containsKey(currentField)) {
                    board[row][column].addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            final CellActor clickedCell = (CellActor) event.getTarget();
                            switch (move) {
                                case RED:
                                    if (canMove[0]) {
                                        for (int row = 0; row < rows; row++) {
                                            for (int column = 0; column < columns; column++) {
                                                if (board[row][column] == clickedCell && clickedCell.getState() == CellState.RED) {
                                                    int currentNumber = boardPoints.get(new Pair(row, column));
                                                    for (Pair<Integer, Integer> i : boardPoints.keySet()) {
                                                        if (boardPoints.get(i) == ((currentNumber+diceNumber[0])%52)) {
                                                            if (board[i.fst][i.snd].getState() == CellState.EMPTY) {
                                                                clickedCell.setState(CellState.EMPTY);
                                                                clickedCell.setDrawable(trans);
                                                                board[i.fst][i.snd].setDrawable(red);
                                                                board[i.fst][i.snd].setState(CellState.RED);
                                                            } else {
                                                                switch(board[i.fst][i.snd].getState()) {
                                                                    case GREEN:
                                                                        for (Pair<Integer, Integer> r : greenHome) {
                                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                                board[r.fst][r.snd].setDrawable(green);
                                                                                board[r.fst][r.snd].setState(CellState.GREEN);
                                                                                break;
                                                                            }
                                                                        }
                                                                        break;
                                                                    case YELLOW:
                                                                        for (Pair<Integer, Integer> r : yellowHome) {
                                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                                board[r.fst][r.snd].setDrawable(yellow);
                                                                                board[r.fst][r.snd].setState(CellState.YELLOW);
                                                                                break;
                                                                            }
                                                                        }
                                                                        break;
                                                                    case BLUE:
                                                                        for (Pair<Integer, Integer> r : blueHome) {
                                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                                board[r.fst][r.snd].setDrawable(blue);
                                                                                board[r.fst][r.snd].setState(CellState.BLUE);
                                                                                break;
                                                                            }
                                                                        }
                                                                        break;
                                                                    case RED:
                                                                        for (Pair<Integer, Integer> r : redHome) {
                                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                                board[r.fst][r.snd].setDrawable(red);
                                                                                board[r.fst][r.snd].setState(CellState.RED);
                                                                                break;
                                                                            }
                                                                        }
                                                                        break;
                                                                }
                                                                clickedCell.setDrawable(trans);
                                                                clickedCell.setState(CellState.EMPTY);
                                                                board[i.fst][i.snd].setDrawable(red);
                                                                board[i.fst][i.snd].setState(CellState.RED);
                                                            }
                                                            canMove[0] = false;
                                                            if (diceNumber[0] != 6) {
                                                                infoImage.setDrawable(new TextureRegionDrawable(green));
                                                                move = CellState.GREEN;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    break;
                                case GREEN:
                                    if (canMove[0]) {
                                        for (int row = 0; row < rows; row++) {
                                            for (int column = 0; column < columns; column++) {
                                                if (board[row][column] == clickedCell && clickedCell.getState() == CellState.GREEN) {
                                                    int currentNumber = boardPoints.get(new Pair(row, column));
                                                    for (Pair<Integer, Integer> i : boardPoints.keySet()) {
                                                        if (boardPoints.get(i) == ((currentNumber+diceNumber[0])%52)) {
                                                            if (board[i.fst][i.snd].getState() == CellState.EMPTY) {
                                                                clickedCell.setState(CellState.EMPTY);
                                                                clickedCell.setDrawable(trans);
                                                                board[i.fst][i.snd].setDrawable(green);
                                                                board[i.fst][i.snd].setState(CellState.GREEN);
                                                            } else {
                                                                switch(board[i.fst][i.snd].getState()) {
                                                                    case RED:
                                                                        for (Pair<Integer, Integer> r : redHome) {
                                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                                board[r.fst][r.snd].setDrawable(red);
                                                                                board[r.fst][r.snd].setState(CellState.RED);
                                                                                break;
                                                                            }
                                                                        }
                                                                        break;
                                                                    case YELLOW:
                                                                        for (Pair<Integer, Integer> r : yellowHome) {
                                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                                board[r.fst][r.snd].setDrawable(yellow);
                                                                                board[r.fst][r.snd].setState(CellState.YELLOW);
                                                                                break;
                                                                            }
                                                                        }
                                                                        break;
                                                                    case BLUE:
                                                                        for (Pair<Integer, Integer> r : blueHome) {
                                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                                board[r.fst][r.snd].setDrawable(blue);
                                                                                board[r.fst][r.snd].setState(CellState.BLUE);
                                                                                break;
                                                                            }
                                                                        }
                                                                        break;
                                                                    case GREEN:
                                                                        for (Pair<Integer, Integer> r : greenHome) {
                                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                                board[r.fst][r.snd].setDrawable(green);
                                                                                board[r.fst][r.snd].setState(CellState.GREEN);
                                                                                break;
                                                                            }
                                                                        }
                                                                        break;
                                                                }
                                                                clickedCell.setDrawable(trans);
                                                                clickedCell.setState(CellState.EMPTY);
                                                                board[i.fst][i.snd].setDrawable(green);
                                                                board[i.fst][i.snd].setState(CellState.GREEN);
                                                            }
                                                            canMove[0] = false;
                                                            if (diceNumber[0] != 6) {
                                                                infoImage.setDrawable(new TextureRegionDrawable(yellow));
                                                                move = CellState.YELLOW;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    break;
                                case BLUE:
                                    if (canMove[0]) {
                                        for (int row = 0; row < rows; row++) {
                                            for (int column = 0; column < columns; column++) {
                                                if (board[row][column] == clickedCell && clickedCell.getState() == CellState.BLUE) {
                                                    int currentNumber = boardPoints.get(new Pair(row, column));
                                                    for (Pair<Integer, Integer> i : boardPoints.keySet()) {
                                                        if (boardPoints.get(i) == ((currentNumber+diceNumber[0])%52)) {
                                                            if (board[i.fst][i.snd].getState() == CellState.EMPTY) {
                                                                clickedCell.setState(CellState.EMPTY);
                                                                clickedCell.setDrawable(trans);
                                                                board[i.fst][i.snd].setDrawable(blue);
                                                                board[i.fst][i.snd].setState(CellState.BLUE);
                                                            } else {
                                                                switch(board[i.fst][i.snd].getState()) {
                                                                    case RED:
                                                                        for (Pair<Integer, Integer> r : redHome) {
                                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                                board[r.fst][r.snd].setDrawable(red);
                                                                                board[r.fst][r.snd].setState(CellState.RED);
                                                                                break;
                                                                            }
                                                                        }
                                                                        break;
                                                                    case YELLOW:
                                                                        for (Pair<Integer, Integer> r : yellowHome) {
                                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                                board[r.fst][r.snd].setDrawable(yellow);
                                                                                board[r.fst][r.snd].setState(CellState.YELLOW);
                                                                                break;
                                                                            }
                                                                        }
                                                                        break;
                                                                    case GREEN:
                                                                        for (Pair<Integer, Integer> r : greenHome) {
                                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                                board[r.fst][r.snd].setDrawable(green);
                                                                                board[r.fst][r.snd].setState(CellState.GREEN);
                                                                                break;
                                                                            }
                                                                        }
                                                                        break;
                                                                    case BLUE:
                                                                        for (Pair<Integer, Integer> r : blueHome) {
                                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                                board[r.fst][r.snd].setDrawable(blue);
                                                                                board[r.fst][r.snd].setState(CellState.BLUE);
                                                                                break;
                                                                            }
                                                                        }
                                                                        break;
                                                                }
                                                                clickedCell.setDrawable(trans);
                                                                clickedCell.setState(CellState.EMPTY);
                                                                board[i.fst][i.snd].setDrawable(blue);
                                                                board[i.fst][i.snd].setState(CellState.BLUE);
                                                            }
                                                            canMove[0] = false;
                                                            if (diceNumber[0] != 6) {
                                                                infoImage.setDrawable(new TextureRegionDrawable(red));
                                                                move = CellState.RED;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    break;
                                case YELLOW:
                                    if (canMove[0]) {
                                        for (int row = 0; row < rows; row++) {
                                            for (int column = 0; column < columns; column++) {
                                                if (board[row][column] == clickedCell && clickedCell.getState() == CellState.YELLOW) {
                                                    int currentNumber = boardPoints.get(new Pair(row, column));
                                                    for (Pair<Integer, Integer> i : boardPoints.keySet()) {
                                                        if (boardPoints.get(i) == ((currentNumber+diceNumber[0])%52)) {
                                                            if (board[i.fst][i.snd].getState() == CellState.EMPTY) {
                                                                clickedCell.setState(CellState.EMPTY);
                                                                clickedCell.setDrawable(trans);
                                                                board[i.fst][i.snd].setDrawable(yellow);
                                                                board[i.fst][i.snd].setState(CellState.YELLOW);
                                                            } else {
                                                                switch(board[i.fst][i.snd].getState()) {
                                                                    case RED:
                                                                        for (Pair<Integer, Integer> r : redHome) {
                                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                                board[r.fst][r.snd].setDrawable(red);
                                                                                board[r.fst][r.snd].setState(CellState.RED);
                                                                                break;
                                                                            }
                                                                        }
                                                                        break;
                                                                    case GREEN:
                                                                        for (Pair<Integer, Integer> r : greenHome) {
                                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                                board[r.fst][r.snd].setDrawable(green);
                                                                                board[r.fst][r.snd].setState(CellState.GREEN);
                                                                                break;
                                                                            }
                                                                        }
                                                                        break;
                                                                    case BLUE:
                                                                        for (Pair<Integer, Integer> r : blueHome) {
                                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                                board[r.fst][r.snd].setDrawable(blue);
                                                                                board[r.fst][r.snd].setState(CellState.BLUE);
                                                                                break;
                                                                            }
                                                                        }
                                                                        break;
                                                                    case YELLOW:
                                                                        for (Pair<Integer, Integer> r : yellowHome) {
                                                                            if (board[r.fst][r.snd].getState() == CellState.EMPTY) {
                                                                                board[r.fst][r.snd].setDrawable(yellow);
                                                                                board[r.fst][r.snd].setState(CellState.YELLOW);
                                                                                break;
                                                                            }
                                                                        }
                                                                        break;
                                                                }
                                                                clickedCell.setDrawable(trans);
                                                                clickedCell.setState(CellState.EMPTY);
                                                                board[i.fst][i.snd].setDrawable(yellow);
                                                                board[i.fst][i.snd].setState(CellState.YELLOW);
                                                            }
                                                            canMove[0] = false;
                                                            if (diceNumber[0] != 6) {
                                                                infoImage.setDrawable(new TextureRegionDrawable(blue));
                                                                move = CellState.BLUE;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    break;
                            }
                        }
                    });
                }
                else if (row == 7 && column == 7) {
                    board[7][7].setDrawable(dice_1);
                    board[row][column].addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            int dice = random.nextInt(6 - 1 + 1) + 1;
                            final CellActor clickedCell = (CellActor) event.getTarget();
                            if (canMove[0]) {
                                return;
                            }
                            canMove[0] = true;
                            switch (dice) {
                                case 1:
                                    clickedCell.setDrawable(dice_1);
                                    diceNumber[0] = 1;
                                    break;
                                case 2:
                                    clickedCell.setDrawable(dice_2);
                                    diceNumber[0] = 2;
                                    break;
                                case 3:
                                    clickedCell.setDrawable(dice_3);
                                    diceNumber[0] = 3;
                                    break;
                                case 4:
                                    clickedCell.setDrawable(dice_4);
                                    diceNumber[0] = 4;
                                    break;
                                case 5:
                                    clickedCell.setDrawable(dice_5);
                                    diceNumber[0] = 5;
                                    break;
                                case 6:
                                    clickedCell.setDrawable(dice_6);
                                    diceNumber[0] = 6;
                                    break;
                            }
                            switch(move) {
                                case RED:
                                    if (diceNumber[0] != 6) {
                                        for (Pair<Integer, Integer> i : boardPoints.keySet()) {
                                            if (board[i.fst][i.snd].getState() == CellState.RED) {
                                                return;
                                            }
                                        }
                                        canMove[0] = false;
                                        move = CellState.GREEN;
                                        infoImage.setDrawable(new TextureRegionDrawable(green));
                                    }
                                    break;
                                case GREEN:
                                    if (diceNumber[0] != 6) {
                                        for (Pair<Integer, Integer> i : boardPoints.keySet()) {
                                            if (board[i.fst][i.snd].getState() == CellState.GREEN) {
                                                return;
                                            }
                                        }
                                        canMove[0] = false;
                                        move = CellState.YELLOW;
                                        infoImage.setDrawable(new TextureRegionDrawable(yellow));
                                    }
                                    break;
                                case BLUE:
                                    if (diceNumber[0] != 6) {
                                        for (Pair<Integer, Integer> i : boardPoints.keySet()) {
                                            if (board[i.fst][i.snd].getState() == CellState.BLUE) {
                                                return;
                                            }
                                        }
                                        canMove[0] = false;
                                        move = CellState.RED;
                                        infoImage.setDrawable(new TextureRegionDrawable(red));
                                    }
                                    break;
                                case YELLOW:
                                    if (diceNumber[0] != 6) {
                                        for (Pair<Integer, Integer> i : boardPoints.keySet()) {
                                            if (board[i.fst][i.snd].getState() == CellState.YELLOW) {
                                                return;
                                            }
                                        }
                                        canMove[0] = false;
                                        move = CellState.BLUE;
                                        infoImage.setDrawable(new TextureRegionDrawable(blue));
                                    }
                                    break;
                            }
                        }
                    });
                } else {
                    board[row][column] = new CellActor();
                }
                grid.add(board[row][column]);
            }
            grid.row();
        }
        table.add(grid).row();
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }

    private Actor createMenuButton() {
        final TextButton menuButton = new TextButton("Menu", skin);
        menuButton.setWidth(100);
        menuButton.setPosition(GameConfig.HUD_WIDTH - menuButton.getWidth(), 20f);
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        return menuButton;
    }

    private Actor createInfo() {
        final Table table = new Table();
        table.add(new Label("Turn: ", skin));
        table.add(infoImage).size(30).row();
        table.center();
        table.pack();
        table.setPosition(
                GameConfig.HUD_WIDTH / 2f - table.getWidth() / 2f,
                GameConfig.HUD_HEIGHT - table.getHeight() - 20f
        );
        return table;
    }
}
