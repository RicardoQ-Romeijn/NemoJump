package com.juego.nemojump.objetos;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.juego.nemojump.screens.Screens;
import com.juego.nemojump.utils.Assets;
import com.juego.nemojump.utils.Settings;

/*
 * Clase de Dory (Personaje que Controlamos)
 */
public class Dory {
	// Estado de las piezas de Dory
	public final static int STATE_NORMAL = 0;
	public final static int STATE_DEAD = 1;
	public int state;

	// Tamaño físico y del sprite que utilizaremos
	public final static float DRAW_WIDTH = .75f;
	public final static float DRAW_HEIGTH = .8f;
	public final static float WIDTH = .4f;
	public final static float HEIGTH = .6f;

	// Velocidades
	final float VELOCIDAD_JUMP = 7.5f; // Velocidad Vertical
	final float VELOCIDAD_X = 5; // Velocidad Horizontal

	// Posiciones, Velocidades y Angulo de Dory
	final public Vector2 position;
	public Vector2 velocidad;
	public float angleDeg;

	// Delta
	public float stateTime;

	// Boolean si ha Saltado
	boolean didJump;
	// Sonido del Salto
	private Sound jumpSound;

	// Constructor por defecto
	public Dory(float x, float y) {
		position = new Vector2(x, y); // Posicion
		velocidad = new Vector2(); // Vector instanciado

		stateTime = 0; // statetime instanciado
		state = STATE_NORMAL; // poner el estado a normal
		jumpSound = Assets.getJumpSound(); // Asignar el sonido sacado de Assets
	}

	// Metodo Update
	public void update(Body body, float delta, float acelX) {
		position.x = body.getPosition().x; // Actualizando Pos X
		position.y = body.getPosition().y; // Actualizando Pos Y

		velocidad = body.getLinearVelocity(); // Sacar la velocidad del cuerpo

		// Si está vivo
		if (state == STATE_NORMAL) {
			// Ha saltado dory en una plataforma
			if (didJump) {
				didJump = false; // resetear el salto
				stateTime = 0; // Poner el delta a 0

				// Poner la velocidad del cuerpo a la velocidad del salto
				velocidad.y = VELOCIDAD_JUMP;
			}

			// Velocidad Horizontal del cuerpo se multiplica su aceleración por la velocidad maxima que podemos ir
			velocidad.x = acelX * VELOCIDAD_X;
		} else { // Si no esta vivo
			// Hacer una animación que rote 360 grados
			body.setAngularVelocity(MathUtils.degRad * 360);
			// Poner la velocidad horizontal a 0
			velocidad.x = 0;
		}

		// Poner la velocidad del cuerpo dependiendo del if anterior
		body.setLinearVelocity(velocidad);

		// Si la posición de dory es mayor que el ancho del mundo
		if (position.x >= Screens.WORLD_WIDTH) {
			position.x = 0; // poner la posición al principio
			body.setTransform(position, 0);
		} else if (position.x <= 0) { // si la posicion de dory es menor que 0
			position.x = Screens.WORLD_WIDTH; // poner a dory al final de la pantalla
			body.setTransform(position, 0);
		}

		// Guradamos en grados el angulo del cuerpo
		angleDeg = body.getAngle() * MathUtils.radDeg;

		// Guardamos la velocidad del cuerpo
		velocidad = body.getLinearVelocity();
		// Guardamos delta en acumulo del cuerpo
		stateTime += delta;

	}

	// Metodo de salto
	public void jump() {
		didJump = true; // Cambiar el boolean ha saltado a verdad
		if (Settings.isSoundOn)
			jumpSound.play(); // Reproducir sonido de salto
	}

	// Metodo de muerto
	public void die() {
		if (state == STATE_NORMAL) { // Si esta vivio
			state = STATE_DEAD; // matalo
			stateTime = 0; // reiniciamos el statetime
		}
	}
}
