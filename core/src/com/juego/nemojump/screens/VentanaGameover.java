package com.juego.nemojump.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.juego.nemojump.utils.Assets;
import com.juego.nemojump.utils.Settings;
import com.juego.nemojump.game.GameScreen;
import com.juego.nemojump.game.WorldGame;

public class VentanaGameover extends Ventana {

	// Creamos 2 Botones y el Mundo que necesitamos para Game Over
	TextButton btMenu, btTryAgain;
	WorldGame oWorld;

	// Constructor
	public VentanaGameover(final GameScreen currentScreen) {
		// Llamamos al constructor de la clase abstracta Ventana
		super(currentScreen, 350, 400, 250);
		// Guardamos el Mundo en la variable de clase
		oWorld = currentScreen.oWorld;

		// Añadimos un Texto "Game Over" a la ventana
		Label lbShop = new Label("Game Over!", Assets.labelStyle);
		lbShop.setFontScale(1.5f);
		lbShop.setAlignment(Align.center);
		lbShop.setPosition(getWidth() / 2f - lbShop.getWidth() / 2f, 400);
		this.addActor(lbShop);

		// Metodo que crea los botones
		this.initButtons();

		/*
		* Creamos una tabla, donde vamos a enseñar la puntuación despues del juego, una vez muerto.
		* Y vamos a enseñar la puntación mas alta que tengamos guardado del juego.
		*/
		final Table scoreTable = new Table();
		scoreTable.setSize(getWidth(), 130);
		scoreTable.setY(230);

		// Etiqueta para Score del juego
		Label lbScore = new Label("Score", Assets.labelStyle);
		lbScore.setAlignment(Align.left);

		// Etiqueta para sacar la puntuación del juego, en este caso la distancia que ha subido
		Label lblNumScore = new Label(oWorld.distanciaMax + "", Assets.labelStyle);
		lblNumScore.setAlignment(Align.right);

		// Etiqueta para Score mas alto guardado de la aplicación
		Label lbBestScore = new Label("Best Score", Assets.labelStyle);
		lbScore.setAlignment(Align.left);

		// Etiqueta para sacar la puntuación mas alta guardada en Settings
		Label lbBestNumScore = new Label(Settings.bestScore + "", Assets.labelStyle);
		lblNumScore.setAlignment(Align.right);

		// En la tabla, añadimos a la izquierda el primer lable de score,
		// y a la derecha, con un expand, la puntuación actual
		scoreTable.pad(10);
		scoreTable.add(lbScore).left();
		scoreTable.add(lblNumScore).right().expand();

		// En la tabla, añaidmos otra fila, y hazemos lo mismo pero para la puntuación maxima
		scoreTable.row();
		scoreTable.add(lbBestScore).left();
		scoreTable.add(lbBestNumScore).right().expand();

		// añadimos el table a la ventana
		this.addActor(scoreTable);

		// Creamos otra tabla donde meteremos los bottones
		Table buttonTable = new Table();

		// Ponemos unos settings por defecto a la tabla
		buttonTable.defaults().expandX().uniform().fill();

		// Añadimos los dos botones con un padding entre los dos
		buttonTable.add(btTryAgain);
		buttonTable.row().padTop(20);
		buttonTable.add(btMenu);

		// Pack hace que, la tabla se ajuste al tamaño necesario a partir de su contenido
		buttonTable.pack();
		// Posicionamos la tabla debajo de la otra tabla de Score
		buttonTable.setPosition(getWidth() / 2f - buttonTable.getWidth() / 2f, 60);
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
		btTryAgain = new TextButton("Try Again", Assets.textButtonStyle);
		btTryAgain.pad(15);

		// Añadimos un Listener al Boton que acabamos de crear
		screen.addListenerPress(btTryAgain);
		// Cuando se pulsa, quitamos la ventana, y porque es el botton de volver a interntar cambiamos la pantalla al GameScreen
		btTryAgain.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				hide();
				screen.changeScreen(GameScreen.class, game);
			};
		});
	}

	@Override
	public void show(Stage stage) {
		super.show(stage);
	}

}
