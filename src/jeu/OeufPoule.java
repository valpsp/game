package jeu;

import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * Classe oeuf, un oeuf est instancie lorsqu'une poule pond, il est retire lorsqu'il eclot apres un temps raisonnable
 * tous les oeufs sont geres dans factionPoule
 * herite de volaille
 * @author Valentin
 *
 */
public class OeufPoule extends Volaille
{
	private float x, y;
	private Animation[] animations = new Animation[1];
	
	//Points de vie
	private int pv = 3000;
	
	//L'oeuf est-il éclot ? (si oui la faction le remplace par une poule)
	private boolean eclot;
	private int vaEclore;
	
	private Map map;
	
	/**
	 * Constructeur par défaut
	 * @param map
	 */
	public OeufPoule(Map map)
	{
		this.x = 200;
		this.y = 200;
		
		this.eclot = false;
		this.vaEclore = 0;

		this.map = map;
	}
	
	/**
	 * Constructeur avec coordonnées
	 * @param map
	 */
	public OeufPoule(Map map, float x, float y)
	{
		this.x = x;
		this.y = y;
		
		this.eclot = false;
		this.vaEclore = 0;

		this.map = map;
	}
	
	/**
	 * Initialisation de l'Oeuf
	 */
	public void init()
	{
		SpriteSheet spriteSheet;
		try
		{
			spriteSheet = new SpriteSheet("src/main/ressources/sprites/monsters/egg.png", 18, 18);
			animations[0] = createAnimation(spriteSheet, 0, 0, 0);
			
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
		g.drawAnimation(animations[0], x-9, y-18);
	}
	
	/**
	 * Update, on appelle éclore et on prie pour qu'il éclose (ou pas)
	 */
	public void update(int delta)
	{
		this.eclore(delta);
	}

	@Override
	public void subirAttaque(int force)
	{
		if(pv > 0)
			this.pv -= force;
			System.out.println("Poule "+pv+ "pv");
	}
	
	/**
	 * Gestion de l'éclosion de l'oeuf, se fait au bout d'un intervalle de temps
	 */
	private void eclore(int delta)
	{
		
		if(vaEclore > 300)
		{
			eclot = true;
		}
		vaEclore++;
	}

	/**
	 * L'oeuf est-il éclot ?
	 */
	public boolean getEclot()
	{
		return eclot;
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

	/**
	 * Renvoie les pv
	 */
	@Override
	public int getPV()
	{
		return this.pv;
	}

	@Override
	public void setAttaqueSubie()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Unite getUniteCible()
	{
		return null;
	}
}
