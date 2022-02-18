package com.juego.nemojump.objetos;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;

/*
* Clase para las piezas de Medusa
* (Las medusas que no se puede saltar)
*
* Esta clase nada mas se utiliza para "animar" las plataformas
* a la que no se puede saltar, se destrozará en 2 piezas
* rotará uno a cada lado correspondiente, e desaparecerán
*/
public class PiezaMedusa implements Poolable {
	// Estado de las piezas de medusa
	public final static int STATE_NORMAL = 0;
	public final static int STATE_DESTROY = 1;
	public int state;

	// Tamaño Físico y Tamaño Sprite
	// Hay que tener en cuenta que, cuando se crea las piezas, es el mismo alto
	// pero el ancho es la mitad, ya que crearemos 2 piezar por medusa
	public static final float DRAW_WIDTH_NORMAL = Medusa.DRAW_WIDTH_NORMAL / 2f;
	public static final float DRAW_HEIGTH_NORMAL = Medusa.DRAW_HEIGTH_NORMAL;
	public static final float WIDTH_NORMAL = Medusa.WIDTH_NORMAL / 2f;
	public static final float HEIGTH_NORMAL = Medusa.HEIGTH_NORMAL;

	// Cual de las dos piezas es la medusa creada (derecha o izquierda)
	public static final int TIPO_LEFT = 0;
	public static final int TIPO_RIGHT = 1;
	public int tipo;

	// Variable de Posición
	public final Vector2 position;

	// El tiempo delta y el angulo a la que rotará en la "animacion"
	public float stateTime;
	public float angleDeg;

	// Constructor, donde initualizamos la posicion
	public PiezaMedusa() {
		position = new Vector2();
	}

	// Cuando llamamos a init, va a hacer lo siguiente
	public void init(float x, float y, int tipo) {
		position.set(x, y); // poner la posicion
		this.tipo = tipo; // asignar el tipo (derecha o izquierda)
		angleDeg = 0; // el angulo al crearse es 0
		stateTime = 0; // state time es 0 porque no ha hecho nada
		state = STATE_NORMAL; // estado "vivo" o en pantalla
	}

	// Cuando llamamos a update, simulará la animación
	public void update(float delta, Body body) {
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;
		// Modificamos el angulo de la física para rotarlo
		angleDeg = MathUtils.radiansToDegrees * body.getAngle();

		// si es mayor que 990
		if (angleDeg > 90) {
			// Rotamos el cuerpo, hasta que llege a 90
			body.setTransform(position, MathUtils.degreesToRadians * 90);
			angleDeg = 90;
		}

		// Modificamos el statetime (delta)
		stateTime += delta;
	}

	@Override
	public void reset() {

	}

	// cuando llamamos a setDestroy, ya hemos terminado la animación,
	// y cambiamos el estado para borrarlo posteriormente
	public void setDestroy() {
		state = STATE_DESTROY;
	}
}
