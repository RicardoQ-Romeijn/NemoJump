package com.juego.nemojump.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Assets {

	public static BitmapFont font;

	public static AtlasRegion fondo;
	public static TextureRegionDrawable titulo;

	/*
	 * Dory
	 */
	public static AtlasRegion dory;
	public static final String doryJumpSound = "data/boing.mp3";

	/*
	* Camarones
	*/
	public static AtlasRegion camarones;

	/*
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

	/*
	* Boton de Pausa
	*/
	public static TextureRegionDrawable btPause;

	/*
	* Fonts
	*/
	public static LabelStyle labelStyle;
	public static TextButtonStyle textButtonStyle;

	/*
	* Asset Manager
	*/
	public static AssetManager assetManager;

	/*
	* Cargar todos los assets necesarios para el juego
	*/
	public static void load() {
		// Cargar el Texture Atlas del Juego
		TextureAtlas nemoAtlas = new TextureAtlas(Gdx.files.internal("data/Jellyfish.atlas"));

		// Cargar cada asset a partir del Texture Atlas
		font = new BitmapFont(Gdx.files.internal("data/font.fnt"), nemoAtlas.findRegion("font"));

		btPause = new TextureRegionDrawable(nemoAtlas.findRegion("btPause"));
		fondo = nemoAtlas.findRegion("background");
		titulo = new TextureRegionDrawable(nemoAtlas.findRegion("title"));

		/*
		 * Personaje
		 */
		dory = nemoAtlas.findRegion("dory");
		camarones = nemoAtlas.findRegion("corepod");

		/*
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

		// Cargar los Fonts para Botones e Etiquetas
		loadStyles(nemoAtlas);

		// Utilizamos el Asset Manager para cargar los sonidos
		assetManager = new AssetManager();
		assetManager.load(doryJumpSound, Sound.class);
		assetManager.finishLoading();
	}

	/*
	 * Cargar los fonts
	 */
	public static void loadStyles(TextureAtlas atlas) {
		labelStyle = new LabelStyle(font, Color.BLACK);
		TextureRegionDrawable button = new TextureRegionDrawable(atlas.findRegion("button"));
		textButtonStyle = new TextButtonStyle(button, button, null, font);
	}

	/*
	* Devolver el Sonido de Saltar
	*/
	public static Sound getJumpSound(){
		return assetManager.get(doryJumpSound);
	}
}
