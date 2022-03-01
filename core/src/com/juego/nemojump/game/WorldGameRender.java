package com.juego.nemojump.game;

import java.util.Iterator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.juego.nemojump.utils.Assets;
import com.juego.nemojump.objetos.Camarones;
import com.juego.nemojump.objetos.Dory;
import com.juego.nemojump.objetos.PiezaMedusa;
import com.juego.nemojump.objetos.Medusa;
import com.juego.nemojump.screens.Screens;

/**
 * Clase que se encarga de cargar los sprites e imagenes de los objetos
 */
public class WorldGameRender {
	// Objetos que utilizaremos para dibujar las imagenes / sprites
	WorldGame oWorld; // Mundo Físico
	SpriteBatch batcher; // Dibuja en modo de indices
	OrthographicCamera oCam; // Camara tipo ortográfica
	Box2DDebugRenderer boxRender; // Renderer de Box2D

	// Constructor por defecto
	public WorldGameRender(SpriteBatch batcher, WorldGame oWorld) {
		// Asignamos / Sobreescribimos el mundo físico y el Sprite Batch
		this.oWorld = oWorld;
		this.batcher = batcher;

		// Instanciamos y posicionamos la camara
		oCam = new OrthographicCamera(Screens.WORLD_WIDTH, Screens.WORLD_HEIGHT);
		oCam.position.set(Screens.WORLD_WIDTH / 2f, Screens.WORLD_HEIGHT / 2f, 0);

		// Instanciamos el Box2D Renderer
		boxRender = new Box2DDebugRenderer();
	}

	public void unprojectToWorldCoords(Vector3 touchPoint) {
		oCam.unproject(touchPoint);
	}

	// Metodo que va a dibujar
	public void render(float delta) {
		// Si el mundo está activo, ponemos la posición de la camara la misma que a Dory
		if (oWorld.state == WorldGame.STATE_RUNNING)
			oCam.position.y = oWorld.oDory.position.y;

		// Si el alto de la camara es menor que el alto del mundo
		if (oCam.position.y < Screens.WORLD_HEIGHT / 2f) {
			oCam.position.y = Screens.WORLD_HEIGHT / 2f; // Ponemos la altura correcta
		}

		// Llamamos al metodo update que recalcula la proyección de la camara
		oCam.update();

		// Se le pasa el mundo físico y las matrices de la camara (combined) para ver el mundo físico
		//boxRender.render(oWorld.oWorldBox, oCam.combined);

		// Ponemos el modo de proyectión del batcher, en este caso, lo sacamos de la camara
		batcher.setProjectionMatrix(oCam.combined);

		// Para dibujar las imagenes, llamamos al metodo begin, y termina en el metodo end
		batcher.begin();

		renderDory(); // Metodo que dibuja a Dory
		renderMedusas(); // Metodo que dibuja a Medusa
		renderPiezasMedusas(); // Metodo que dibuja las piezas de la Medusa
		renderCamarones(); // Metodo que dibuja los camarones

		batcher.end();
	}

	// Metodo que renderiza / dibuja a Dory
	private void renderDory() {
		AtlasRegion keyframe; // Creamos un keyframe de tipo AtlasRegion

		Dory obj = oWorld.oDory; // Cogemos el objeto creado en WorldGame
		keyframe = Assets.dory; // Le ponemos la imagen de Dory desde Assets

		if (obj.velocidad.x < 0) // Está viajando a la Izquierda
			// Dibuja Dory el Sprite mirando al lado Izquiero
			batcher.draw(keyframe, obj.position.x + Dory.DRAW_WIDTH / 2f, obj.position.y - Dory.DRAW_HEIGTH / 2f,
					-Dory.DRAW_WIDTH / 2f, Dory.DRAW_HEIGTH / 2f, -Dory.DRAW_WIDTH, Dory.DRAW_HEIGTH, 1, 1, obj.angleDeg);
		else // Si está viajando a la Derecha
			// Dibuja Dory, el Sprite, mirando al lado derecho
			batcher.draw(keyframe, obj.position.x - Dory.DRAW_WIDTH / 2f, obj.position.y - Dory.DRAW_HEIGTH / 2f,
					Dory.DRAW_WIDTH / 2f, Dory.DRAW_HEIGTH / 2f, Dory.DRAW_WIDTH, Dory.DRAW_HEIGTH, 1, 1, obj.angleDeg);
	}

	// Metodo que renderiza / dibuja a las Medusas
	private void renderMedusas() {
		Iterator<Medusa> i = oWorld.arrMedusas.iterator(); // Creamos un Iterator para recorrer el Array de Medusas
		while (i.hasNext()) {
			Medusa obj = i.next(); // Sacamos el Objeto Medusa

			AtlasRegion keyframe; // Creamos el keyframe que dibujaremos

			if (obj.tipo == Medusa.TIPO_ROMPIBLE) { // Si es tipo rompible
				keyframe = Assets.jellyfishYellow; // Dibujamos una medusa amarilla
			} else { // Si no es tipo rompible
				keyframe = Assets.jellyfishPink; // Dibujamos una medusa rosa
			}

			// Dibujamos el Sprite
			batcher.draw(keyframe, obj.position.x - Medusa.DRAW_WIDTH_NORMAL / 2f, obj.position.y - Medusa.DRAW_HEIGTH_NORMAL / 2f,
					Medusa.DRAW_WIDTH_NORMAL, Medusa.DRAW_HEIGTH_NORMAL);
		}
	}

	// Metodo que renderiza / dibuja a las piezas de las Medusas
	private void renderPiezasMedusas() {
		Iterator<PiezaMedusa> i = oWorld.arrPiezasMedusas.iterator(); // Creamos un Iterator para recorrer el Array de Medusas
		while (i.hasNext()) {
			PiezaMedusa obj = i.next(); // Sacamos el Objeto Pieza Medusa

			// Dibujamos el Sprite con el Asset Directo
			batcher.draw(Assets.jellyfishBlue, obj.position.x - PiezaMedusa.DRAW_WIDTH_NORMAL / 2f, obj.position.y - PiezaMedusa.DRAW_HEIGTH_NORMAL
					/ 2f, PiezaMedusa.DRAW_WIDTH_NORMAL / 2f, PiezaMedusa.DRAW_HEIGTH_NORMAL / 2f, PiezaMedusa.DRAW_WIDTH_NORMAL,
					PiezaMedusa.DRAW_HEIGTH_NORMAL, 1, 1, obj.angleDeg);

		}
	}

	// Metodo que renderiza / dibuja los camarones
	private void renderCamarones() {
		Iterator<Camarones> i = oWorld.arrCamarones.iterator(); // Creamos un Iterator para recorrer el Array de Camarones
		while (i.hasNext()) {
			Camarones obj = i.next(); // Sacamos el Objeto Camarones

			// Dibujamos el Sprite con el Asset Directo
			batcher.draw(Assets.camarones, obj.position.x - Camarones.DRAW_WIDTH / 2f, obj.position.y - Camarones.DRAW_HEIGHT / 2f, Camarones.DRAW_WIDTH,
					Camarones.DRAW_HEIGHT);
		}

	}

}
