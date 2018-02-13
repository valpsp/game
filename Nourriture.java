package jeu;

import java.awt.Font;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;

/**
 * Instanciation des ressources que les unites peuvent manger, avec une reserve finie de nourriture
 * (actuellement sous forme de ble)
 * @author Valentin
 *
 */
public class Nourriture implements Entity
{
	private float x;
	private float y;
	
	private int reserve = 500;
	private Animation[] animations = new Animation[4];
	
	/**
	 * Constructeur par defaut
	 */
	public Nourriture()
	{
		x = 200;
		y = 200;
	}
	
	/**
	 * Constructeur avec coordonnees
	 */
	public Nourriture(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Initialisation de la nourriture
	 * principalement les sprites
	 */
	public void init()
	{
		SpriteSheet spriteSheet;
		try
		{
			spriteSheet = new SpriteSheet("src/main/ressources/sprites/monsters/food2.png", 40, 40);
			animations[0] = createAnimation(spriteSheet, 0, 0, 0);
			animations[1] = createAnimation(spriteSheet, 1, 1, 0);
			animations[2] = createAnimation(spriteSheet, 2, 2, 0);
			animations[3] = createAnimation(spriteSheet, 3, 3, 0);


		} catch (SlickException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * On crée l'animation grâce à un sprite
	 * @param colStart   précise la colonne de départ sur le sprite
	 * @param colEnd	// 				// 		fin		//
	 * @param ligne		précise la ligne du sprite à utiliser
	 * @return
	 */
	private Animation createAnimation(SpriteSheet spriteSheet, int colStart, int colEnd, int ligne)
	{
		Animation animation = new Animation();
		for(int i = colStart; i <= colEnd; i++)
		{
			animation.addFrame(spriteSheet.getSprite(i, ligne), 200);
		}
		return animation;
	}
	
	/**
	 * Affichage
	 */
	public void render(Graphics g)
	{	
		g.setColor(Color.black);
		//g.drawString(reserve + "/500",x-30, y-50);
		
		if(this.reserve >= 400)
			g.drawAnimation(animations[0], x-20, y-30);
		else if( this.reserve >=300 && this.reserve < 400)
			g.drawAnimation(animations[1], x-20, y-30);
		else if( this.reserve >=150 && this.reserve < 300)
			g.drawAnimation(animations[2], x-20, y-30);
		else if( this.reserve < 200)
			g.drawAnimation(animations[3], x-20, y-30);
	}
	
	/**
	 * Update
	 */
	public void update(int delta)
	{
	}
	
	/**
	 * Appelee par les unites en boucle, elle leur renvoit 1 de nourriture et decremente
	 * la reserve de la nourriture
	 * @return
	 */
	public int miner()
	{
			reserve = reserve - 1;
			return 1;
	}
	
	/**
	 * return reserve
	 */
	public float getReserve()
	{
		return this.reserve;
	}
	
	/**
	 * return x
	 */
	public float getX()
	{
		return this.x;
	}

	/**
	 * return y
	 */
	public float getY()
	{
		return this.y;
	}
}
