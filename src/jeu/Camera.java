package jeu;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 * Classe Camera, pour zoomer/dezoomer et se balader sur la map
 * @author Valentin
 *
 */
public class Camera
{
	//Le zoom et la position de la cam
	private float scale;
	private float x;
	private float y;
	
	/**
	 * Constructeur par défaut
	 */
	public Camera()
	{
		this.scale = 1;
		this.x = 0;
		this.y = 0;
	}
	
	/**
	 * On translate la map de x et y et on zoom de scale
	 * (en fait c'est la map qui se déplace et se redimensionne, pas la caméra qui bouge)
	 * @param g
	 */
	public void render(Graphics g)
	{
		g.translate(x, y);
		g.scale(scale, scale);
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public float getScale()
	{
		return scale;
	}
	
	/**
	 * Gestion de la cam au clavier
	 * @param key
	 */
	public void keyReleased(int key)
	{
		if(Input.KEY_X == key)
			this.scale -= 0.3;
		
		if(Input.KEY_W == key)
			this.scale += 0.3;
		
		if(Input.KEY_Z == key)
			this.y += 50;
		
		if(Input.KEY_Q == key)
			this.x += 50;
		
		if(Input.KEY_S == key)
			this.y -= 50;
		
		if(Input.KEY_D == key)
			this.x -= 50;
	}

	public void mouseDragged(int oldx, int oldy, int newx, int newy)
	{
			this.x += newx-oldx;
			this.y += newy-oldy;
	}

	public void mouseWheelMoved(int change)
	{
		if(change > 0 && scale < 1.5)
		{
			this.scale += 0.1;
		}
		if(change < 0 && scale > 0.5)
		{
			this.scale -= 0.1;
		}
	}
}
