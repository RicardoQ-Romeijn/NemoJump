package com.juego.superjumper.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.juego.superjumper.utils.Assets;
import com.juego.superjumper.MainSuperJumper;
import com.juego.superjumper.utils.Settings;
import com.juego.superjumper.game.GameScreen;

public class MainMenuScreen extends Screens {

	Image titulo;

	TextButton btPlay;
	Label lbBestScore;

	public MainMenuScreen(final MainSuperJumper game) {
		super(game);

		titulo = new Image(Assets.titulo); // Creamos el titulo (Imagen)
		// Ponemos la posicion centrado, y fuera de la pantalla
		titulo.setPosition(SCREEN_WIDTH / 2f - titulo.getWidth() / 2f, 1000);

		// Añadimos una secuencia para movel el titulo a su sitito con una animación
		// Tambien añadimos el Score despues de poner el titulo en su sitio
		titulo.addAction(Actions.sequence(Actions.moveTo(titulo.getX(), 600, 1, Interpolation.bounceOut), Actions.run(new Runnable() {
			@Override
			public void run() {
				stage.addActor(lbBestScore);
			}
		})));

		lbBestScore = new Label("Best score " + Settings.bestScore, Assets.labelStyleChico);
		lbBestScore.setPosition(SCREEN_WIDTH / 2f - lbBestScore.getWidth() / 2f, 570);
		lbBestScore.getColor().a = 0;
		lbBestScore.addAction(Actions.alpha(1, .25f));

		btPlay = new TextButton("Play", Assets.textButtonStyleGrande);
		btPlay.setPosition(SCREEN_WIDTH / 2f - btPlay.getWidth() / 2f, 440);
		btPlay.pad(10);
		btPlay.pack();
		addEfectoPress(btPlay);
		btPlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changeScreen(GameScreen.class, game);
			}
		});

		stage.addActor(titulo);
		stage.addActor(btPlay);
	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void draw(float delta) {
		batcher.begin();
		batcher.draw(Assets.fondo, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		batcher.end();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
			Gdx.app.exit();
		}
		return super.keyDown(keycode);
	}

}
