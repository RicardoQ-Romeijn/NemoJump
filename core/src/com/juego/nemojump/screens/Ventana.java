package com.juego.nemojump.screens;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.juego.nemojump.MainSuperJumper;
import com.juego.nemojump.screens.Screens;

public abstract class Ventana extends Group {
	// Variable global para la duracion de animacion
	public static final float DURACION_ANIMATION = .2f;

	// Variables de pantalla y juego que necesitaremos
	protected Screens screen;
	protected MainSuperJumper game;

	// Booleano de ventana visible
	private boolean isVisible = false;

	// Constructor con parametros, de pantalla actual, ancho alto de ventana y posicion que queremos la ventana
	public Ventana(Screens currentScreen, float width, float height, float positionY) {
		screen = currentScreen;
		game = currentScreen.game;
		setSize(width, height);
		setY(positionY);
	}

	//  Activar Ventana
	public void show(Stage stage) {
		// Poner tamaño y escala de ventana
		this.setOrigin(this.getWidth() / 2f, this.getHeight() / 2f);
		this.setX(Screens.SCREEN_WIDTH / 2f - this.getWidth() / 2f);
		// cambiamos la estala para animarlo
		this.setScale(.1f);

		// Animar la aparición de la ventana
		this.addAction(Actions.sequence(Actions.scaleTo(1, 1, DURACION_ANIMATION)));

		// Ponemos la ventana visible
		isVisible = true;
		stage.addActor(this);
	}

	// Decir si está visible la ventana
	public boolean isVisible() {
		return isVisible;
	}

	// Cerrar Ventana
	public void hide() {
		isVisible = false;
		remove();
	}
}
