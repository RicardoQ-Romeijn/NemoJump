package com.juego.nemojump.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.juego.nemojump.utils.Assets;
import com.juego.nemojump.MainSuperJumper;
import com.juego.nemojump.utils.Settings;
import com.juego.nemojump.game.GameScreen;

public class MainMenuScreen extends Screens {

	Image titulo;

	TextButton btPlay;
	Button  btMusic;
	Button  btSound;
	Label lbBestScore;
	Label lbTotalCoins;
	Label lbTotalGames;

	public MainMenuScreen(final MainSuperJumper game) {
		super(game);

		titulo = new Image(Assets.titulo); // Creamos el titulo (Imagen)
		// Ponemos la posicion centrado, y fuera de la pantalla
		titulo.setPosition(SCREEN_WIDTH / 2f - titulo.getWidth() / 2f, 1000);

		// Añadimos una secuencia para movel el titulo a su sitito con una animación
		// Tambien añadimos el Score despues de poner el titulo en su sitio
		titulo.addAction(Actions.sequence(Actions.moveTo(titulo.getX(), 660, 1, Interpolation.bounceOut), Actions.run(new Runnable() {
			@Override
			public void run() {
				stage.addActor(btPlay);
				stage.addActor(btMusic);
				stage.addActor(btSound);
				stage.addActor(lbBestScore);
				stage.addActor(lbTotalCoins);
				stage.addActor(lbTotalGames);
			}
		})));

		/*
		* Creamos el Boton Play para empezar el Juego
		*/
		btPlay = new TextButton("Play", Assets.textButtonStyle);
		btPlay.setPosition(SCREEN_WIDTH / 2f - btPlay.getWidth() / 2f, 325);
		btPlay.pad(10);
		btPlay.pack();
		addListenerPress(btPlay);
		btPlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changeScreen(GameScreen.class, game);
			}
		});

		/*
		 * Creamos los botones de Musica y Audio para el juego
		 */
		btMusic = new Button(Settings.getMusicIcon());
		btMusic.setPosition(SCREEN_WIDTH - 50, SCREEN_HEIGHT - 50);
		btMusic.pad(10);
		addListenerPress(btMusic);
		btMusic.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Settings.changeMusic();
				TextureRegionDrawable button = new TextureRegionDrawable(Settings.getMusicIcon());
				Button.ButtonStyle textButtonStyle = new TextButton.TextButtonStyle(button, button, null, null);
				btMusic.setStyle(textButtonStyle);
				Settings.playMusic();
			}
		});

		btSound = new Button(Settings.getSoundIcon());
		btSound.setPosition(SCREEN_WIDTH - 50, SCREEN_HEIGHT - btMusic.getHeight() - 60);
		btSound.pad(10);
		addListenerPress(btSound);
		btSound.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Settings.changeSound();
				TextureRegionDrawable button = new TextureRegionDrawable(Settings.getSoundIcon());
				Button.ButtonStyle textButtonStyle = new TextButton.TextButtonStyle(button, button, null, null);
				btSound.setStyle(textButtonStyle);
			}
		});

		/*
		* Añadimos 3 Labels Distintos, uno para cada ajuste que guardamos
		*/
		lbBestScore = new Label("Mejor Puntuación " + Settings.bestScore, Assets.labelStyle);
		lbBestScore.setPosition(SCREEN_WIDTH / 2f - lbBestScore.getWidth() / 2f, 150);
		lbBestScore.getColor().a = 0;
		lbBestScore.addAction(Actions.alpha(1, .25f));

		lbTotalCoins = new Label("Total Camarones " + Settings.camaronesTotal, Assets.labelStyle);
		lbTotalCoins.setPosition(SCREEN_WIDTH / 2f - lbBestScore.getWidth() / 2f, 100);
		lbTotalCoins.getColor().a = 0;
		lbTotalCoins.addAction(Actions.alpha(1, .25f));

		lbTotalGames = new Label("Veces Jugado " + Settings.numeroVecesJugadas, Assets.labelStyle);
		lbTotalGames.setPosition(SCREEN_WIDTH / 2f - lbBestScore.getWidth() / 2f, 50);
		lbTotalGames.getColor().a = 0;
		lbTotalGames.addAction(Actions.alpha(1, .25f));

		// Añadimos los actores titulo y boton al stage (los labels sales despues del titulo, mira el codigo arriba)
		stage.addActor(titulo);
	}

	@Override
	public void update(float delta) {

	}

	// Dibujar en Pantalla, por defecto queremos el fondo
	@Override
	public void draw(float delta) {
		batcher.begin();
		batcher.draw(Assets.fondo, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		batcher.end();
	}

	// Si pulsamos Esc o Borrar, termina el programa cuando estamos en el menu principal
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
			Gdx.app.exit();
		}
		return super.keyDown(keycode);
	}

}
