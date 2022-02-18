package com.juego.nemojump.game;

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
import com.juego.nemojump.objetos.Camarones;
import com.juego.nemojump.objetos.Dory;
import com.juego.nemojump.objetos.PiezaMedusa;
import com.juego.nemojump.objetos.Medusa;
import com.juego.nemojump.screens.Screens;

public class WorldGame {
	final public static int STATE_RUNNING = 0;
	final public static int STATE_GAMEOVER = 1;
	int state;

	public World oWorldBox;

	Dory oPer;
	private Array<Body> arrBodies;
	Array<Medusa> arrMedusas;
	Array<PiezaMedusa> arrPiezasMedusas;
	Array<Camarones> arrCamarones;
	public int camarones;
	public int distanciaMax;
	float mundoCreadoHastaY;

	public WorldGame() {
		oWorldBox = new World(new Vector2(0, -9.8f), true);
		oWorldBox.setContactListener(new Colisiones());

		arrBodies = new Array<>();
		arrMedusas = new Array<>();
		arrPiezasMedusas = new Array<>();
		arrCamarones = new Array<>();

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
				Camarones.createMoneda(oWorldBox, arrCamarones, mundoCreadoHastaY + .5f);

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
		oPer = new Dory(2.4f, .5f);

		BodyDef bd = new BodyDef();
		bd.position.set(oPer.position.x, oPer.position.y);
		bd.type = BodyType.DynamicBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Dory.WIDTH / 2f, Dory.HEIGTH / 2f);

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

		Medusa oPlat = Pools.obtain(Medusa.class);
		oPlat.init(MathUtils.random(Screens.WORLD_WIDTH), y, MathUtils.random(10));

		BodyDef bd = new BodyDef();
		bd.position.set(oPlat.position.x, oPlat.position.y);
		bd.type = BodyType.KinematicBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Medusa.WIDTH_NORMAL / 2f, Medusa.HEIGTH_NORMAL / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;

		body.createFixture(fixutre);
		body.setUserData(oPlat);
		arrMedusas.add(oPlat);

