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

public class WorldGameRender {
	final float WIDTH = Screens.WORLD_WIDTH;
	final float HEIGHT = Screens.WORLD_HEIGHT;

	WorldGame oWorld;
	SpriteBatch batcher;
	OrthographicCamera oCam;
	Box2DDebugRenderer boxRender;

	public WorldGameRender(SpriteBatch batcher, WorldGame oWorld) {
		this.oWorld = oWorld;
		this.batcher = batcher;

		oCam = new OrthographicCamera(WIDTH, HEIGHT);
		oCam.position.set(WIDTH / 2f, HEIGHT / 2f, 0);

		boxRender = new Box2DDebugRenderer();
	}

	public void unprojectToWorldCoords(Vector3 touchPoint) {
		oCam.unproject(touchPoint);
	}

	public void render(float delta) {
		if (oWorld.state == WorldGame.STATE_RUNNING)
			oCam.position.y = oWorld.oPer.position.y;

		if (oCam.position.y < Screens.WORLD_HEIGHT / 2f) {
			oCam.position.y = Screens.WORLD_HEIGHT / 2f;
		}

		oCam.update();
		// Se le pasa el mundo físico y las matrices de la camara (combined)
		//boxRender.render(oWorld.oWorldBox, oCam.combined);
		batcher.setProjectionMatrix(oCam.combined);

		batcher.begin();

		renderPersonaje();
		renderPlataformas();
		renderPiezasPlataformas();
		renderCoins();

		batcher.end();

		// boxRender.render(oWorld.oWorldBox, oCam.combined);

	}

	private void renderPersonaje() {
		AtlasRegion keyframe = null;

		Dory obj = oWorld.oPer;
		keyframe = Assets.dory;

		if (obj.velocidad.x < 0)
			batcher.draw(keyframe, obj.position.x + Dory.DRAW_WIDTH / 2f, obj.position.y - Dory.DRAW_HEIGTH / 2f,
					-Dory.DRAW_WIDTH / 2f, Dory.DRAW_HEIGTH / 2f, -Dory.DRAW_WIDTH, Dory.DRAW_HEIGTH, 1, 1, obj.angleDeg);

		else
			batcher.draw(keyframe, obj.position.x - Dory.DRAW_WIDTH / 2f, obj.position.y - Dory.DRAW_HEIGTH / 2f,
					Dory.DRAW_WIDTH / 2f, Dory.DRAW_HEIGTH / 2f, Dory.DRAW_WIDTH, Dory.DRAW_HEIGTH, 1, 1, obj.angleDeg);

	}

	private void renderPlataformas() {
		Iterator<Medusa> i = oWorld.arrMedusas.iterator();
		while (i.hasNext()) {
			Medusa obj = i.next();

			AtlasRegion keyframe = null;

			if (obj.tipo == Medusa.TIPO_ROMPIBLE) {
				keyframe = Assets.jellyfishYellow;
				/*switch (obj.color) {
					case Plataformas.JELLY:
						keyframe = Assets.jellyfishMain;
						break;
					case Plataformas.JELLY_YELLOW:
						keyframe = Assets.jellyfishYellow;
						break;
					case Plataformas.JELLY_BLUE:
						keyframe = Assets.jellyfishBlue;
						break;
					case Plataformas.JELLY_PINK:
						keyframe = Assets.jellyfishPink;
						break;
					default:
						keyframe = Assets.jellyfishBlue;
						break;
				}*/
			} else {
				keyframe = Assets.jellyfishPink;
				/*switch (obj.color) {
					case Plataformas.JELLY:
						keyframe = Assets.jellyfishMain;
						break;
					case Plataformas.JELLY_YELLOW:
						keyframe = Assets.jellyfishYellow;
						break;
					case Plataformas.JELLY_BLUE:
						keyframe = Assets.jellyfishBlue;
						break;
					case Plataformas.JELLY_PINK:
						keyframe = Assets.jellyfishPink;
						break;
					default:
						keyframe = Assets.jellyfishMain;
						break;

				}*/
			}
			batcher.draw(keyframe, obj.position.x - Medusa.DRAW_WIDTH_NORMAL / 2f, obj.position.y - Medusa.DRAW_HEIGTH_NORMAL / 2f,
					Medusa.DRAW_WIDTH_NORMAL, Medusa.DRAW_HEIGTH_NORMAL);

		}
	}

	private void renderPiezasPlataformas() {
		Iterator<PiezaMedusa> i = oWorld.arrPiezasMedusas.iterator();
		while (i.hasNext()) {
			PiezaMedusa obj = i.next();

			AtlasRegion keyframe = null;

			/*if (obj.tipo == PiezaPlataformas.TIPO_LEFT) {
				switch (obj.color) {
				case Plataformas.COLOR_BEIGE:
					keyframe = Assets.plataformaBeigeLeft;
					break;
				case Plataformas.COLOR_BLUE:
					keyframe = Assets.plataformaBlueLeft;
					break;
				case Plataformas.COLOR_GRAY:
					keyframe = Assets.plataformaGrayLeft;
					break;
				case Plataformas.COLOR_GREEN:
					keyframe = Assets.plataformaGreenLeft;
					break;
				case Plataformas.COLOR_MULTICOLOR:
					keyframe = Assets.plataformaMulticolorLeft;
					break;
				case Plataformas.COLOR_PINK:
					keyframe = Assets.plataformaPinkLeft;
					break;

				}
			}
			else {
				switch (obj.color) {
				case Plataformas.COLOR_BEIGE:
					keyframe = Assets.plataformaBeigeRight;
					break;
				case Plataformas.COLOR_BLUE:
					keyframe = Assets.plataformaBlueRight;
					break;
				case Plataformas.COLOR_GRAY:
					keyframe = Assets.plataformaGrayRight;
					break;
				case Plataformas.COLOR_GREEN:
					keyframe = Assets.plataformaGreenRight;
					break;
				case Plataformas.COLOR_MULTICOLOR:
					keyframe = Assets.plataformaMulticolorRight;
					break;
				case Plataformas.COLOR_PINK:
					keyframe = Assets.plataformaPinkRight;
					break;

				}
			}*/

			keyframe = Assets.jellyfishBlue;

			batcher.draw(keyframe, obj.position.x - PiezaMedusa.DRAW_WIDTH_NORMAL / 2f, obj.position.y - PiezaMedusa.DRAW_HEIGTH_NORMAL
					/ 2f, PiezaMedusa.DRAW_WIDTH_NORMAL / 2f, PiezaMedusa.DRAW_HEIGTH_NORMAL / 2f, PiezaMedusa.DRAW_WIDTH_NORMAL,
					PiezaMedusa.DRAW_HEIGTH_NORMAL, 1, 1, obj.angleDeg);

		}
	}

	private void renderCoins() {
		Iterator<Camarones> i = oWorld.arrCamarones.iterator();
		while (i.hasNext()) {
			Camarones obj = i.next();

			batcher.draw(Assets.camarones, obj.position.x - Camarones.DRAW_WIDTH / 2f, obj.position.y - Camarones.DRAW_HEIGHT / 2f, Camarones.DRAW_WIDTH,
					Camarones.DRAW_HEIGHT);
		}

	}

}
