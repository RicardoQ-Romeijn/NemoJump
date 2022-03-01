package com.juego.nemojump.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.juego.nemojump.utils.Assets;
import com.juego.nemojump.MainSuperJumper;
import com.juego.nemojump.utils.Settings;
import com.juego.nemojump.screens.VentanaGameover;
import com.juego.nemojump.screens.VentanaPause;
import com.juego.nemojump.screens.Screens;

/**
* La Pantalla del Juego
* Solo será el codigo de la pantalla, esto no incluye la física ni los sprites, esto se hará
* en otros ficheros para mejorar la lectura del codigo.
*
* Física: WorldGame
* Sprites: WorldGameRender
*/
public class GameScreen extends Screens {

	// Los estados del juego, activo, pausado, muerto
	static final int STATE_RUNNING = 1;
	static final int STATE_PAUSED = 2;
	static final int STATE_GAME_OVER = 3;
	static int state;

	// Objeto de Física y Render (Sprites)
	public WorldGame oWorld;
	WorldGameRender spriteRenderer;

	// Posición mas alto de Dory?
	Vector3 touchPositionWorldCoords;

	// Etiquetas y boton que aparecerán en la parte superior de la pantalla
	Label lbDistancia, lbCamarones;
	Button btPause;

	// Un objeto de la Ventana de Pausa
	VentanaPause ventanPause;