		shape.dispose();

	}

	private void crearPiezasPlataforma(Medusa oPlat) {
		crearPiezasPlataforma(oPlat, PiezaMedusa.TIPO_LEFT);
		crearPiezasPlataforma(oPlat, PiezaMedusa.TIPO_RIGHT);

	}

	private void crearPiezasPlataforma(Medusa oPla, int tipo) {
		PiezaMedusa oPieza;
		float x;
		float angularVelocity = 100;

		if (tipo == PiezaMedusa.TIPO_LEFT) {
			x = oPla.position.x - PiezaMedusa.WIDTH_NORMAL / 2f;
			angularVelocity *= -1;
		}
		else {
			x = oPla.position.x + PiezaMedusa.WIDTH_NORMAL / 2f;
		}

		oPieza = Pools.obtain(PiezaMedusa.class);
		oPieza.init(x, oPla.position.y, tipo);

		BodyDef bd = new BodyDef();
		bd.position.set(oPieza.position.x, oPieza.position.y);
		bd.type = BodyType.DynamicBody;

		Body body = oWorldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(PiezaMedusa.WIDTH_NORMAL / 2f, PiezaMedusa.HEIGTH_NORMAL / 2f);

		FixtureDef fixutre = new FixtureDef();
		fixutre.shape = shape;
		fixutre.isSensor = true;

		body.createFixture(fixutre);
		body.setUserData(oPieza);
		body.setAngularVelocity(MathUtils.degRad * angularVelocity);
		arrPiezasMedusas.add(oPieza);

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
			if (body.getUserData() instanceof Dory) {
				updatePersonaje(body, delta, acelX, touchPositionWorldCoords);
			}
			else if (body.getUserData() instanceof Medusa) {
				updatePlataforma(body, delta);
			}
			else if (body.getUserData() instanceof PiezaMedusa) {
				updatePiezaPlataforma(body, delta);
			}
			else if (body.getUserData() instanceof Camarones) {
				updateMoneda(body, delta);
			}

		}

		if (distanciaMax < (oPer.position.y * 10)) {
			distanciaMax = (int) (oPer.position.y * 10);
		}

		// Si el personaje esta 5.5f mas abajo de la altura maxima se muere (Se multiplica por 10 porque la distancia se multiplica por 10 )
		if (oPer.state == Dory.STATE_NORMAL && distanciaMax - (5.5f * 10) > (oPer.position.y * 10)) {
			oPer.die();
		}
		if (oPer.state == Dory.STATE_DEAD && distanciaMax - (25 * 10) > (oPer.position.y * 10)) {
			state = STATE_GAMEOVER;
		}

	}

	private void eliminarObjetos() {
		oWorldBox.getBodies(arrBodies);
		Iterator<Body> i = arrBodies.iterator();

		while (i.hasNext()) {
			Body body = i.next();

			if (!oWorldBox.isLocked()) {

				if (body.getUserData() instanceof Medusa) {
					Medusa oPlat = (Medusa) body.getUserData();
					if (oPlat.state == Medusa.STATE_DESTROY) {
						arrMedusas.removeValue(oPlat, true);
						oWorldBox.destroyBody(body);
						if (oPlat.tipo == Medusa.TIPO_ROMPIBLE)
							crearPiezasPlataforma(oPlat);
						Pools.free(oPlat);
					}
				}
				else if (body.getUserData() instanceof Camarones) {
					Camarones oMon = (Camarones) body.getUserData();
					if (oMon.state == Camarones.STATE_TAKEN) {
						arrCamarones.removeValue(oMon, true);
						oWorldBox.destroyBody(body);
						Pools.free(oMon);
					}
				}
				else if (body.getUserData() instanceof PiezaMedusa) {
					PiezaMedusa oPiez = (PiezaMedusa) body.getUserData();
					if (oPiez.state == PiezaMedusa.STATE_DESTROY) {
						arrPiezasMedusas.removeValue(oPiez, true);
						oWorldBox.destroyBody(body);
						Pools.free(oPiez);
					}
				}
				else if (body.getUserData().equals("suelo")) {
					if (oPer.position.y - 5.5f > body.getPosition().y || oPer.state == Dory.STATE_DEAD) {
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
		Medusa obj = (Medusa) body.getUserData();
		obj.update(delta);
		if (oPer.position.y - 5.5f > obj.position.y) {
			obj.setDestroy();
		}
	}

	private void updatePiezaPlataforma(Body body, float delta) {
		PiezaMedusa obj = (PiezaMedusa) body.getUserData();
		obj.update(delta, body);
		if (oPer.position.y - 5.5f > obj.position.y) {
			obj.setDestroy();
		}

	}

	private void updateMoneda(Body body, float delta) {
		Camarones obj = (Camarones) body.getUserData();
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

			if (a.getBody().getUserData() instanceof Dory)
				beginContactPersonaje(a, b);
			else if (b.getBody().getUserData() instanceof Dory)
				beginContactPersonaje(b, a);

		}

		private void beginContactPersonaje(Fixture fixPersonaje, Fixture fixOtraCosa) {
			Object otraCosa = fixOtraCosa.getBody().getUserData();

			if (otraCosa.equals("suelo")) {
				oPer.jump();

				if (oPer.state == Dory.STATE_DEAD) {
					state = STATE_GAMEOVER;
				}
			}
			else if (otraCosa instanceof Medusa) {
				Medusa plataforma = (Medusa) otraCosa;

				if (oPer.velocidad.y <= 0) {
					if (plataforma.tipo != Medusa.TIPO_ROMPIBLE) {
						oPer.jump();
					} else {
						plataforma.setDestroy();
					}
				}

			}
			else if (otraCosa instanceof Camarones) {
				Camarones camarones = (Camarones) otraCosa;
				camarones.take();
				WorldGame.this.camarones++;
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

			if (a.getBody().getUserData() instanceof Dory)
				preSolveHero(a, b, contact);
			else if (b.getBody().getUserData() instanceof Dory)
				preSolveHero(b, a, contact);

		}

		private void preSolveHero(Fixture fixPersonaje, Fixture otraCosa, Contact contact) {
			Object oOtraCosa = otraCosa.getBody().getUserData();

			if (oOtraCosa instanceof Medusa) {
				// Si va para arriba atraviesa la plataforma

				Medusa obj = (Medusa) oOtraCosa;

				float ponyY = fixPersonaje.getBody().getPosition().y - .30f;
				float pisY = obj.position.y + Medusa.HEIGTH_NORMAL / 2f;

				if (ponyY < pisY)
					contact.setEnabled(false);

				if (obj.tipo == Medusa.TIPO_NORMAL && oPer.state == Dory.STATE_DEAD) {
					contact.setEnabled(false);
				}

			}

		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {

		}

	}

}
