package com.juego.nemojump.objetos;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;
import com.juego.nemojump.screens.Screens;

/*
 * Clase de Camarones
 *
 * Esta clase se utiliza como un sistema de puntuación
 * Como puede ser una moneda, etc.
 */
public class Camarones implements Poolable {
	// Estado de las piezas de camarones / puntuacion
	public final static int STATE_NORMAL = 0;
	public final static int STATE_TAKEN = 1;
	public int state;

	// Tamaño físico y del sprite que utilizaremos
	public final static float DRAW_WIDTH = .27f;
	public final static float DRAW_HEIGHT = .34f;
	public final static float WIDTH = .25f;
	public final static float HEIGHT = .32f;

	// Posición del camaron
	public final Vector2 position;

	// Acumulador de Delta
	public float stateTime;

	// Constructor por defecto
	public Camarones() {
		position = new Vector2(); // Instanciamos la posicion
	}

	// Cuando initializamos un camaron, necesitamos su posicion
	public void init(float x, float y) {
		position.set(x, y); // guardamos la posicion
		state = STATE_NORMAL; // poner el estado a normal
		stateTime = 0; // reiniciar el acumulador
	}

	// Update donde acumulamos el delta
	public void update(float delta) {
		stateTime += delta;
	}

	// Metodo take, es cuando cogemos el camaron
	public void take() {
		state = STATE_TAKEN; // cambiamos el estado
		stateTime = 0; // reiniciamos el acumulador
	}

	// Metodo que crea la moneda
	public static void createCamaron(World worldBox, Array<Camarones> arrCamarones, float y) {
		// Sacamos el X aleatoriamente entre el tamaño del mundo, mas con el tamaño del camaron partido por 2
		float x = MathUtils.random(Screens.WORLD_WIDTH) + WIDTH / 2f;

		// Creamos un camaron a partir del pool e initializamos
		Camarones oCamarones = Pools.obtain(Camarones.class);
		oCamarones.init(x, y);

		// Creamos un Body Físico para el camaron
		BodyDef bd = new BodyDef();
		// Posicionamos el body
		bd.position.x = x;
		bd.position.y = y;
		// Un cuerpo estático, ya que no va a mover
		bd.type = BodyType.StaticBody;
		Body oBody = worldBox.createBody(bd);

		// El tipo de cuerpo que se creará, un cuadrado
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(WIDTH / 2f, HEIGHT / 2f);

		// Creamos el fixture para el camaron
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		oBody.createFixture(fixture);
		oBody.setUserData(oCamarones);
		// Borramos el tipo del cuerpo, ya que no lo necesitamos mas
		shape.dispose();
		// Ayadimos al array el camaron
		arrCamarones.add(oCamarones);
	}

	@Override
	public void reset() {

	}

}
