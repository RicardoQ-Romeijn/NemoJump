package com.juego.nemojump.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

	public static boolean isMusicOn;
	public static boolean isSoundOn;

	public static int numeroVecesJugadas;

	public static int coinsTotal;

	public static int bestScore;

	// Variable de Preferencia de Gdx
	private final static Preferences pref = Gdx.app.getPreferences("com.juego.nemojump");

	/*
	 * Cargar en las variables estaticas el valor guardado, si hay, del fichero
	 */
	public static void load() {
		isMusicOn = pref.getBoolean("isMusicOn", true);
		isSoundOn = pref.getBoolean("isSoundOn", true);

		numeroVecesJugadas = pref.getInteger("numeroVecesJugadas", 0);

		coinsTotal = pref.getInteger("coinsTotal", 0);
		bestScore = pref.getInteger("bestScore", 0);
	}

	/*
	* Guardar la Distancia Máxima
	*/
	public static void setBestScore(int distance) {
		if (bestScore < distance) {
			bestScore = distance;
			save();
		}
	}

	/*
	 * Guardar el total de monedas cogidas
	 */
	public static void addCoinsTotal(int coins) {
		coinsTotal =+ coins;
		save();
	}

	/*
	 * Guardar el numero de veces jugado
	 */
	public static void addGame() {
		numeroVecesJugadas++;
		save();
	}

	/*
	 * Guardar todos
	 */
	public static void save() {
		pref.putBoolean("isMusicOn", isMusicOn);
		pref.putBoolean("isSoundOn", isSoundOn);

		pref.putInteger("numeroVecesJugadas", numeroVecesJugadas);
		pref.putInteger("coinsTotal", coinsTotal);
		pref.putInteger("bestScore", bestScore);

		pref.flush();
	}

}
