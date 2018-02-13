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
 * Classe Poule a partir de laquelle on va pouvoir instancier des unites poules
 * herite de volaille
 * @author Valentin
 *
 */
public class Poule extends Volaille
{
	//Pour les tests, où regarde l'unité
	private float regardX;
	private float regardY;
	private boolean vision; 
	
	//Coordonnées et mouvement
	private float x, y;
	private int direction;
	private boolean moving = false;
	private Animation[] animations = new Animation[24];
	private int changerDirection; 	//Symbolise "l'envie" de changer de direction, si > à un seuil on change
	
	//Points de vie
	private int pv = 350;
	
	//Ressource et oeuf
	private boolean oeuf;
	private Nourriture nourritureCible;
	private int nourritureReserve;
	private int nourritureMax = 50;
	private int mangerDeNouveau;
	
	//Combat
	private Unite uniteCible = null;
	private Unite uniteRenfort = null;
	private int forceAttaque = 5;
	private int attaquerDeNouveau;
	private int frequenceAttaque = 20; //au plus il est petit au plus la frequence est haute
	private float range = 15;
	private int attaqueSubie = 0;
	
	private Map map;
	private Ressources ressources;
	private Faction factionEnnemie1;
	private Faction factionEnnemie2;
	private Faction factionCopain;
	
	/**
	 * Constructeur par défaut
	 * @param map
	 * On passe la map en parametre pour pouvoir appeler sa methode "collision"
	 */
	public Poule(Map map, Ressources ressources, Faction factionEnnemie1, Faction factionEnnemie2, Faction factionCopain)
	{
		this.x = 200;
		this.y = 200;
		this.direction = 2;
		this.changerDirection = 0;
		
		this.oeuf = false;	
		this.nourritureReserve = 0;
		this.mangerDeNouveau = 0;
		
		this.attaquerDeNouveau = 0;
		
		this.map = map;
		this.ressources = ressources;
		this.factionEnnemie1 = factionEnnemie1;
		this.factionEnnemie2 = factionEnnemie2;
		this.factionCopain = factionCopain;
		
		//Pour les tests, où regarde l'unité
		this.regardX = this.x;
		this.regardY = this.y;
		this.vision = false;

	}
	
	/**
	 * Constructeur avec coordonnées
	 * @param map
	 */
	public Poule(Map map, Ressources ressources, Faction factionEnnemie1, Faction factionEnnemie2, Faction factionCopain, float x, float y)
	{
		this.x = x;
		this.y = y;
		this.direction = 2;
		this.changerDirection = 0;
		
		this.oeuf = false;
		this.nourritureReserve = 0;
		this.mangerDeNouveau = 0;
		
		this.attaquerDeNouveau = 0;

		this.map = map;
		this.ressources = ressources;
		this.factionEnnemie1 = factionEnnemie1;
		this.factionEnnemie2 = factionEnnemie2;	
		this.factionCopain = factionCopain;
		
		//Pour les tests, où regarde l'unité
		this.regardX = this.x;
		this.regardY = this.y;
		this.vision = false;
	}
	
