package com.juego.nemojump.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.juego.nemojump.screens.Screens;

public class Settings {

	public static boolean isMusicOn;
	public static boolean isSoundOn;

	public static int numeroVecesJugadas;

	public static int camaronesTotal;

	public static int bestScore;

	public static Music music;

	// Variable de Preferencia de Gdx
	private final static Preferences pref = Gdx.app.getPreferences("com.juego.nemojump");

	// Cargar en las variables estaticas el valor guardado, si hay, del fichero
	public static void load() {
		isMusicOn = pref.getBoolean("isMusicOn", true);
		isSoundOn = pref.getBoolean("isSoundOn", true);

		numeroVecesJugadas = pref.getInteger("numeroVecesJugadas", 0);

		camaronesTotal = pref.getInteger("camaronesTotal", 0);
		bestScore = pref.getInteger("bestScore", 0);
	}

	// Guardar la Distancia Máxima
	public static void setBestScore(int distance) {
		if (bestScore < distance) {
			bestScore = distance;
			save();
		}
	}

	// Guardar el total de monedas cogidas
	public static void addCamaronesTotal(int coins) {
		camaronesTotal = camaronesTotal + coins;
		save();
	}

	// Guardar el numero de veces jugado
	public static void addGame() {
		numeroVecesJugadas++;
		save();
	}

	// Devuelve el Sprite Correcto de Musica
	public static TextureRegionDrawable getMusicIcon(){
		if (isMusicOn)
			return Assets.btMusicOn;
		else
			return Assets.btMusicOff;
	}

	// Cambia el valor de Musica
	public static void changeMusic(){
		isMusicOn = !isMusicOn;
	}

	public static void playMusic() {
		if (isMusicOn)
			if (music != null) {
				music.play();
				music.setLooping(true);
			} else {
				music = Assets.getBgMusic();
				music.play();
				music.setLooping(true);
			}
		else if (music != null)
			music.stop();
	}

	// Devuelve el Sprite Correcto de Sonidos
	public static TextureRegionDrawable getSoundIcon(){
		if (isSoundOn)
			return Assets.btSoundOn;
		else
			return Assets.btSoundOff;
	}

	// Cambia el valor de Sonidos
	public static void changeSound(){
		isSoundOn = !isSoundOn;
	}

	// Guardar todos
	public static void save() {
		pref.putBoolean("isMusicOn", isMusicOn);
		pref.putBoolean("isSoundOn", isSoundOn);

		pref.putInteger("numeroVecesJugadas", numeroVecesJugadas);
		pref.putInteger("camaronesTotal", camaronesTotal);
		pref.putInteger("bestScore", bestScore);

		pref.flush();
	}

}
