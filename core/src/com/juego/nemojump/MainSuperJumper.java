package com.juego.nemojump;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.juego.nemojump.screens.MainMenuScreen;
import com.juego.nemojump.screens.Screens;
import com.juego.nemojump.utils.Assets;
import com.juego.nemojump.utils.Settings;

public class MainSuperJumper extends Game {

	public MainSuperJumper() {

	}

	public Stage stage;
	public SpriteBatch batcher;

	@Override
	public void create() {
		stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));
		batcher = new SpriteBatch();

		Settings.load();
		Assets.load();

		setScreen(new MainMenuScreen(this));
	}
}