	/**
	 * Initialisation de la poule
	 * On initialise ses sprites essentiellement
	 */
	public void init()
	{
		SpriteSheet spriteSheet;
		try
		{

			spriteSheet = new SpriteSheet("src/main/ressources/sprites/monsters/chicken.png", 36, 36);
			//Normale
			animations[0] = createAnimation(spriteSheet, 1, 1, 3);
			animations[1] = createAnimation(spriteSheet, 1, 1, 1);
			animations[2] = createAnimation(spriteSheet, 1, 1, 0);
			animations[3] = createAnimation(spriteSheet, 1, 1, 2);
			animations[4] = createAnimation(spriteSheet, 0, 2, 3);
			animations[5] = createAnimation(spriteSheet, 0, 2, 1);
			animations[6] = createAnimation(spriteSheet, 0, 2, 0);
			animations[7] = createAnimation(spriteSheet, 0, 2, 2);
			
			//Oeuf
			animations[8] = createAnimation(spriteSheet, 7, 7, 7);
			animations[9] = createAnimation(spriteSheet, 7, 7, 5);
			animations[10] = createAnimation(spriteSheet, 7, 7, 4);
			animations[11] = createAnimation(spriteSheet, 7, 7, 6);
			animations[12] = createAnimation(spriteSheet, 6, 8, 7);
			animations[13] = createAnimation(spriteSheet, 6, 8, 5);
			animations[14] = createAnimation(spriteSheet, 6, 8, 4);
			animations[15] = createAnimation(spriteSheet, 6, 8, 6);
			
			//Attaquée
			animations[16] = createAnimation(spriteSheet, 10, 10, 7);
			animations[17] = createAnimation(spriteSheet, 10, 10, 5);
			animations[18] = createAnimation(spriteSheet, 10, 10, 4);
			animations[19] = createAnimation(spriteSheet, 10, 10, 6);
			animations[20] = createAnimation(spriteSheet, 9, 11, 7);
			animations[21] = createAnimation(spriteSheet, 9, 11, 5);
			animations[22] = createAnimation(spriteSheet, 9, 11, 4);
			animations[23] = createAnimation(spriteSheet, 9, 11, 6);
			
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
	 * Affiche d'abord un ombre pour la poule, puis la poule par dessus
	 */
	public void render(Graphics g)
	{
		g.setColor(new Color(0,0,0,.5f));
		g.fillOval(x-10, y-8, 20, 12);
		
		//Pour les test, où l'unité regarde
		if(vision)
		{
			g.setColor(Color.red);
			g.fillOval(regardX-5, regardY-5, 12, 7);
			g.drawLine(this.x, this.y, regardX, regardY);
		}
		
		
		//Pas de mouvement ? on affiche les animations immobiles
		if(moving == false)
		{
			if(attaqueSubie > 10)
				g.drawAnimation(animations[direction+16], x-19, y-33);
			else if(nourritureReserve < nourritureMax)
				g.drawAnimation(animations[direction], x-19, y-33);
			else
				g.drawAnimation(animations[direction + 8], x-19, y-33);

		}
		//La poule se déplace ? On affiche les animations de mouvemnent
		else
		{
			if(attaqueSubie > 10)
				g.drawAnimation(animations[direction+16], x-19, y-33);
			else if(nourritureReserve < nourritureMax)
				g.drawAnimation(animations[direction + 4], x-19, y-33);
			else
				g.drawAnimation(animations[direction + 12], x-19, y-33);

		}
	}
	
	/**
	 * Update l'état de la poule
	 * On appelle sa fonction d'IA qui gère son comportement
	 */
	public void update(int delta)
	{
		//Pour les tests, où regarde la poule
		this.regardX = this.x;
		this.regardY = this.y;
				
		attaqueSubie--;
		
		this.IA(delta);
	}
	
	
	/**
	 * La fonction d'intelligence, elle va scanner l'environnement de l'unité
	 * puis appeler les fonctions qui gèrent son comportement specifique
	 * vis à vis des ressources ou autres unités
	 * @param delta
	 */
	private void IA(int delta)
	{	
		//On scanne les ressources à portée
		this.nourritureCible = this.scanRessources();
		
		this.uniteCible = this.scanEnnemi();
		
		if(uniteCible == null)
			this.uniteRenfort = this.scanCopain();
		
		//Si unite à portee et pas d'obstacle
		if(uniteCible != null && !obstacle(uniteCible))
		{
				this.IACible(uniteCible);
		}
		else if(uniteRenfort != null  && !obstacle(uniteRenfort.getUniteCible())&& !obstacle(uniteRenfort))
		{
			this.IARenfort(uniteRenfort);
		}
		//Si nourriture à portée et si on a faim
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
		this.ponte();

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
		
		//On regarde si la nourriture est assez proche pour être mangée
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
	 * Gère le comportement si cible detectée et pas d'obstacle
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
			this.attaquer(uniteCible);
		}
		//Si non on se dirige vers elle
		else
		{
			Random rand = new Random();
			int randomXouY = rand.nextInt(4);
			
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
	 * Gère le comportement si besoin de renfort detecté et pas d'obstacle
	 * @param uniteCible
	 */
	private void IARenfort(Unite uniteRenfort)
	{
		float xCible, yCible;
		
		xCible = uniteRenfort.getX();
		yCible = uniteRenfort.getY();
		

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
			this.moving = true;

	}
	
	/**
	 * On enlève des pv à l'unite cible à une certaine frequence
	 */
	public void attaquer(Unite uniteCible)
	{
		if(attaquerDeNouveau > frequenceAttaque)
		{
			uniteCible.subirAttaque(this.forceAttaque);
			uniteCible.setAttaqueSubie();
			attaquerDeNouveau = 0;
		}
		attaquerDeNouveau++;
	}
	
	/**
	 * est appelée par l'unité attaquante et retire des pv
	 */
	@Override
	public void subirAttaque(int force)
	{
		if(pv > 0)
		{
			this.pv -= force;
		}
	}
	
	
	/**
	 * Déplacement Aléatoire
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
	 * Effectue le mouvement en calculant les futures coordoonnées
	 * et en vérifiant les collisions
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
	 * Calcul des futures coordonnées pour les collisions
	 */
	private float getFuturX(int delta)
	{
		float futurX = this.x;
		switch(this.direction) 
		{
			case 1: futurX = this.x - (0.2f * delta); break;
			case 3: futurX = this.x + (0.2f * delta); break;
		}
		return futurX;
	}
	
	/**
	 * Calcul des futures coordonnées pour les collisions
	 */
	private float getFuturY(int delta)
	{
		float futurY = this.y;
		switch(this.direction) 
		{
			case 0: futurY = this.y - (0.2f * delta); break;
			case 2: futurY = this.y + (0.2f * delta); break;
		}
		return futurY;
	}
	
	/**
	 * Regarde sur la map entre l'unité et l'objet passé en paramètre si il y a un obstacle
	 * (une tuile de collision)
	 */
	private boolean obstacle(Entity entity)
	{
		boolean res = false;
		
		if(entity != null)
		{
			float xEntity = entity.getX();
			float yEntity = entity.getY();
			
			float pasX = (xEntity - this.x)/10;
			float pasY = (yEntity - this.y)/10;
			
			regardX = this.x;
			regardY = this.y;
	
			//La distance entre l'unité et l'objet est divisée en 10, on regarde sur chacun de ces 10
			//points si il y a une tuile de collision
			int i = 0;
			while(i < 10 && res == false)
			{
				regardX += pasX;
				regardY += pasY;
				res = map.collision(regardX, regardY);
				i++;
			}
		}
		return res;
	}
	
	/**
	 * Ponte aléatoire
	 */
	private void ponte()
	{
		Random rand = new Random();
		int randomPonte = rand.nextInt(700);
		if(randomPonte == 459 && nourritureReserve == this.nourritureMax)
		{
			oeuf = true;
			nourritureReserve = 0;
		}
		else
			oeuf = false;
	}
	
	/**
	 * return oeuf
	 */
	public boolean getOeuf()
	{
		return this.oeuf;
	}
	
	
	/**
	 * Rend la ressource la plus proche de l'unité
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
	 * On mange sur la Nourriture qui se trouve à indexNourriture
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
	 * Rend la l'unite la plus proche de l'unité
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
	 * Rend la l'unite la plus proche de l'unité
	 * Utilise la fonciton Scan de la classe ressources
	 */
	private Unite scanCopain()
	{	
		ArrayList<Unite> uniteTab = factionCopain.scan(this.x, this.y);
			

		for(int i = uniteTab.size()-1; i >= 0; i--)
		{
			if(uniteTab.get(i) == this || uniteTab.get(i).getUniteCible() == null)
					uniteTab.remove(i);
		}
		
		if(!uniteTab.isEmpty())
		{
			return Collections.min(uniteTab,
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
	 * Déplacement manuel de la poule
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
	 * Affiche ce que detecte l'unité
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
	 * Permet de faire clignoter l'unite quand attaquée
	 */
	public void setAttaqueSubie()
	{
		attaqueSubie = 20;
	}
	
	/**
	 * return l'unite Cible
	 */
	public Unite getUniteCible()
	{
		return this.uniteCible;
	}
}
