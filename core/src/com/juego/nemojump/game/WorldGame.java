package com.juego.superjumper.game;

import java.util.Iterator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
import com.juego.superjumper.objetos.Moneda;
import com.juego.superjumper.objetos.Personaje;
import com.juego.superjumper.objetos.PiezaPlataformas;
import com.juego.superjumper.objetos.Plataformas;
import com.juego.superjumper.screens.Screens;

public class WorldGame {
	final public static int STATE_RUNNING = 0;
	final public static int STATE_GAMEOVER = 1;
	int state;

	public World oWorldBox;

	Personaje oPer;
	private Array<Body> arrBodies;
	Array<Plataformas> arrPlataformas;
	Array<PiezaPlataformas> arrPiezasPlataformas;
	Array<Moneda> arrMonedas;
	public int coins;
	public int distanciaMax;
	float mundoCreadoHastaY;

	public WorldGame() {
		oWorldBox = new World(new Vector2(0, -9.8f), true);
		oWorldBox.setContactListener(new Colisiones());

		arrBodies = new Array<>();
		arrPlataformas = new Array<>();
		arrPiezasPlataformas = new Array<>();
		arrMonedas = new Array<>();

		state = STATE_RUNNING;

		crearSuelo();
		crearPersonaje();

		mundoCreadoHastaY = oPer.position.y;
		crearSiguienteParte();

	}

	private void crearSiguienteParte() {
		float y = mundoCreadoHastaY + 2;

		for (int i = 0; mundoCreadoHastaY < (y + 10); i++) {
			mundoCreadoHastaY = y + (i);

			crearPlataforma(mundoCreadoHastaY);

			if (MathUtils.random(20) < 5)
				Moneda.createUnaMoneda(oWorldBox, arrMonedas, mundoCreadoHastaY + .5f);

		}

	}

	/**
	 * El piso solo aparece 1 vez, al principio del juego
	 */
	private void crearSuelo() {
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;

		Body body = oWorldBox.createBody(bd);

		EdgeShape shape = new EdgeShape();
		shape.set(0, 0, Screens.WORLD_WIDTH, 0);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;

		body.createFixture(fixutre);
		body.setUserData("suelo");

		shape.dispose();

	}

	private void crearPersonaje() {
		oPer = new Personaje(2.4f, .5f);

		BodyDef bd = new BodyDef();
		bd.position.set(oPer.position.x, oPer.position.y);
		bd.type = BodyType.DynamicBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Personaje.WIDTH / 2f, Personaje.HEIGTH / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.density = 10;
		fixutre.friction = 0;
		fixutre.restitution = 0;

		body.createFixture(fixutre);
		body.setUserData(oPer);
		body.setFixedRotation(true);

		shape.dispose();
	}

	private void crearPlataforma(float y) {

		Plataformas oPlat = Pools.obtain(Plataformas.class);
		oPlat.init(MathUtils.random(Screens.WORLD_WIDTH), y, MathUtils.random(10));

		BodyDef bd = new BodyDef();
		bd.position.set(oPlat.position.x, oPlat.position.y);
		bd.type = BodyType.KinematicBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Plataformas.WIDTH_NORMAL / 2f, Plataformas.HEIGTH_NORMAL / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;

		body.createFixture(fixutre);
		body.setUserData(oPlat);
		arrPlataformas.add(oPlat);

		shape.dispose();

	}

	private void crearPiezasPlataforma(Plataformas oPlat) {
		crearPiezasPlataforma(oPlat, PiezaPlataformas.TIPO_LEFT);
		crearPiezasPlataforma(oPlat, PiezaPlataformas.TIPO_RIGHT);

	}

