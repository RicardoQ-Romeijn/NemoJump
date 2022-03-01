package com.juego.nemojump;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.juego.nemojump.screens.MainMenuScreen;
import com.juego.nemojump.screens.Screens;
import com.juego.nemojump.utils.Assets;
import com.juego.nemojump.utils.Settings;

/**
 * Creamos la clase que se ejecutará, MainSuperJumper, que extiende de Juego
 */
public class MainSuperJumper extends Game {
	// Creamos el Stage y el Batcher que utilizaremos durante to.do el juego
	public Stage stage;
	public SpriteBatch batcher;

	// Sobreescribimer el Create de Game
	@Override
	public void create() {
		// Instanciamos ambos objetos
		stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));
		batcher = new SpriteBatch();

		// Cargamos los Settings y Assets para el Juego
		Settings.load();
		Assets.load();

		// Ponemos la Pantalla al Menu Principal
		setScreen(new MainMenuScreen(this));
	}
}
