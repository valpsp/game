package jeu;

import org.newdawn.slick.Graphics;

/**
 * Classe abstraite de laquelle vont heriter les oeufs et les poules
 * @author Valentin
 *
 */
public abstract class Volaille implements Unite
{
	public abstract void render(Graphics g);

	public abstract void init();

	public abstract void update(int delta);

	public abstract float getX();

	public abstract float getY();
	
	public abstract int getPV();


	public boolean getOeuf()
	{
		return false;
	}

	public boolean getEclot()
	{
		return false;
	}

	public void keyReleased(int key)
	{		
	}

	public void keyPressed(int key)
	{		
	}

	public void setVision(boolean vision)
	{		
	}


}
