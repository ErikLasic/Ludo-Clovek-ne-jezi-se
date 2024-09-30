package game.ludo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.ludo.screen.IntroScreen;

public class LudoGame extends Game {
	private SpriteBatch batch;
	private AssetManager assetManager;
	
	@Override
	public void create () {
		assetManager = new AssetManager();

		batch = new SpriteBatch();

		setScreen(new IntroScreen(this));
	}
	
	@Override
	public void dispose () {
		assetManager.dispose();
		batch.dispose();
	}

	public AssetManager getAssetManager() {return assetManager;}

	public SpriteBatch getBatch() {return batch;}
}
