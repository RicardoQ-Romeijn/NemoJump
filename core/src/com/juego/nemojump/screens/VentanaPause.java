package com.juego.nemojump.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.juego.nemojump.utils.Assets;
import com.juego.nemojump.game.GameScreen;
import com.juego.nemojump.game.WorldGame;

public class VentanaPause extends Ventana {

	// Creamos 2 Botones y el Mundo que necesitamos para Game Over
	TextButton btMenu, btResume;
	WorldGame oWorld;

	// Constructor
	public VentanaPause(final GameScreen currentScreen) {
		// Llamamos al constructor de la clase abstracta Ventana
		super(currentScreen, 350, 280, 300);
		// Guardamos el Mundo en la variable de clase
		this.oWorld = currentScreen.oWorld;

		// Añadimos un Texto "Pause" a la ventana, que será el titulo
		Label lbShop = new Label("Pause", Assets.labelStyle);
		lbShop.setFontScale(1.5f);
		lbShop.setAlignment(Align.center);
		lbShop.setPosition(getWidth() / 2f - lbShop.getWidth() / 2f, 230);
		this.addActor(lbShop);

		// Metodo que crea los botones
		this.initButtons();

		// Creamos otra tabla donde meteremos los bottones
		Table buttonTable = new Table();

		// Ponemos unos settings por defecto a la tabla
		buttonTable.defaults().expandX().uniform().fill();

		// Añadimos los dos botones con un padding entre los dos
		buttonTable.add(btResume);
		buttonTable.row().padTop(20);
		buttonTable.add(btMenu);

		// Pack hace que, la tabla se ajuste al tamaño necesario a partir de su contenido
		buttonTable.pack();
		// Posicionamos la tabla debajo de la otra tabla de Score
		buttonTable.setPosition(getWidth() / 2f - buttonTable.getWidth() / 2f, 50);
		// Añadimos la tabla a la ventana
		this.addActor(buttonTable);

	}

	// Creamos los dos botones en el metodo initButtons
	private void initButtons() {
		// Botton Menu
		btMenu = new TextButton("Menu", Assets.textButtonStyle);
		btMenu.pad(15);

		// Añadimos un Listener al Boton que acabamos de crear
		screen.addListenerPress(btMenu);
		// Cuando se pulsa, quitamos la ventana, y porque es el botton de volver al menu, cambiamos la pantalla al MainMenuScreen
		btMenu.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				hide();
				screen.changeScreen(MainMenuScreen.class, game);
			};
		});

		// Botton Volver a Intentar
		btResume = new TextButton("Resume", Assets.textButtonStyle);
		btResume.pad(15);

		// Añadimos un Listener al Boton que acabamos de crear
		screen.addListenerPress(btResume);
		// Cuando se pulsa, quitamos la ventana, y porque es el botton de resumir, no hacemos nada mas
		btResume.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				hide();
			};
		});
	}

	/*private void initButtons() {
		btMenu = new TextButton("Menu", Assets.textButtonStyle);
		btMenu.pad(15);

		screen.addListenerPress(btMenu);
		btMenu.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				hide();
				screen.changeScreen(MainMenuScreen.class, game);
			};
		});

		btResume = new TextButton("Resume", Assets.textButtonStyle);
		btResume.pad(15);

		screen.addListenerPress(btResume);
		btResume.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				hide();

			};
		});
	}*/

	@Override
	public void show(Stage stage) {
		super.show(stage);
	}

	@Override
	public void hide() {
		((GameScreen) screen).setRunning();
		super.hide();
	}

}
