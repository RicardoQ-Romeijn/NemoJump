package com.juego.superjumper.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Assets {

	public static BitmapFont fontChico;
	public static BitmapFont fontGrande;

	public static AtlasRegion fondo;
	public static TextureRegionDrawable titulo;

	/**
	 * Personaje
	 */
	public static AtlasRegion personaje;
	public static final String personajeJumpSound = "data/boing.mp3";
	public static AtlasRegion coin;

	/**
	* Jellyfish
	*/

	public static AtlasRegion jellyfishMain;
	public static AtlasRegion jellyfishMainSquished;
	public static AtlasRegion jellyfishMainExpanded;

	public static AtlasRegion jellyfishYellow;
	public static AtlasRegion jellyfishYellowSquished;
	public static AtlasRegion jellyfishYellowExpanded;

	public static AtlasRegion jellyfishPink;
	public static AtlasRegion jellyfishPinkSquished;

	public static AtlasRegion jellyfishBlue;
	public static AtlasRegion jellyfishBlueSquished;

	public static TextureRegionDrawable btPause;

	public static LabelStyle labelStyleChico;
	public static LabelStyle labelStyleGrande;
	public static TextButtonStyle textButtonStyleGrande;


	public static AssetManager assetManager;

	public static void loadStyles(TextureAtlas atlas) {
		// Label Style
		labelStyleChico = new LabelStyle(fontChico, Color.WHITE);
		labelStyleGrande = new LabelStyle(fontGrande, Color.WHITE);

		TextureRegionDrawable button = new TextureRegionDrawable(atlas.findRegion("button"));
		textButtonStyleGrande = new TextButtonStyle(button, button, null, fontGrande);
	}

	public static void load() {
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));
		TextureAtlas nemoAtlas = new TextureAtlas(Gdx.files.internal("data/Jellyfish.atlas"));

		// fontChico = new BitmapFont(Gdx.files.internal("data/fontChico.fnt"), atlas.findRegion("fontChico"));
		fontChico = new BitmapFont(Gdx.files.internal("data/font.fnt"), nemoAtlas.findRegion("font"));
		fontGrande = new BitmapFont(Gdx.files.internal("data/font.fnt"), nemoAtlas.findRegion("font"));

		loadStyles(atlas);

		btPause = new TextureRegionDrawable(nemoAtlas.findRegion("btPause"));

		fondo = nemoAtlas.findRegion("background");
		titulo = new TextureRegionDrawable(nemoAtlas.findRegion("title"));

		/**
		 * Personaje
		 */

		personaje = nemoAtlas.findRegion("dory");

		coin = nemoAtlas.findRegion("corepod");

		/**
		 * Jellyfish
		 */

		jellyfishMain = nemoAtlas.findRegion("jelly-2");
		jellyfishMainExpanded = nemoAtlas.findRegion("jelly-1");
		jellyfishMainSquished = nemoAtlas.findRegion("jelly-3");

		jellyfishYellow = nemoAtlas.findRegion("jellyfish-large2");
		jellyfishYellowExpanded = nemoAtlas.findRegion("jellyfish-large3");
		jellyfishYellowSquished = nemoAtlas.findRegion("jellyfish-large1");

		jellyfishBlue = nemoAtlas.findRegion("jellyfish-tiny2");
		jellyfishBlueSquished = nemoAtlas.findRegion("jellyfish-tiny1");

		jellyfishPink = nemoAtlas.findRegion("jellyfish-medium2");
		jellyfishPinkSquished = nemoAtlas.findRegion("jellyfish-medium1");

		assetManager = new AssetManager();

		assetManager.load(personajeJumpSound, Sound.class);

		assetManager.finishLoading();
	}

	public static Sound getJumpSound(){
		return assetManager.get(personajeJumpSound);
	}
}
