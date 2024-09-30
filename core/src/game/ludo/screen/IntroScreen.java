package game.ludo.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.ludo.LudoGame;
import game.ludo.assets.AssetDescriptors;
import game.ludo.assets.RegionNames;
import game.ludo.config.GameConfig;

public class IntroScreen extends ScreenAdapter {
    //Čas animacije
    public static final float INTRO_DURATION_IN_SEC = 2f;

    private final LudoGame game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private TextureAtlas gameplayAtlas;

    private float duration = 0f;

    private Stage stage;

    public IntroScreen(LudoGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        // load assets
        assetManager.load(AssetDescriptors.UI_FONT);
        assetManager.load(AssetDescriptors.UI_SKIN);
        assetManager.load(AssetDescriptors.GAMEPLAY);
        assetManager.finishLoading();   // blocks until all assets are loaded

        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);

        stage.addActor(background());
        stage.addActor(diceOne());
        stage.addActor(diceTwo());
        stage.addActor(diceThree());
        stage.addActor(diceFour());
        stage.addActor(diceFive());
        stage.addActor(diceSix());
        stage.addActor(createAnimationRED());
        stage.addActor(createAnimationGREEN());
        stage.addActor(Green_1());
        stage.addActor(Green_2());
        stage.addActor(Green_3());
        stage.addActor(Red_1());
        stage.addActor(Red_2());
        stage.addActor(Red_3());
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        //ScreenUtils.clear(165 / 255f, 113 / 255f, 121 / 255f, 0f);
        ScreenUtils.clear(0f, 0f, 0f, 0f);

        duration += delta;

