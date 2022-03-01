package com.juego.nemojump.game;

import java.util.Iterator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.juego.nemojump.objetos.Camarones;
import com.juego.nemojump.objetos.Dory;
import com.juego.nemojump.objetos.PiezaMedusa;
import com.juego.nemojump.objetos.Medusa;
import com.juego.nemojump.screens.Screens;
import com.juego.nemojump.utils.Assets;

/**
 * Esta clase contendrá la física del juego
 */
public class WorldGame {
	// Estados del juego
	final public static int STATE_RUNNING = 0;
	final public static int STATE_GAMEOVER = 1;
	int state;

	// Objeto del mundo del juego
	public World oWorldBox;

	// Objetos que saldrán en el juego
	Dory oDory;
	private Array<Body> arrBodies;
	Array<Medusa> arrMedusas;
	Array<PiezaMedusa> arrPiezasMedusas;
	Array<Camarones> arrCamarones;
	public int camarones;
	public int distanciaMax;
	float mundoCreadoHastaY;

	// Constructor de esta clase
	public WorldGame() {
		// Instanciamos el mundo que tendrá una gravedad de -9.8
		oWorldBox = new World(new Vector2(0, -9.8f), true);
		// Ponemos el contact listener a la clase colisiones (Definido al final de este fichero)
		oWorldBox.setContactListener(new Colisiones());

		// Instanciamos los arrays
		arrBodies = new Array<>();
		arrMedusas = new Array<>();
		arrPiezasMedusas = new Array<>();
		arrCamarones = new Array<>();

		// Ponemos el esta del juego en modo "marcha"
		state = STATE_RUNNING;

		// Creamos un suelo base y el personaje, que solo se creará una vez
		crearSuelo();
		crearDory();

		// Cogemos la posición Y del personaje, que nos indica la altura a la que ha llegado dory
		mundoCreadoHastaY = oDory.position.y;
		// Este metodo crea la sigiente parte del juego mediante vayamos avanzando verticalmente
		crearSiguienteParte();

	}

	/**
	 * Este metodo nos crea la siguente parte verticalmente , donde nos crea las plataformas
	 * y las monedas del mundo.
	 */
	private void crearSiguienteParte() {
		float y = mundoCreadoHastaY + 1f; // La distancia que queremos entre una plataforma y otra

		for (int i = 0; mundoCreadoHastaY < y; i++) {
			// A mediado que vamos avanzando tenemos que sobreescribir la altura maxima
			mundoCreadoHastaY = y + (i);
			// Creamos una plataforma
			crearMedusa(mundoCreadoHastaY);
			// Una probabilidad de crear los camarones
			if (MathUtils.random(20) < 5)
				Camarones.createCamaron(oWorldBox, arrCamarones, mundoCreadoHastaY + .5f);
		}
	}

	// Metodo de crear el suerlo. El suelo solo aparece 1 vez, al principio del juego
	private void crearSuelo() {
		BodyDef bd = new BodyDef(); // Creamos un objeto que definirá el cuerpo
		bd.type = BodyType.StaticBody; // Tipo estatico

		Body body = oWorldBox.createBody(bd); // Creamos el cuerpo del objeto en el mundo

		// El tipo de objeto que será, en este caso una linea recta que abarca en la parte inferior el ancho de la pantalla
		EdgeShape shape = new EdgeShape();
		shape.set(0, 0, Screens.WORLD_WIDTH, 0);

		// Tenemos que darle una fixtura al cuerpo
		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;

		// De lamos la fixtura al cuerpo, y le damos un nombre
		body.createFixture(fixutre);
		body.setUserData("suelo");
		// Borramos el tamaño porque no se va a utilizar mas
		shape.dispose();
	}

	// Metodo de crear el personaje
	private void crearDory() {
		// Creamos a nuestro personaje en la posicion indicada
		oDory = new Dory(2.4f, .5f);

		// La mayoría del codigo es igual que el Suelo, comentaré las parte que cambian.
		BodyDef bd = new BodyDef();
		bd.position.set(oDory.position.x, oDory.position.y);
		bd.type = BodyType.DynamicBody; // El cuerpo será de tipo dinamico porque el personaje tiene que moverse

		Body body = oWorldBox.createBody(bd);

		// El tipo de objeto será un cuadrado
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Dory.WIDTH / 2f, Dory.HEIGTH / 2f);

		// En la fixtura le tenemos que dar una densidad para que parezca que tenga peso
		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.density = 10;
		fixutre.friction = 0;
		fixutre.restitution = 0;

