package game.ludo.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.ludo.CellState;
import game.ludo.LudoGame;
import game.ludo.assets.AssetDescriptors;
import game.ludo.assets.RegionNames;
import game.ludo.common.GameManager;
import game.ludo.config.GameConfig;

public class SettingsScreen extends ScreenAdapter {

    private final LudoGame game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;

    private ButtonGroup<CheckBox> checkBoxGroup;
    private CheckBox checkBoxRED;
    private CheckBox checkBoxGREEN;
    private CheckBox checkBoxBLUE;
    private CheckBox checkBoxYELLOW;

    public SettingsScreen(LudoGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        stage.addActor(SettingsUI());
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

    private Actor SettingsUI() {
        Table table = new Table();
        table.defaults().padLeft(30).padRight(30);

        Skin skin = assetManager.get(AssetDescriptors.UI_SKIN);
        TextureAtlas gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);

        TextureRegion backgroundRegion = gameplayAtlas.findRegion(RegionNames.BACKGROUND);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                CheckBox checked = checkBoxGroup.getChecked();

                if (checked == checkBoxRED) {
                    GameManager.INSTANCE.setInitColor(CellState.RED);
                } else if (checked == checkBoxGREEN) {
                    GameManager.INSTANCE.setInitColor(CellState.GREEN);
                } else if (checked == checkBoxBLUE) {
                    GameManager.INSTANCE.setInitColor(CellState.BLUE);
                } else if (checked == checkBoxYELLOW) {
                    GameManager.INSTANCE.setInitColor(CellState.YELLOW);
                }
            }
        };

        checkBoxRED = new CheckBox(CellState.RED.name(), skin);
        checkBoxGREEN = new CheckBox(CellState.GREEN.name(), skin);
        checkBoxBLUE = new CheckBox(CellState.BLUE.name(), skin);
        checkBoxYELLOW = new CheckBox(CellState.YELLOW.name(), skin);

        checkBoxRED.addListener(listener);
        checkBoxGREEN.addListener(listener);
        checkBoxBLUE.addListener(listener);
        checkBoxYELLOW.addListener(listener);

        checkBoxGroup = new ButtonGroup<>(checkBoxRED, checkBoxGREEN, checkBoxBLUE, checkBoxYELLOW);
        checkBoxGroup.setChecked(GameManager.INSTANCE.getInitColor().name());

        TextButton menuButton = new TextButton("Menu", skin);
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });

        Table contentTable = new Table(skin);

        TextureRegion menuBackground = gameplayAtlas.findRegion(RegionNames.MENU_BACKGROUND);
        contentTable.setBackground(new TextureRegionDrawable(menuBackground));

        contentTable.add(new Label("Settings:", skin)).padBottom(50).colspan(2).row();
        contentTable.add(new Label("Choose init color:", skin)).colspan(2).row();
        contentTable.add(checkBoxRED);
        contentTable.add(checkBoxGREEN).row();
        contentTable.add(checkBoxBLUE);
        contentTable.add(checkBoxYELLOW).row();
        contentTable.add(menuButton).width(100).padTop(50).colspan(2);

        table.add(contentTable);
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }
}
