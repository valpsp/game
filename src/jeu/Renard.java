package jeu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;

/**
 * Classe renard a partir de laquelle on va pouvoir instancier des unites renard
 * ATTENTION: LE RENARD EST UN SINGLETON DONC CONSTRUCTEUR PRIVATE ET INSTANCIATION VIA getInstance()
 * @author Valentin
 *
 */
public class Renard extends Faction implements Unite
{
	//Pour les tests, o� regarde l'unit�
	private float regardX;
	private float regardY;
	private boolean vision; 
	
	//Singleton instance
	private static Renard instance = null;
	
	//Coordonn�es et mouvement
	private float x, y;
	private int direction;
	private boolean moving = false;
	private Animation[] animations = new Animation[17];
	private int changerDirection; 	//Symbolise "l'envie" de changer de direction, si > � un seuil on change
	
	//Points de vie
	private int pv = 4000;
	
	//Ressource
	private Nourriture nourritureCible;
	private int nourritureReserve;
	private int nourritureMax = 40;
	private int mangerDeNouveau;
	
	//Combat
	private Unite uniteCible;
	private int forceAttaque = 350;
	private int attaquerDeNouveau;
	private int frequenceAttaque = 30; //au plus il est petit au plus la frequence est haute
	private float range = 35;
	private int attaqueSubie = 0;
	
	private Map map;
	private Ressources ressources;
	private Faction factionEnnemie1;
	private Faction factionEnnemie2;

	
	
	/**
	 * IMPORTANT
	 * Methode qui permet d'obtenir une instanciation de renard si il n'est pas d�j� instanci�
	 */
	public static Renard getInstance(Map map, Ressources ressources, Faction factionEnnemie1, Faction factionEnnemie2, float x, float y)
	{
		if(instance == null)
			instance = new Renard(map, ressources, factionEnnemie1, factionEnnemie2, x, y);
		return instance;
	}
	
	
	/**
	 * Constructeur par d�faut
	 * @param map
	 * On passe la map en parametre pour pouvoir appeler sa methode "collision"
	 */
	private Renard(Map map, Ressources ressources, Faction factionEnnemie1, Faction factionEnnemie2)
	{
		this.x = 200;
		this.y = 200;
		this.direction = 2;
		this.changerDirection = 0;
		
		this.nourritureReserve = 0;
		this.mangerDeNouveau = 0;
		
		this.attaquerDeNouveau = 0;
		
		this.map = map;
		this.ressources = ressources;
		this.factionEnnemie1 = factionEnnemie1;
		this.factionEnnemie2 = factionEnnemie2;
		
		//Pour les tests, o� regarde l'unit�
		this.regardX = this.x;
		this.regardY = this.y;
		this.vision = false;

	}
	
	/**
	 * Constructeur avec coordonn�es
	 * @param map
	 */
	private Renard(Map map, Ressources ressources, Faction factionEnnemie1, Faction factionEnnemie2, float x, float y)
	{
		this.x = x;
		this.y = y;
		this.direction = 2;
		this.changerDirection = 0;
		
		this.nourritureReserve = 0;
		this.mangerDeNouveau = 0;
		
		this.attaquerDeNouveau = 0;

		this.map = map;
		this.ressources = ressources;
		this.factionEnnemie1 = factionEnnemie1;
		this.factionEnnemie2 = factionEnnemie2;
		
		//Pour les tests, o� regarde l'unit�
		this.regardX = this.x;
		this.regardY = this.y;
		this.vision = false;
	}
	
