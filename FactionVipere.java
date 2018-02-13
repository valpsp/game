package jeu;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * Classe FactionVipere
 * Elle contient l'ensemble des poules et oeufs
 * Elle est appelée par le jeu et s'occupe de tout ce qui concerne les viperes
 * (mise à jour de leur état, suppression d'une viperes, ajout... etc)
 * @author Valentin
 *
 */
public class FactionVipere extends Faction
{
	//Contient les viperes et oeufs
	private ArrayList<Reptile> reptiles= new ArrayList<Reptile>();
	
	private boolean vision;
	
	private Map map;
	private Ressources ressources;
	private Faction factionEnnemie1;
	private Faction factionEnnemie2;	
	
	/**
	 * Constructeur par défaut
	 * @param map
	 */
	public FactionVipere(Map map, Ressources ressources, Faction factionEnnemie1, Faction factionEnnemie2)
	{
		this.map = map;
		this.ressources = ressources;
		this.factionEnnemie1 = factionEnnemie1;
		this.factionEnnemie2 = factionEnnemie2;
	}
	
	/**
	 * Affichage de la faction
	 * @param g
	 */
	public void render(Graphics g)
	{
		//On affiche d'abord les oeufs
		for(Reptile r : reptiles)
		{
			if(r.getClass().getSimpleName().equals("OeufVipere"))
			 r.render(g);
		}
		
		//Ensuite les viperes
		for(Reptile r : reptiles)
		{
			if(r.getClass().getSimpleName().equals("Vipere"))
			 r.render(g);
		}
	}
	
	/**
	 * Initialisation des viperes
	 * @throws SlickException
	 */
	public void init() throws SlickException
	{
		for(Reptile r: reptiles)
		{
			 r.init();
		}
	}
	
	
	/**
	 * Update des viperes et oeufs
	 * On appelle leur methode update l'un apres l'autre
	 * @param delta
	 */
	public void update(int delta) throws SlickException
	{
		for(int i = 0; i < reptiles.size(); i++)
		{	
			//update
			 reptiles.get(i).update(delta);
			 reptiles.get(i).setVision(vision);

			 
			 //Si c'est une Vipere on regarde si elle a un oeuf et on la fait pondre un nb aléatoire d'oeufs
			 if(reptiles.get(i).getClass().getSimpleName().equals("Vipere"))
			 {
				 if((reptiles.get(i)).getOeuf())
				 {
					 Random rand = new Random();
					 int randomPonte = rand.nextInt(4)+1;
					 
					 //On pond les oeufs..
					 for(int j = 0; j < randomPonte; j++)
					 {	
						 //ici on gère juste l'emplacement de la ponte si y a plus de 2 oeufs
						 if(j > 1)
							 this.addOeufVipere(reptiles.get(i).getX()+(6*j), reptiles.get(i).getY()-(4*j));
						 else
							 this.addOeufVipere(reptiles.get(i).getX()+(8*j), reptiles.get(i).getY()+(8*j));
					 }
				 }
					 
			 }
			 
			 //Si c'est un Oeuf on regarde si il a éclot et on met un vipere aléatoirement à la place de l'oeuf
			 if(reptiles.get(i).getClass().getSimpleName().equals("OeufVipere"))
			 {
				 if((reptiles.get(i)).getEclot())
				 {
					 Random rand = new Random();
					 int randomPonte = rand.nextInt(4);
					 
					 //Tous les oeufs ne vont pas donner des vipères
					 if(randomPonte > 1)
					 	this.addVipere(reptiles.get(i).getX(), reptiles.get(i).getY());
					 
					 reptiles.remove(i);
				 }
			 }
			 
			 //Si 0pv on la tue
			 if(i < reptiles.size() && reptiles.get(i).getPV() <= 0)
				 reptiles.remove(i);
		}
	}
	
	/**
	 * Renvoie un tableau des unites qui se trouvent à moins de 300 de x et y
	 */
	public ArrayList<Unite> scan(float x, float y)
	{
		ArrayList<Unite> vipereTab = new ArrayList<>();
		for(Reptile n : reptiles)
		{
			if(((n.getX() - x) > -300 && (n.getX() - x) < 300)
				&& ((n.getY() - y) > -300 && (n.getY() - y) < 300))
				vipereTab.add(n);
		}
		return vipereTab;
	}
	
	/**
	 * Assigne la faction ennemie 1
	 */
	public void setFactionEnnemie1(Faction factionEnnemie1)
	{
		this.factionEnnemie1 = factionEnnemie1;
	}
	
	/**
	 * Assigne la faction ennemie 2
	 */
	public void setFactionEnnemie2(Faction factionEnnemie2)
	{
		this.factionEnnemie2 = factionEnnemie2;
	}
	
	/**
	 * Ajoute une vipere à la faction
	 */
	private void addVipere(float x, float y)
	{
		Vipere nouvelleVipere = new Vipere(map, ressources, factionEnnemie1, factionEnnemie2, x, y);
		this.reptiles.add(nouvelleVipere);
		nouvelleVipere.init();
	}
	
	/**
	 * Ajoute un Oeuf à la faction
	 */
	private void addOeufVipere(float x, float y)
	{
		OeufVipere nouvelOeuf = new OeufVipere(map, x, y);
		this.reptiles.add(nouvelOeuf);
		nouvelOeuf.init();
	}
	
	/**
	 * Renvoie la taille de la Faction
	 */
	public int getSize()
	{
		return reptiles.size();
	}
	
	/**
	 * Actions au clavier pour controler et tester
	 * @param key
	 */
	public void KeyReleased(int key, float scale, float camX, float camY)
	{
		if(Input.KEY_O == key)
		{
			Point p = MouseInfo.getPointerInfo().getLocation();
			this.addVipere((float)p.getX()*(1/scale)-camX/scale,((float)p.getY()-50)*(1/scale)-camY/scale);
		}
		
		for(int i = 0; i < reptiles.size(); i++)
		{
			reptiles.get(i).keyReleased(key);
		}
	}
	
	/**
	 * Actions au clavier pour controler et tester
	 * @param key
	 */
	public void KeyPressed(int key)
	{		
		for(int i = 0; i < reptiles.size(); i++)
		{
			reptiles.get(i).keyPressed(key);
		}
		if(key == Input.KEY_L)
			this.vision = true;
		if(key == Input.KEY_K)
			this.vision = false;
	}
	
	/**
	 * On ajoute une Vipere au double click
	 * le calcul des coordonées prend en compte l'echelle et le positionnement de la camera
	 */
	public void mouseClicked(int button, int x, int y, int clickCount, float scale, float camX, float camY)
	{
		if(button == 0 && clickCount == 2)
		{
			this.addVipere(x*(1/scale)-camX/scale,y*(1/scale)-camY/scale);
		}
	}
}