	// Constructor del Juego
	public GameScreen(MainSuperJumper game) {
		super(game);

		// Instanciamos la ventana Pause para utilizarlo mas tarde
		ventanPause = new VentanaPause(this);

		// Instanciamos las clases de la física y sprites
		oWorld = new WorldGame();
		spriteRenderer = new WorldGameRender(batcher, oWorld);
		touchPositionWorldCoords = new Vector3();

		// Poner el estado de juego a "vivo" o en ejecución y añadir a la variable de las veces jugado uno
		state = STATE_RUNNING;
		Settings.numeroVecesJugadas++;

		// stage.setDebugAll(true);

		// Creamos una tabla donde añadiremos las etiquetas y el boton
		Table menuMarcador = new Table();
		menuMarcador.setSize(SCREEN_WIDTH, 40); // ancho de pantalla y alto 40px
		// Posicionar en Y a la parte superior de pantalla menos el alto de la tabla
		menuMarcador.setY(SCREEN_HEIGHT - menuMarcador.getHeight());

		// Instancias las etiquetas
		lbCamarones = new Label("", Assets.labelStyle);
		lbDistancia = new Label("", Assets.labelStyle);

		// Añadir la imagen de los camarones
		menuMarcador.add(new Image(new TextureRegionDrawable(Assets.camarones))).left().padLeft(5).padRight(2);
		// Añadir la etiqueta de numero de camarones
		menuMarcador.add(lbCamarones).left();
		// Añador la distancia recorrido centrado y expandir en x
		menuMarcador.add(lbDistancia).center().expandX();

		// Instanciamos el boton de pausa
		btPause = new Button(Assets.btPause);
		btPause.setSize(35, 35); // ponder un tamaño al boton
		btPause.setPosition(SCREEN_WIDTH - 40, SCREEN_HEIGHT - 40); // poner en la esquina superior derecha
		addListenerPress(btPause); // Añadir el listener al boton (Heredado)
		btPause.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				setPaused(); // Metodo de Pausar (Mas Abajo)
			}
		});

		// Añadimos la tabla y el boton al juego
		stage.addActor(menuMarcador);
		stage.addActor(btPause);
	}

	// Cada vez que se llama al update (dentro del render en la clase Sreens)
	@Override
	public void update(float delta) {
		switch (state) {
		case STATE_RUNNING: // Si esta en estado activo
			updateRunning(delta); // Ejecutamos el metodo activo
			break;
		case STATE_GAME_OVER: // Si esta en estado muerto
			updateGameOver(delta); // Ejecutamos el metodo morir
			break;
		}

	}

	// Metodo de Juego Activo
	private void updateRunning(float delta) {
		float acelX = 0; // Acceleración de Dory Horizontal

		/*
		* Hay 2 condiciones, para ir a la derecho o izquierda, uno de ellos, para juego en el ordenador
		* Cuando pulsamos A o D para controlar a Dory, el otro metodo es pulsando la pantalla,
		* Si pulsamos la pantalla en la parte derecha, va a la derecha, si pulsamos la parte izquierda
		* Dory va a la izquierda.
		*
		* Imaginate que hay una linea imaginaria que parte el movil por la mitad vertidalmente.
		*/
		if (Gdx.input.isKeyPressed(Keys.A) || (Gdx.input.isTouched() && Gdx.input.getX() < Gdx.graphics.getWidth() / 2))
			acelX = -1;
		else if (Gdx.input.isKeyPressed(Keys.D) || (Gdx.input.isTouched() && Gdx.input.getX() > Gdx.graphics.getWidth() / 2))
			acelX = 1;

		// Llamamos al update, ya que cuando pulsamos uno de los dos botones, queremos que cambie la posicion
		oWorld.update(delta, acelX);

		// Cambiamos las etiquetas siempre que el juego esta en marcha y cambie las variables
		lbCamarones.setText("x" + oWorld.camarones);
		lbDistancia.setText("Score " + oWorld.distanciaMax);

		// Si el estado del mundo es igual al estado muerto
		if (oWorld.state == WorldGame.STATE_GAMEOVER) {
			setGameover(); // Llamamos al metodo que cambia el estado del juego
		}
	}

	// Metodo de Juego Muerto
	private void updateGameOver(float delta) {
		oWorld.update(delta, 0); // Pasamos delta y la velocidad de Dory Horizontal a 0
	}

	// Metodo de dibujar la pantalla
	@Override
	public void draw(float delta) {
		// Siempre se dibuja lo siguente:
		batcher.begin();
		batcher.draw(Assets.fondo, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT); // Fondo de pantalla
		batcher.end();

		// Siempre se dibuja los Sprites cuando el juego no esta en pausa
		if (state != STATE_PAUSED) {
			spriteRenderer.render(delta);
		}
	}

	// Metodo de poner en pausa el juego
	private void setPaused() {
		if (state == STATE_RUNNING) { // Solo podemos pausar si el juego esta activo (No muerto)
			state = STATE_PAUSED; // Cambiar el estado del juego
			ventanPause.show(stage);  // Activar la ventana de Pausa
		}
	}

	// Metodo de poner el juego otra vez en marcha
	public void setRunning() {
		state = STATE_RUNNING; // Cambiar el estado a en Marcha (Vivo)
	}

	// Metodo de poner el juego a Muerto
	private void setGameover() {
		state = STATE_GAME_OVER; // Cambiar el estado

		// Actualizar información en Settings
		Settings.addGame(); // Añade 1 al total de juegos
		Settings.setBestScore(oWorld.distanciaMax); // Cambiamos el top score de partida
		Settings.addCamaronesTotal(oWorld.camarones); // Añadimor los camarones
		Settings.load();

		// Mostramos la ventana de game over
		new VentanaGameover(this).show(stage);
	}

	@Override
	public void hide() {
		super.hide(); // Hide de Screens
	}

	// Metodo si estas jugando en modo pc
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ESCAPE || keycode == Keys.BACK) { // Si tecla pulsado es Esc o Hacia Atras
			if (ventanPause.isVisible()) // Si la ventana de pausa esta visible
				ventanPause.hide(); // Hacer que desaparesca
			else // si no esta la ventana de pausa visible
				setPaused(); // Pausar el juego
			return true; // Volver verdadero porque se ha ejecutado codigo
		}
		return super.keyDown(keycode); // Devolver el metodo por boolean por defecto del metodo
	}

}
