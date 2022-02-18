package com.juego.nemojump.objetos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

/*
 * Clase de Medusa
 *
 * Esta clase se utiliza como "plataforma" para saltar
 */
public class Medusa implements Poolable {
	// Estado de las piezas de medusa
	public final static int STATE_NORMAL = 0;
	public final static int STATE_DESTROY = 1;
	public int state;

	// El Tipo de plataforma, normal o rompible / falso
	public static final int TIPO_NORMAL = 0;
	public static final int TIPO_ROMPIBLE = 1;
	public int tipo;

	// Tamaño físico y del sprite que utilizaremos
	public static final float DRAW_WIDTH_NORMAL = 1f;
	public static final float DRAW_HEIGTH_NORMAL = .6f;
	public static final float WIDTH_NORMAL = 1f;
	public static final float HEIGTH_NORMAL = .6f;

	// Posicion y el Delta
	public final Vector2 position;
	public float stateTime;

	// Constructor por defecto donde instanciamos la posicion
	public Medusa() {
		position = new Vector2();
	}

	// Cuando llamamos a init, va a hacer lo siguiente
	public void init(float x, float y, int tipo) {
		position.set(x, y); // pone la posicion

		// El tipo introducido, será un rango desde 1 a 10,
		// Significa que 1 de cada 10 va a ser de una medusa "falsa"
		// (Medusa la que no da impulso)
		if (tipo == 1) {
			this.tipo = TIPO_ROMPIBLE;
		} else {
			this.tipo = TIPO_NORMAL;
		}

		// Estado "vivo" o normal, e decir el statetime (delta) es 0
		state = STATE_NORMAL;
		stateTime = 0;
	}

	// Cuando llamamos a update, sobreescribe el estatetime que es nuestro delta
	public void update(float delta) {
		stateTime += delta;
	}

	// Cuando llamamos a este metodo, queremos borrar la plataforma
	// Solo lo queremos borrar si está en estado normal
	public void setDestroy() {
		if (state == STATE_NORMAL) {
			state = STATE_DESTROY;
			stateTime = 0;
		}
	}

	@Override
	public void reset() {

	}

}
