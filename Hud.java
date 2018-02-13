package jeu;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;


/**
 * Le hud qui s'affiche sur la droite de l'écran et présente des informations utile au spectateur
 * @author Valentin
 *
 */
public class Hud
{
	private static final int X = 10;
	private static final int Y = 30;
	
	private Faction faction1;
	private Faction faction2;
	private Faction faction3;
	
	private Animation[] animations = new Animation[12];
	
	//Pour les poules
	private int pouleChangerAnimation = 0;
	private int pouleRandomAnimation = 2;
	//Pour les viperes
	private int vipereChangerAnimation = 0;
	private int vipereRandomAnimation = 2;
	//Pour le renard
	private int renardChangerAnimation = 0;
	private int renardRandomAnimation = 2;
	
	Image fond;
	
	public Hud(Faction faction1, Faction faction2, Faction faction3)
	{
		this.faction1 = faction1;
		this.faction2 = faction2;
		this.faction3 = faction3;
	}
	
	public void init() throws SlickException
	{
		  this.fond = new Image("src/main/ressources/fonds/fond3.png");
		  
		  SpriteSheet spriteSheetPoule;
		  SpriteSheet spriteSheetVipere;
		  SpriteSheet spriteSheetRenard;

			try
			{
				//Poule
				spriteSheetPoule = new SpriteSheet("src/main/ressources/sprites/monsters/chickenHud.png", 60, 60);
				animations[0] = createAnimation(spriteSheetPoule, 0, 2, 3);
				animations[1] = createAnimation(spriteSheetPoule, 0, 2, 1);
				animations[2] = createAnimation(spriteSheetPoule, 0, 2, 0);
				animations[3] = createAnimation(spriteSheetPoule, 0, 2, 2);
				//Vipere
				spriteSheetVipere = new SpriteSheet("src/main/ressources/sprites/monsters/snakeHud.png", 60, 60);
				animations[4] = createAnimation(spriteSheetVipere, 0, 2, 3);
				animations[5] = createAnimation(spriteSheetVipere, 0, 2, 1);
				animations[6] = createAnimation(spriteSheetVipere, 0, 2, 0);
				animations[7] = createAnimation(spriteSheetVipere, 0, 2, 2);
				//renard
				spriteSheetRenard = new SpriteSheet("src/main/ressources/sprites/monsters/foxHud.png", 60, 60);
				animations[8] = createAnimation(spriteSheetRenard, 6, 8, 3);
				animations[9] = createAnimation(spriteSheetRenard, 6, 8, 1);
				animations[10] = createAnimation(spriteSheetRenard, 6, 8, 0);
				animations[11] = createAnimation(spriteSheetRenard, 6, 8, 2);
			} catch (SlickException e)
			{
				e.printStackTrace();
			}
	}
	
	/**
	 * Tout le hud se fait ici, peu de choses à afficher donc pas nécessaire de créer d'autres méthodes
	 * 
	 */
	public void render(Graphics g)
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		 
		g.resetTransform();
		g.setColor(Color.white);
		  
		Rectangle s = new Rectangle((int)screenSize.getWidth()-300, 0, 300, (int)screenSize.getHeight());
		g.texture(s, fond, true);
		  
		Random rand = new Random();
		String nombrePoule = "NOMBRE "+ faction1.getSize();
		String nombreVipere = "NOMBRE "+ faction2.getSize();
		String pvRenard = "NOMBRE "+ faction3.getSize();

		
		//Affichage de la poule dans le Hud
		if(pouleChangerAnimation > 100)
		{
		 pouleRandomAnimation = rand.nextInt(4);
		 pouleChangerAnimation = 0;
		}
		g.drawAnimation(animations[pouleRandomAnimation], (int)screenSize.getWidth()-190, 60);
		pouleChangerAnimation++;
		g.setColor(Color.black);
		g.drawString(nombrePoule, (int)screenSize.getWidth()-190, 132);
		
		//Affichage de la vipere dans le Hud
		if(vipereChangerAnimation > 80)
		{
		 vipereRandomAnimation = rand.nextInt(4);
		 vipereChangerAnimation = 0;
		}
		g.drawAnimation(animations[vipereRandomAnimation+4], (int)screenSize.getWidth()-190, 290);
		vipereChangerAnimation++;
		g.setColor(Color.black);
		g.drawString(nombreVipere, (int)screenSize.getWidth()-190, 362);
		
		//Affichage de la vipere dans le Hud
		if(renardChangerAnimation > 160)
		{
			renardRandomAnimation = rand.nextInt(4);
		 renardChangerAnimation = 0;
		}
		g.drawAnimation(animations[renardRandomAnimation+8], (int)screenSize.getWidth()-190, 520);
		renardChangerAnimation++;
		g.setColor(Color.black);
		g.drawString(pvRenard, (int)screenSize.getWidth()-190, 592);
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
}