		body.createFixture(fixutre);
		body.setUserData(oDory); // nombre en este caso va a ser el Objeto Dory
		body.setFixedRotation(true); // El objeto no rotará

		shape.dispose();
	}

	// Metodo de crear la medusa
	private void crearMedusa(float y) {
		// Creamos un objeto medusa, y como hereda de Pooleable, lo instanciamos de la siguente manera:
		Medusa oMedusa = Pools.obtain(Medusa.class);
		// Initializamos la medusa, con la posicion como parametros
		oMedusa.init(MathUtils.random(Screens.WORLD_WIDTH), y);

		// La mayoría del codigo es igual que el Suelo y Dory, comentaré las parte que cambian.
		BodyDef bd = new BodyDef();
		bd.position.set(oMedusa.position.x, oMedusa.position.y);
		bd.type = BodyType.KinematicBody; // Tipo quinematico

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Medusa.WIDTH_NORMAL / 2f, Medusa.HEIGTH_NORMAL / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;

		body.createFixture(fixutre);
		body.setUserData(oMedusa); // De nombre le pasasmos el objeto de medusa
		arrMedusas.add(oMedusa); // Añadimos al array la medusa

		shape.dispose();
	}

	// Metodo que nos creará ambas partes de las piezas
	private void crearPiezasPlataforma(Medusa oPlat) {
		// Llama al metodo que nos crea la física de cada parte
		crearPiezasPlataforma(oPlat, PiezaMedusa.TIPO_LEFT);
		crearPiezasPlataforma(oPlat, PiezaMedusa.TIPO_RIGHT);

	}

	// Metodo que crea las piezas de medusa individualmente
	private void crearPiezasPlataforma(Medusa oPla, int tipo) {
		float x; // La posición x de la medusa
		float angularVelocity = 100; // La velocidad de rotación que tendrá

		// Si es la medusa izquierda
		if (tipo == PiezaMedusa.TIPO_LEFT) {
			x = oPla.position.x - PiezaMedusa.WIDTH_NORMAL / 2f; // La parte de cada medusa es la mitad del tamaño que el original
			angularVelocity *= -1; // La velocidad la pasamos a negativo para que rote al lado contrario
		} else { // La parte derecha
			x = oPla.position.x + PiezaMedusa.WIDTH_NORMAL / 2f;
		}

		// Creamos el objeto a partir del modelo
		PiezaMedusa oPieza = Pools.obtain(PiezaMedusa.class);
		// Llamamos al metodo que initializa el objeto
		oPieza.init(x, oPla.position.y, tipo);

		// Creamos la definición del cuerpo
		BodyDef bd = new BodyDef();
		bd.position.set(oPieza.position.x, oPieza.position.y);
		bd.type = BodyType.DynamicBody; // Tipo dinamico porque se moverá

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(PiezaMedusa.WIDTH_NORMAL / 2f, PiezaMedusa.HEIGTH_NORMAL / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true; // Lo ponemos como sensor porque queremos que no tenga colisión

		body.createFixture(fixutre);
		body.setUserData(oPieza);
		body.setAngularVelocity(MathUtils.degRad * angularVelocity); // Damos la velocidad  angular al cuerpo
		arrPiezasMedusas.add(oPieza); // Añadimos al array la parte

		shape.dispose();
	}

	// Metodo update
	public void update(float delta, float acelX) {
		// Llamamos a step. Este se encarga de hacer la detección de colisiones, la integración y la solucón de los constrains
		oWorldBox.step(delta, 8, 4);

		// Llamamos al metodo que nos limipia los objetos que ya no utlizamos o no estan en pantalla
		eliminarObjetos();

		// Reviso si es necesario generar la siquiete parte del mundo
		if (oDory.position.y + 10 > mundoCreadoHastaY) {
			crearSiguienteParte();
		}

		// Recorremos todos los objetos del mundo, para hacer su update
		oWorldBox.getBodies(arrBodies);
		Iterator<Body> i = arrBodies.iterator();

		while (i.hasNext()) {
			Body body = i.next();
			// Hacemos metodos para cada instancia
			if (body.getUserData() instanceof Dory) {
				updateDory(body, delta, acelX);
			}
			else if (body.getUserData() instanceof Medusa) {
				updateMedusa(body, delta);
			}
			else if (body.getUserData() instanceof PiezaMedusa) {
				updatePiezaMedusa(body, delta);
			}
			else if (body.getUserData() instanceof Camarones) {
				updateCamarones(body, delta);
			}
		}

		// Vamos aumentando la distancia maxima que ha recorrido Dory
		if (distanciaMax < (oDory.position.y * 10)) {
			distanciaMax = (int) (oDory.position.y * 10);
		}

		// Si el personaje esta 5.5f mas abajo de la altura maxima se muere
		// (Se multiplica por 10 porque la distancia se multiplica por 10 )
		if (oDory.state == Dory.STATE_NORMAL && distanciaMax - (5.5f * 10) > (oDory.position.y * 10)) {
			oDory.die();
		}
		// Ponemos el estado del juego a muerto si el estado de dory esta muerto y el personaje esta 5.5f mas abajo de la altura maxima
		if (oDory.state == Dory.STATE_DEAD && distanciaMax - (25 * 10) > (oDory.position.y * 10)) {
			state = STATE_GAMEOVER;
		}
	}

	// Metodo de eliminar los objetos, para opitimizar el rendimiento
	private void eliminarObjetos() {
		// Cogemos todos los cuerpos en el juego
		oWorldBox.getBodies(arrBodies);
		Iterator<Body> i = arrBodies.iterator();
		// Recorremons el iterator
		while (i.hasNext()) {
			// Creamos el objeto base
			Body body = i.next(); // Recordamos que el data del cuerpo tiene el objeto instanciado

			// Si el mundo esta abierto para modificaciones
			if (!oWorldBox.isLocked()) {
				if (body.getUserData() instanceof Medusa) { // Si es instancia de medusa
					Medusa oPlat = (Medusa) body.getUserData();
					if (oPlat.state == Medusa.STATE_DESTROY) { // Si el estado de medusa esta en muerto / romper
						arrMedusas.removeValue(oPlat, true); // Borramos el objeto del array
						oWorldBox.destroyBody(body); // Borramos el objeto del mundo
						if (oPlat.tipo == Medusa.TIPO_ROMPIBLE) // Si la medusa era de tipo rompible, llamamos a crear las piezas
							crearPiezasPlataforma(oPlat);
						Pools.free(oPlat); // Como medusa hereda de Pooleable, tambien llamamos al metodo free
					}
				} else if (body.getUserData() instanceof Camarones) { // Si es instancia de camarones
					Camarones oMon = (Camarones) body.getUserData();
					if (oMon.state == Camarones.STATE_TAKEN) { // Si el camarón lo hemos cogido
						arrCamarones.removeValue(oMon, true); // Borramos del array el objeto
						oWorldBox.destroyBody(body); // Borramos del mundo el objeto
						Pools.free(oMon); // Como camaron hereda de Pooleable, tambien llamamos al metodo free
					}
				} else if (body.getUserData() instanceof PiezaMedusa) { // Si es instancia de pieza medusa
					PiezaMedusa oPiez = (PiezaMedusa) body.getUserData();
					if (oPiez.state == PiezaMedusa.STATE_DESTROY) { // Si el estado de medusa esta en muerto / romper
						arrPiezasMedusas.removeValue(oPiez, true); // Borramos el objeto del array
						oWorldBox.destroyBody(body); // Borramos el objeto del mundo
						Pools.free(oPiez); // Como medusa hereda de Pooleable, tambien llamamos al metodo free
					}
				} else if (body.getUserData().equals("suelo")) { // Si es el Suelo creado al principio del juego
					if (oDory.position.y - 5.5f > body.getPosition().y || oDory.state == Dory.STATE_DEAD) { // Si dory ha muerto y está por debajo de la posicion indicado
						oWorldBox.destroyBody(body); // Borramos el objeto
					}
				}
			}
		}
	}

	// Metodo update para Dory
	private void updateDory(Body body, float delta, float acelX) {
		oDory.update(body, delta, acelX); // Llamamos al update de Dory
	}

	// Metodo update de Medusa
	private void updateMedusa(Body body, float delta) {
		Medusa obj = (Medusa) body.getUserData(); // Cogemos el objeto guardado en los datos
		obj.update(delta); // llamamos al metodo update del modelo
		if (oDory.position.y - 5.5f > obj.position.y) { // Si la posición de dory (+5.5) es mayor que la medusa
			obj.setDestroy(); // Llamamos al metodo que cambia el estado de la medusa
		}
	}

	// Metodo update de las piezas de medusa
	private void updatePiezaMedusa(Body body, float delta) {
		PiezaMedusa obj = (PiezaMedusa) body.getUserData(); // Cogemos el objeto guardado en los datos
		obj.update(delta, body);  // llamamos al metodo update del modelo
		if (oDory.position.y - 5.5f > obj.position.y) { // Si la posición de dory (+5.5) es mayor que la de pieza medusa
			obj.setDestroy(); // Llamamos al metodo que cambia el estado de la medusa
		}

	}

	private void updateCamarones(Body body, float delta) {
		Camarones obj = (Camarones) body.getUserData(); // Cogemos el objeto guardado en los datos
		obj.update(delta); // llamamos al metodo update del modelo
		if (oDory.position.y - 5.5f > obj.position.y) { // Si la posición de dory (+5.5) es mayor que la de pieza medusa
			obj.take(); // Llamamos al metodo que cambia el estado del camaron
		}

	}

	/**
	 * Clase de Colisiones para la física que implementa el ContactListener
	 */
	class Colisiones implements ContactListener {

		// Metodo que se ejecuta cuando hay una colisión
		@Override
		public void beginContact(Contact contact) {
			Fixture a = contact.getFixtureA(); // Cogemos la fixtura de A
			Fixture b = contact.getFixtureB(); // Cogemos la fixtura de B

			if (a.getBody().getUserData() instanceof Dory) // Si la Fixtura A es instancia de Dory
				beginContactPersonaje(a, b); // Llamamos al metodo que contendrá la logica de la colisión
			else if (b.getBody().getUserData() instanceof Dory) // Si la Fixtura B es instancia de Dory
				beginContactPersonaje(b, a); // Llamamos al metodo que contendrá la logica de la colisión
		}

		// Metodo que contiene la logica de colisión
		private void beginContactPersonaje(Fixture fixDory, Fixture fixOtroObjeto) {
			Object otraCosa = fixOtroObjeto.getBody().getUserData(); // Cogemos el objeto que está en Data

			// Si es el suelo que creamos al principio
			if (otraCosa.equals("suelo")) {
				oDory.jump(); // Llamamos al metodo Saltar

				if (oDory.state == Dory.STATE_DEAD) { // Si Dory esta muerto
					state = STATE_GAMEOVER; // Cambiamos el estado del juego a terminado
				}
			} else if (otraCosa instanceof Medusa) { // Si es instancia de Medusa
				Medusa medusa = (Medusa) otraCosa; // Hacemos un casting al Object

				if (oDory.velocidad.y <= 0) { // Si Dory está cayendo hacia bajo
					if (medusa.tipo != Medusa.TIPO_ROMPIBLE) { // Si la medusa no es tipo rompible
						oDory.jump(); // Llamamos al metodo que hace saltar a Dory
					} else { // Si la medusa es de tipo rompible
						medusa.setDestroy(); // Rompemos a la medusa
					}
				}
			} else if (otraCosa instanceof Camarones) { // Si es instancia de Camarones
				Camarones camarones = (Camarones) otraCosa; // Hacemos un casting al Objeto
				camarones.take(); // Llamamos al metodo de coger el camaron
				WorldGame.this.camarones++; // Añadimos a camarones 1
				//oPer.jump(); // Si queremos que salte cuando cogemos un camaron, descomentamos
			}
		}

		@Override
		public void endContact(Contact contact) {

		}

		// Metodo que se ejecuta antes de llamar a @beginContact
		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			// Cogemos ambas fixturas
			Fixture a = contact.getFixtureA();
			Fixture b = contact.getFixtureB();

			// Dependiendo si son instancias de Dory, llamamos al metodo en un orden o en otro
			if (a.getBody().getUserData() instanceof Dory)
				preSolveHero(a, b, contact);
			else if (b.getBody().getUserData() instanceof Dory)
				preSolveHero(b, a, contact);
		}

		private void preSolveHero(Fixture fixDory, Fixture fixOtraCosa, Contact contact) {
			Object oOtraCosa = fixOtraCosa.getBody().getUserData(); // Creamos un objeto base a partir de la colisión

			if (oOtraCosa instanceof Medusa) { // Si es instancia de Medusa

				// Si Dory va para arriba atraviesa la plataforma
				Medusa obj = (Medusa) oOtraCosa;

				float posYDory = fixDory.getBody().getPosition().y - .30f; // Posición de Dory
				float posYMedusa = obj.position.y + Medusa.HEIGTH_NORMAL / 2f; // Posición de Medusa + la altura

				if (posYDory < posYMedusa) // Si la posición de dory es menor que la de la medusa
					contact.setEnabled(false); // Quitamos el contact, para que no haya colisión

				if (obj.tipo == Medusa.TIPO_NORMAL && oDory.state == Dory.STATE_DEAD)  // Si está Dory muerto
					contact.setEnabled(false); // Quitamos el contact, para que no haya colisión
			}
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {

		}

	}

}
