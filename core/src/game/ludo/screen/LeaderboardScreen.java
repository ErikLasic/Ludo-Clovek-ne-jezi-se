package game.ludo.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.ludo.LudoGame;
import game.ludo.assets.AssetDescriptors;
import game.ludo.assets.RegionNames;
import game.ludo.config.GameConfig;

public class LeaderboardScreen extends ScreenAdapter {

    private final LudoGame game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;

    private Skin skin;
    private TextureAtlas gameplayAtlas;

    public LeaderboardScreen(LudoGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        skin = assetManager.get(AssetDescriptors.UI_SKIN);
        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);

        stage.addActor(LeaderboardUI());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 0f);

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

    private Actor LeaderboardUI() {
        Table table = new Table();

        TextureRegion backgroundRegion = gameplayAtlas.findRegion(RegionNames.BACKGROUND);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        TextButton menuButton = new TextButton("Menu", skin);
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        Table leaderboardTable = new Table();
        leaderboardTable.defaults().padLeft(30).padRight(30);

        TextureRegion menuBackgroundRegion = gameplayAtlas.findRegion(RegionNames.MENU_BACKGROUND);
        leaderboardTable.setBackground(new TextureRegionDrawable(menuBackgroundRegion));

        leaderboardTable.add(new Label("Leaderboard:", skin)).padBottom(100).colspan(2).top().row();
        leaderboardTable.add(new Label("Janez: 1st", skin)).fillX().row();
        leaderboardTable.add(new Label("Marija: 2nd", skin)).fillX().row();
        leaderboardTable.add(new Label("Lojze: 3rd", skin)).fillX().row();
        leaderboardTable.add(new Label("Franc: 4th", skin)).fillX().row();
        leaderboardTable.add(menuButton).fillX().padTop(50);

        leaderboardTable.center();

        table.add(leaderboardTable);
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }
}
