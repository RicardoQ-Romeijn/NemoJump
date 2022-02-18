package com.juego.nemojump.utils;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

// Una clase base que me ayuda a crear y mantener animaciones
public class AnimatedSpriteActor extends Actor {

	// Objeto de Animacion
	Animation<TextureRegion> animation;

	// El tiempo que está en ejecución (a partir de delta)
	float stateTime;

	// Constructor para la animacion
	public AnimatedSpriteActor(Animation<TextureRegion> animation) {
		this.animation = animation;
		stateTime = 0;
	}

	// Actuar la animación
	@Override
	public void act(float delta) {
		stateTime += delta;
		super.act(delta);
	}

	// Dibujar la animación
	@Override
	public void draw(Batch batch, float parentAlpha) {
		// sacamos el sprite de la animación a partir de delta
		TextureRegion spriteframe = animation.getKeyFrame(stateTime, true);
		// dibujar el sprite en la pantalla
		batch.draw(spriteframe, this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
}