	/**
	 * Initialisation du renard
	 * On initialise ses sprites essentiellement
	 */
	public void init()
	{
		SpriteSheet spriteSheet;
		try
		{
			spriteSheet = new SpriteSheet("src/main/ressources/sprites/monsters/fox.png", 48, 48);
			animations[0] = createAnimation(spriteSheet, 7, 7, 3);
			animations[1] = createAnimation(spriteSheet, 7, 7, 1);
			animations[2] = createAnimation(spriteSheet, 7, 7, 0);
			animations[3] = createAnimation(spriteSheet, 7, 7, 2);
			animations[4] = createAnimation(spriteSheet, 6, 8, 3);
			animations[5] = createAnimation(spriteSheet, 6, 8, 1);
			animations[6] = createAnimation(spriteSheet, 6, 8, 0);
			animations[7] = createAnimation(spriteSheet, 6, 8, 2);
			
			//Attaqu�e
			animations[8] = createAnimation(spriteSheet, 6, 8, 7);
			animations[9] = createAnimation(spriteSheet, 6, 8, 5);
			animations[10] = createAnimation(spriteSheet, 6, 8, 4);
			animations[11] = createAnimation(spriteSheet, 6, 8, 6);
			animations[12] = createAnimation(spriteSheet, 6, 8, 7);
			animations[13] = createAnimation(spriteSheet, 6, 8, 5);
			animations[14] = createAnimation(spriteSheet, 6, 8, 4);
			animations[15] = createAnimation(spriteSheet, 6, 8, 6);
			
			//Mort
			animations[16] = createAnimation(spriteSheet, 0, 0, 0);

			
		} catch (SlickException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * On cr�e l'animation gr�ce � un sprite
	 * @param colStart   pr�cise la colonne de d�part sur le sprite
	 * @param colEnd	// 				// 		fin		//
	 * @param ligne		pr�cise la ligne du sprite � utiliser
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
	 * Affiche d'abord un ombre pour le renard, puis le renard par dessus
	 */
	public void render(Graphics g)
	{
		g.setColor(new Color(0,0,0,.5f));
		
		//Pour les test, o� l'unit� regarde
		if(vision)
		{
			g.setColor(Color.blue);
			g.fillOval(regardX-5, regardY-5, 12, 7);
			g.drawLine(this.x, this.y, regardX, regardY);
		}
		
		if(pv > 0)
		{
			//Pas de mouvement ? on affiche les animations immobiles
			if(moving == false)
			{
				if(attaqueSubie > 10)
					g.drawAnimation(animations[direction+8], x-19, y-33);
				else if(nourritureReserve < nourritureMax)
					g.drawAnimation(animations[direction], x-19, y-33);
	
	
			}
			//Le renard se d�place ? On affiche les animations de mouvemnent
			else
			{
				if(attaqueSubie > 10)
					g.drawAnimation(animations[direction+8], x-19, y-33);
				else if(nourritureReserve < nourritureMax)
					g.drawAnimation(animations[direction + 4], x-19, y-33);
			}
		}
		else
			g.drawAnimation(animations[16], x-19, y-33);

	}
	
	/**
	 * Update l'�tat de le renard
	 * On appelle sa fonction d'IA qui g�re son comportement
	 */
	public void update(int delta)
	{
		//Pour les tests, o� regarde le renard
		this.regardX = this.x;
		this.regardY = this.y;
		
		attaqueSubie--;
		
		this.IA(delta);
	}
	
	
	/**
	 * La fonction d'intelligence, elle va scanner l'environnement de l'unit�
	 * puis appeler les fonctions qui g�rent son comportement specifique
	 * vis � vis des ressources ou autres unit�s
	 * @param delta
	 */
	private void IA(int delta)
	{	
		if(pv > 0)
		{
			//On scanne les ressources � port�e
			nourritureCible = this.scanRessources();
			
			this.uniteCible = this.scanEnnemi();		
			
			//Si unite � portee et pas d'obstacle
			if(uniteCible != null && !obstacle(uniteCible))
			{
					this.IACible(uniteCible);
			}
			//Si nourriture � port�e et si on a faim
			else if(nourritureCible != null && this.nourritureReserve < this.nourritureMax)
			{
				//Si pas d'obstacle, on va manger
				if(!obstacle(nourritureCible))
					this.IANourriture(nourritureCible);
				//Sinon on fait autre chose
				else
					this.randomMove(delta);
			}
			//Si rien, on fait autre chose
			else
			{
				this.randomMove(delta);
			}
			this.move(delta);
		}
	}
	
	/**
	 * Gere le comportement si nourriture detectee et pas d'obstacle sur le chemin
	 * @param indexNourriture
	 */
	private void IANourriture(Nourriture nourriture)
	{
		boolean mangeable = false;
		float xNourriture, yNourriture;
		
		xNourriture = nourriture.getX();
		yNourriture = nourriture.getY();
		
		//On regarde si la nourriture est assez proche pour �tre mang�e
		mangeable = (this.x - xNourriture > -25 && this.x - xNourriture < 25) &&
					(this.y - yNourriture > -25 && this.y - yNourriture < 25);
		
		//Si oui on la mange
		if(mangeable)
		{
			this.manger(nourritureCible);
		}
		//Si non on se dirige vers elle
		else
		{
			Random rand = new Random();
			int randomXouY = rand.nextInt(8);
			
			if(randomXouY < 1)
			{
				boolean xOuy;
				float ecartX = Math.abs(this.x - xNourriture);
				float ecartY = Math.abs(this.y - yNourriture);
	
				xOuy = (ecartX > ecartY);
						
				if(xOuy)
				{
					if(this.x < xNourriture)
						this.direction = 3;
					else if(this.x > xNourriture)
						this.direction = 1;
				}
				else
				{
					if(this.y < yNourriture)
						this.direction = 2;
					else if(this.y > yNourriture)
						this.direction = 0;
				}
				this.moving = true;
			}
		}
	}
	
	/**
	 * G�re le comportement si cible detect�e et pas d'obstacle
	 * @param uniteCible
	 */
	private void IACible(Unite uniteCible)
	{
		boolean attaquable = false;
		float xCible, yCible;
		
		xCible = uniteCible.getX();
		yCible = uniteCible.getY();
		
		//On regarde si l'unite est assez proche pour etre attaquee
		attaquable = (this.x - xCible > -(range) && this.x - xCible < range) &&
					(this.y - yCible > -(range) && this.y - yCible < range);
		
		//Si oui on l'attaque
		if(attaquable)
		{
			Random rand = new Random();
			int randomXouY = rand.nextInt(8);
			
			if(randomXouY < 1)
			{
				boolean xOuy;
				float ecartX = Math.abs(this.x - xCible);
				float ecartY = Math.abs(this.y - yCible);
	
				xOuy = (ecartX > ecartY);
						
				if(xOuy)
				{
					if(this.x < xCible)
						this.direction = 3;
					else if(this.x > xCible)
						this.direction = 1;
				}
				else
				{
					if(this.y < yCible)
						this.direction = 2;
					else if(this.y > yCible)
						this.direction = 0;
				}
			}
			this.attaquer(uniteCible);
		}
		//Si non on se dirige vers elle
		else
		{
			Random rand = new Random();
			int randomXouY = rand.nextInt(8);
			
			if(randomXouY < 1)
			{
				boolean xOuy;
				float ecartX = Math.abs(this.x - xCible);
				float ecartY = Math.abs(this.y - yCible);
	
				xOuy = (ecartX > ecartY);
						
				if(xOuy)
				{
					if(this.x < xCible)
						this.direction = 3;
					else if(this.x > xCible)
						this.direction = 1;
				}
				else
				{
					if(this.y < yCible)
						this.direction = 2;
					else if(this.y > yCible)
						this.direction = 0;
				}
			}
			this.moving = true;
		}
	}
	
	/**
	 * On enl�ve des pv � l'unite cible � une certaine frequence
	 */
	public void attaquer(Unite uniteCible)
	{
		this.moving = false;
		if(attaquerDeNouveau > frequenceAttaque)
		{
			uniteCible.subirAttaque(this.forceAttaque);
			uniteCible.setAttaqueSubie();
			attaquerDeNouveau = 0;
		}
		attaquerDeNouveau++;
	}
	
	/**
	 * est appel�e par l'unit� attaquante et retire des pv
	 */
	public void subirAttaque(int force)
	{
		if(pv > 0)
			this.pv -= force;
	}
	
	/**
	 * D�placement Al�atoire
	 * @param delta
	 */
	private void randomMove(int delta)
	{
		Random rand = new Random();
		int randomDirection = rand.nextInt(4);
		int randomChangement = rand.nextInt(20);
		
		this.changerDirection += randomChangement;
		if(this.changerDirection >= 200)
		{
			this.direction = randomDirection;
			this.changerDirection = 0;
				this.moving = true;
		}
	}
	
	
	/**
	 * Effectue le mouvement en calculant les futures coordoonn�es
	 * et en v�rifiant les collisions
	 * @param delta
	 */
	private void move(int delta)
	{
		if(this.moving) 
		{
			float futurX = getFuturX(delta);
			float futurY = getFuturY(delta);
						
			if(this.map.collision(futurX, futurY))
			{
				this.moving = false;
			}
			else
			{
				this.x = futurX;
				this.y = futurY;
			}
		}
	}
	
	/**
	 * Calcul des futures coordonn�es pour les collisions
	 */
	private float getFuturX(int delta)
	{
		float futurX = this.x;
		switch(this.direction) 
		{
			case 1: futurX = this.x - (0.3f * delta); break;
			case 3: futurX = this.x + (0.3f * delta); break;
		}
		return futurX;
	}
	
	/**
	 * Calcul des futures coordonn�es pour les collisions
	 */
	private float getFuturY(int delta)
	{
		float futurY = this.y;
		switch(this.direction) 
		{
			case 0: futurY = this.y - (0.3f * delta); break;
			case 2: futurY = this.y + (0.3f * delta); break;
		}
		return futurY;
	}
	
	/**
	 * Regarde sur la map entre l'unit� et l'objet pass� en param�tre si il y a un obstacle
	 * (une tuile de collision)
	 */
	private boolean obstacle(Entity entity)
	{
		boolean res = false;
		float xEntity = entity.getX();
		float yEntity = entity.getY();
		
		float pasX = (xEntity - this.x)/10;
		float pasY = (yEntity - this.y)/10;
		
		regardX = this.x;
		regardY = this.y;

		//La distance entre l'unit� et l'objet est divis�e en 10, on regarde sur chacun de ces 10
		//points si il y a une tuile de collision
		int i = 0;
		while(i < 10 && res == false)
		{
			regardX += pasX;
			regardY += pasY;
			res = map.collision(regardX, regardY);
			i++;
		}
		return res;
	}
	
	
	/**
	 * Rend la ressource la plus proche de l'unit�
	 * Utilise la fonciton Scan de la classe ressources
	 */
	private Nourriture scanRessources()
	{	
		ArrayList<Nourriture> nourritureTab = ressources.scan(this.x, this.y);
		if(!nourritureTab.isEmpty())
		{
			return Collections.min(nourritureTab,
									new Comparator<Nourriture>()
									{
										@Override
										public int compare(Nourriture n1, Nourriture n2)
										{
											float distance1 = Math.abs(n1.getX() - x) + Math.abs(n1.getY() - y);
											float distance2 = Math.abs(n2.getX() - x) + Math.abs(n2.getY() - y);
											
											if(distance1 > distance2) return 1;
											else if(distance1 < distance2) return -1;
											else return 0;
										}
									}
								   );
		}
		
		else return null;
	}
	
	/**
	 * Rend la l'unite la plus proche de l'unit�
	 * Utilise la fonciton Scan de la classe ressources
	 */
	private Unite scanEnnemi()
	{	
		ArrayList<Unite> uniteTab1 = factionEnnemie1.scan(this.x, this.y);
		ArrayList<Unite> uniteTab2 = factionEnnemie2.scan(this.x, this.y);
		uniteTab1.addAll(uniteTab2);
		
		if(!uniteTab1.isEmpty())
		{
			return Collections.min(uniteTab1,
									new Comparator<Unite>()
									{
										@Override
										public int compare(Unite n1, Unite n2)
										{
											float distance1 = Math.abs(n1.getX() - x) + Math.abs(n1.getY() - y);
											float distance2 = Math.abs(n2.getX() - x) + Math.abs(n2.getY() - y);
											
											if(distance1 > distance2) return 1;
											else if(distance1 < distance2) return -1;
											else return 0;
										}
									}
								   );
		}
		
		else return null;
	}
	
	/**
	 * On mange sur la Nourriture qui se trouve � indexNourriture
	 * @param indexNourriture
	 */
	private void manger(Nourriture nourriture)
	{
		this.moving = false;
		if(nourritureReserve < nourritureMax && mangerDeNouveau > 10)
		{
			nourritureReserve += nourriture.miner();	
			mangerDeNouveau = 0;
		}
		mangerDeNouveau++;
	}
	
	/**
	 * D�placement manuel de le renard
	 * @param key
	 */
	public void keyPressed(int key)
	{
		switch(key)
		{
			case Input.KEY_UP: this.direction = 0; this.moving = true; break;
			case Input.KEY_LEFT: this.direction = 1; this.moving = true; break;
			case Input.KEY_DOWN: this.direction = 2; this.moving = true; break;
			case Input.KEY_RIGHT: this.direction = 3; this.moving = true; break;
		}
	}
	
	/**
	 * On fluidifie le mouvement
	 * @param key
	 */
	public void keyReleased(int key)
	{
		if((key == Input.KEY_UP && this.direction == 0) ||
				(key == Input.KEY_LEFT && this.direction == 1) ||
				(key == Input.KEY_DOWN && this.direction == 2) ||
				(key == Input.KEY_RIGHT && this.direction == 3))
			this.moving = false;
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
	 * Affiche ce que detecte l'unit�
	 */
	public void setVision(boolean vision)
	{
		this.vision = vision;
	}
	
	/**
	 * Renvoie les pv
	 */
	public int getPV()
	{
		return this.pv;
	}

	/**
	 * 
	 */
	public void setAttaqueSubie()
	{
		attaqueSubie = 20;
	}

	public Unite getUniteCible()
	{
		return uniteCible;
	}

	@Override
	public ArrayList<Unite> scan(float x, float y)
	{
		ArrayList<Unite> res = new ArrayList<Unite>();
		if(pv > 0)
		{
			if(((this.x - x) > -300 && (this.x - x) < 300)
				&& ((this.y - y) > -300 && (this.y - y) < 300))
			res.add(this);
		}
		
		return res;
	}

	@Override
	public int getSize()
	{
		if (pv > 0)
			return 1;
		else
			return 0;
	}
}