	private void crearPiezasPlataforma(Plataformas oPla, int tipo) {
		PiezaPlataformas oPieza;
		float x;
		float angularVelocity = 100;

		if (tipo == PiezaPlataformas.TIPO_LEFT) {
			x = oPla.position.x - PiezaPlataformas.WIDTH_NORMAL / 2f;
			angularVelocity *= -1;
		}
		else {
			x = oPla.position.x + PiezaPlataformas.WIDTH_NORMAL / 2f;
		}

		oPieza = Pools.obtain(PiezaPlataformas.class);
		oPieza.init(x, oPla.position.y, tipo, oPla.color);

		BodyDef bd = new BodyDef();
		bd.position.set(oPieza.position.x, oPieza.position.y);
		bd.type = BodyType.DynamicBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(PiezaPlataformas.WIDTH_NORMAL / 2f, PiezaPlataformas.HEIGTH_NORMAL / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;

		body.createFixture(fixutre);
		body.setUserData(oPieza);
		body.setAngularVelocity(MathUtils.degRad * angularVelocity);
		arrPiezasPlataformas.add(oPieza);

		shape.dispose();
	}

	public void update(float delta, float acelX, Vector3 touchPositionWorldCoords) {
		oWorldBox.step(delta, 8, 4);

		eliminarObjetos();

		/**
		 * Reviso si es necesario generar la siquiete parte del mundo
		 */
		if (oPer.position.y + 10 > mundoCreadoHastaY) {
			crearSiguienteParte();
		}

		oWorldBox.getBodies(arrBodies);
		Iterator<Body> i = arrBodies.iterator();

		while (i.hasNext()) {
			Body body = i.next();
			if (body.getUserData() instanceof Personaje) {
				updatePersonaje(body, delta, acelX, touchPositionWorldCoords);
			}
			else if (body.getUserData() instanceof Plataformas) {
				updatePlataforma(body, delta);
			}
			else if (body.getUserData() instanceof PiezaPlataformas) {
				updatePiezaPlataforma(body, delta);
			}
			else if (body.getUserData() instanceof Moneda) {
				updateMoneda(body, delta);
			}

		}

		if (distanciaMax < (oPer.position.y * 10)) {
			distanciaMax = (int) (oPer.position.y * 10);
		}

		// Si el personaje esta 5.5f mas abajo de la altura maxima se muere (Se multiplica por 10 porque la distancia se multiplica por 10 )
		if (oPer.state == Personaje.STATE_NORMAL && distanciaMax - (5.5f * 10) > (oPer.position.y * 10)) {
			oPer.die();
		}
		if (oPer.state == Personaje.STATE_DEAD && distanciaMax - (25 * 10) > (oPer.position.y * 10)) {
			state = STATE_GAMEOVER;
		}

	}

	private void eliminarObjetos() {
		oWorldBox.getBodies(arrBodies);
		Iterator<Body> i = arrBodies.iterator();

		while (i.hasNext()) {
			Body body = i.next();

			if (!oWorldBox.isLocked()) {

				if (body.getUserData() instanceof Plataformas) {
					Plataformas oPlat = (Plataformas) body.getUserData();
					if (oPlat.state == Plataformas.STATE_DESTROY) {
						arrPlataformas.removeValue(oPlat, true);
						oWorldBox.destroyBody(body);
						if (oPlat.tipo == Plataformas.TIPO_ROMPIBLE)
							crearPiezasPlataforma(oPlat);
						Pools.free(oPlat);
					}
				}
				else if (body.getUserData() instanceof Moneda) {
					Moneda oMon = (Moneda) body.getUserData();
					if (oMon.state == Moneda.STATE_TAKEN) {
						arrMonedas.removeValue(oMon, true);
						oWorldBox.destroyBody(body);
						Pools.free(oMon);
					}
				}
				else if (body.getUserData() instanceof PiezaPlataformas) {
					PiezaPlataformas oPiez = (PiezaPlataformas) body.getUserData();
					if (oPiez.state == PiezaPlataformas.STATE_DESTROY) {
						arrPiezasPlataformas.removeValue(oPiez, true);
						oWorldBox.destroyBody(body);
						Pools.free(oPiez);
					}
				}
				else if (body.getUserData().equals("suelo")) {
					if (oPer.position.y - 5.5f > body.getPosition().y || oPer.state == Personaje.STATE_DEAD) {
						oWorldBox.destroyBody(body);
					}
				}
			}
		}
	}

	private void updatePersonaje(Body body, float delta, float acelX, Vector3 touchPositionWorldCoords) {
		oPer.update(body, delta, acelX);
	}

	private void updatePlataforma(Body body, float delta) {
		Plataformas obj = (Plataformas) body.getUserData();
		obj.update(delta);
		if (oPer.position.y - 5.5f > obj.position.y) {
			obj.setDestroy();
		}
	}

	private void updatePiezaPlataforma(Body body, float delta) {
		PiezaPlataformas obj = (PiezaPlataformas) body.getUserData();
		obj.update(delta, body);
		if (oPer.position.y - 5.5f > obj.position.y) {
			obj.setDestroy();
		}

	}

	private void updateMoneda(Body body, float delta) {
		Moneda obj = (Moneda) body.getUserData();
		obj.update(delta);
		if (oPer.position.y - 5.5f > obj.position.y) {
			obj.take();
		}

	}

	class Colisiones implements ContactListener {

		@Override
		public void beginContact(Contact contact) {
			Fixture a = contact.getFixtureA();
			Fixture b = contact.getFixtureB();

			if (a.getBody().getUserData() instanceof Personaje)
				beginContactPersonaje(a, b);
			else if (b.getBody().getUserData() instanceof Personaje)
				beginContactPersonaje(b, a);

		}

		private void beginContactPersonaje(Fixture fixPersonaje, Fixture fixOtraCosa) {
			Object otraCosa = fixOtraCosa.getBody().getUserData();

			if (otraCosa.equals("suelo")) {
				oPer.jump();

				if (oPer.state == Personaje.STATE_DEAD) {
					state = STATE_GAMEOVER;
				}
			}
			else if (otraCosa instanceof Plataformas) {
				Plataformas plataforma = (Plataformas) otraCosa;

				if (oPer.velocidad.y <= 0) {
					if (plataforma.tipo != Plataformas.TIPO_ROMPIBLE) {
						oPer.jump();
					} else {
						plataforma.setDestroy();
					}
				}

			}
			else if (otraCosa instanceof Moneda) {
				Moneda moneda = (Moneda) otraCosa;
				moneda.take();
				coins++;
				//oPer.jump();
			}

		}

		@Override
		public void endContact(Contact contact) {

		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			Fixture a = contact.getFixtureA();
			Fixture b = contact.getFixtureB();

			if (a.getBody().getUserData() instanceof Personaje)
				preSolveHero(a, b, contact);
			else if (b.getBody().getUserData() instanceof Personaje)
				preSolveHero(b, a, contact);

		}

		private void preSolveHero(Fixture fixPersonaje, Fixture otraCosa, Contact contact) {
			Object oOtraCosa = otraCosa.getBody().getUserData();

			if (oOtraCosa instanceof Plataformas) {
				// Si va para arriba atraviesa la plataforma

				Plataformas obj = (Plataformas) oOtraCosa;

				float ponyY = fixPersonaje.getBody().getPosition().y - .30f;
				float pisY = obj.position.y + Plataformas.HEIGTH_NORMAL / 2f;

				if (ponyY < pisY)
					contact.setEnabled(false);

				if (obj.tipo == Plataformas.TIPO_NORMAL && oPer.state == Personaje.STATE_DEAD) {
					contact.setEnabled(false);
				}

			}

		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {

		}

	}

}
