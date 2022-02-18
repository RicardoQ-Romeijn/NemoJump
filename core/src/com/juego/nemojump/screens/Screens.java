package com.juego.nemojump.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.juego.nemojump.MainSuperJumper;
import com.juego.nemojump.utils.Settings;
import com.juego.nemojump.game.GameScreen;

public abstract class Screens extends InputAdapter implements Screen {
	// Variables estaticas que utilizaremos muchas veces
	public static final int SCREEN_WIDTH = 480;
	public static final int SCREEN_HEIGHT = 800;

	public static final float WORLD_WIDTH = 4.8f;
	public static final float WORLD_HEIGHT = 8f;

	// Variable del Juego Base
	public MainSuperJumper game;

	// Camaras, etc
	public OrthographicCamera oCam;
	public SpriteBatch batcher;
	public Stage stage;

	protected Music music;

	public Screens(MainSuperJumper game) {
		// Sacar las variables que vamos a utilizar a partir del juego
		this.game = game;
		this.stage = game.stage;
		this.batcher = game.batcher;

		this.stage.clear();

		// Crear la camara e centrar
		oCam = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
		oCam.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);

		// InputMultiplexer te permite añadir varios procesos, como el stage, dentro de un objeto donde se puede ejecutar a la vez
		InputMultiplexer input = new InputMultiplexer(this, stage);
		Gdx.input.setInputProcessor(input);
	}

	@Override
	public void render(float delta) {
		if (delta > .1f)
			delta = .1f;

		update(delta);
		stage.act(delta);

		oCam.update();
		batcher.setProjectionMatrix(oCam.combined);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		draw(delta);

		stage.draw();
	}

	// Metodo para añadir un listener cuando pulsamos sobre un objeto / actor
	public void addListenerPress(final Actor actor) {
		actor.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				actor.setPosition(actor.getX(), actor.getY() - 5);
				event.stop();
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				actor.setPosition(actor.getX(), actor.getY() + 5);
			}
		});
	}

	public void changeScreen(final Class<?> newScreen, final MainSuperJumper game) {
		if (newScreen == GameScreen.class) {
			game.setScreen(new GameScreen(game));
		}
		else if (newScreen == MainMenuScreen.class) {
			game.setScreen(new MainMenuScreen(game));
		}
	}

	public abstract void update(float delta);

	public abstract void draw(float delta);

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
		if (music != null) {
			music.stop();
			music.dispose();
			music = null;
		}
		Settings.save();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		batcher.dispose();
	}

}