        // go to the MenuScreen after INTRO_DURATION_IN_SEC seconds
        if (duration > INTRO_DURATION_IN_SEC) {
            game.setScreen(new MenuScreen(game));
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private Actor background() {
        Table table = new Table();
        table.defaults().pad(20);

        TextureRegion backgroundRegion = gameplayAtlas.findRegion(RegionNames.BACKGROUND);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }

    private Actor diceOne() {
        Image diceOne = new Image(gameplayAtlas.findRegion(RegionNames.DICE_ONE));
        diceOne.setPosition(diceOne.getWidth(),
                viewport.getWorldHeight() - diceOne.getHeight() -50f);
        diceOne.setOrigin(Align.center);
        diceOne.addAction(
                Actions.sequence(
                        Actions.parallel(
                                Actions.rotateBy(-1080, 2f)   // rotate the image three times
                        )
                )
        );
        return diceOne;
    }
    private Actor diceTwo() {
        Image diceTwo = new Image(gameplayAtlas.findRegion(RegionNames.DICE_TWO));
        diceTwo.setPosition(2.5f*diceTwo.getWidth(),
                viewport.getWorldHeight() - diceTwo.getHeight() -20f);
        diceTwo.setOrigin(Align.center);
        diceTwo.addAction(
                Actions.sequence(
                        Actions.parallel(
                                Actions.rotateBy(1080, 2f)   // rotate the image three times
                        )
                )
        );
        return diceTwo;
    }
    private Actor diceThree() {
        Image diceThree = new Image(gameplayAtlas.findRegion(RegionNames.DICE_THREE));
        diceThree.setPosition(2.5f*2*diceThree.getWidth(),
                viewport.getWorldHeight() - diceThree.getHeight() -50f);
        diceThree.setOrigin(Align.center);
        diceThree.addAction(
                Actions.sequence(
                        Actions.parallel(
                                Actions.rotateBy(-1080, 2f)   // rotate the image three times
                        )
                )
        );
        return diceThree;
    }
    private Actor diceFour() {
        Image diceFour = new Image(gameplayAtlas.findRegion(RegionNames.DICE_FOUR));
        diceFour.setPosition(2.5f*3*diceFour.getWidth(),
                viewport.getWorldHeight() - diceFour.getHeight() -20f);
        diceFour.setOrigin(Align.center);
        diceFour.addAction(
                Actions.sequence(
                        Actions.parallel(
                                Actions.rotateBy(1080, 2f)   // rotate the image three times
                        )
                )
        );
        return diceFour;
    }
    private Actor diceFive() {
        Image diceFive = new Image(gameplayAtlas.findRegion(RegionNames.DICE_FIVE));
        diceFive.setPosition(2.5f*4*diceFive.getWidth(),
                viewport.getWorldHeight() - diceFive.getHeight() -50f);
        diceFive.setOrigin(Align.center);
        diceFive.addAction(
                Actions.sequence(
                        Actions.parallel(
                                Actions.rotateBy(-1080, 2f)   // rotate the image three times
                        )
                )
        );
        return diceFive;
    }
    private Actor diceSix() {
        Image diceSix = new Image(gameplayAtlas.findRegion(RegionNames.DICE_SIX));
        diceSix.setPosition(2.5f*5*diceSix.getWidth(),
                viewport.getWorldHeight() - diceSix.getHeight() -20f);
        diceSix.setOrigin(Align.center);
        diceSix.addAction(
                Actions.sequence(
                        Actions.parallel(
                                Actions.rotateBy(1080, 2f)   // rotate the image three times
                        )
                )
        );
        return diceSix;
    }

    private Actor createAnimationRED() {
        Image red = new Image(gameplayAtlas.findRegion(RegionNames.RED_PLAYER));
        red.setPosition(viewport.getWorldWidth()/4.3f - red.getWidth()/2f, 0 + red.getHeight()*2f);

        red.addAction(
                Actions.sequence(
                        Actions.moveTo(viewport.getWorldWidth()/2f - red.getWidth()/2f*3, 0+red.getHeight(), 0.5f),
                        Actions.moveTo(viewport.getWorldWidth()/2f - red.getWidth()/2f*3, 0+red.getHeight()*3f, 0.5f)
                )
        );
        return red;
    }
    private Actor Red_1() {
        Image red = new Image(gameplayAtlas.findRegion(RegionNames.RED_PLAYER));
        red.setPosition(viewport.getWorldWidth()/4.3f - red.getWidth()/2f*3f, 0 + red.getHeight()*2f);

        return red;
    }
    private Actor Red_2() {
        Image red = new Image(gameplayAtlas.findRegion(RegionNames.RED_PLAYER));
        red.setPosition(viewport.getWorldWidth()/4.3f - red.getWidth()/2f*3f, 0 + red.getHeight()*3f);

        return red;
    }
    private Actor Red_3() {
        Image red = new Image(gameplayAtlas.findRegion(RegionNames.RED_PLAYER));
        red.setPosition(viewport.getWorldWidth()/4.3f - red.getWidth()/2f, 0 + red.getHeight()*3f);

        return red;
    }

    private Actor createAnimationGREEN() {
        Image green = new Image(gameplayAtlas.findRegion(RegionNames.GREEN_PLAYER));
        green.setPosition(viewport.getWorldWidth()/4.3f - green.getWidth()/2f, 0 + green.getHeight()*12f);

        green.addAction(
                Actions.sequence(
                        Actions.delay(1f, Actions.moveTo(viewport.getWorldWidth()/4.3f - green.getWidth()/2f*5f, 0 + green.getHeight()*8f, 0.5f)),
                        Actions.moveTo(viewport.getWorldWidth()/4.3f - green.getWidth()/2f + green.getWidth()*2f, 0 + green.getHeight()*8f, 0.5f)
                )
        );
        return green;
    }
    private Actor Green_1() {
        Image green = new Image(gameplayAtlas.findRegion(RegionNames.GREEN_PLAYER));
        green.setPosition(viewport.getWorldWidth()/4.3f - green.getWidth()/2f*3f, 0 + green.getHeight()*12f);

        return green;
    }
    private Actor Green_2() {
        Image green = new Image(gameplayAtlas.findRegion(RegionNames.GREEN_PLAYER));
        green.setPosition(viewport.getWorldWidth()/4.3f - green.getWidth()/2f*3f, 0 + green.getHeight()*11f);

        return green;
    }
    private Actor Green_3() {
        Image green = new Image(gameplayAtlas.findRegion(RegionNames.GREEN_PLAYER));
        green.setPosition(viewport.getWorldWidth()/4.3f - green.getWidth()/2f, 0 + green.getHeight()*11f);

        return green;
    }
}
