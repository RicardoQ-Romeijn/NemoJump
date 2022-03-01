package com.juego.nemojump.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
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

	// Dory
	public static AtlasRegion dory;
	public static final String doryJumpSound = "data/boing.mp3";
	public static final String bgmusic = "data/bgmusic.mp3";

	// Camarones
	public static AtlasRegion camarones;

	// Jellyfish
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

	// Boton de Pausa
	public static TextureRegionDrawable btPause;

	// Botones de Musica y Audio
	public static  TextureRegionDrawable btMusicOn;
	public static  TextureRegionDrawable btMusicOff;
	public static  TextureRegionDrawable btSoundOn;
	public static  TextureRegionDrawable btSoundOff;

	// Fonts
	public static LabelStyle labelStyle;
	public static TextButtonStyle textButtonStyle;

	// Asset Manager
	public static AssetManager assetManager;

	// Cargar todos los assets necesarios para el juego
	public static void load() {
		// Cargar el Texture Atlas del Juego
		TextureAtlas nemoAtlas = new TextureAtlas(Gdx.files.internal("data/Jellyfish.atlas"));

		// Cargar cada asset a partir del Texture Atlas
		font = new BitmapFont(Gdx.files.internal("data/font.fnt"), nemoAtlas.findRegion("font"));

		btPause = new TextureRegionDrawable(nemoAtlas.findRegion("btPause"));
		fondo = nemoAtlas.findRegion("background");
		titulo = new TextureRegionDrawable(nemoAtlas.findRegion("title"));

		// Botones de Musica y Audio
		btMusicOn = new TextureRegionDrawable(nemoAtlas.findRegion("btmusic_on"));
		btMusicOff = new TextureRegionDrawable(nemoAtlas.findRegion("btmusic_off"));
		btSoundOn = new TextureRegionDrawable(nemoAtlas.findRegion("btspeaker_on"));
		btSoundOff = new TextureRegionDrawable(nemoAtlas.findRegion("btspeaker_off"));

		// Personaje
		dory = nemoAtlas.findRegion("dory");
		camarones = nemoAtlas.findRegion("corepod");

		// Jellyfish
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
		assetManager.load(bgmusic, Music.class);
		assetManager.finishLoading();
	}

	// Cargar los fonts
	public static void loadStyles(TextureAtlas atlas) {
		labelStyle = new LabelStyle(font, Color.BLACK);
		TextureRegionDrawable button = new TextureRegionDrawable(atlas.findRegion("button"));
		textButtonStyle = new TextButtonStyle(button, button, null, font);
	}

	// Devolver el Sonido de Saltar
	public static Sound getJumpSound(){
		return assetManager.get(doryJumpSound);
	}

	// Devolver la musica de fondo
	public static Music getBgMusic(){
		return assetManager.get(bgmusic);
	}
}
