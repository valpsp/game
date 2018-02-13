package jeu;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * Classe FactionPoule
 * Elle contient l'ensemble des poules et oeufs
 * Elle est appelée par le jeu et s'occupe de tout ce qui concerne les poules
 * (mise à jour de leur état, suppression d'une poule, ajout... etc)
 * @author Valentin
 *
 */
public class FactionPoule extends Faction
{
	//Contient les poules et oeufs
	private ArrayList<Volaille> volailles = new ArrayList<Volaille>();
	
	private boolean vision;
	
	private Map map;
	private Ressources ressources;
	private Faction factionEnnemie1;
	private Faction factionEnnemie2;
	
	/**
	 * Constructeur par défaut
	 * @param map
	 */
	public FactionPoule(Map map, Ressources ressources, Faction factionEnnemie1, Faction factionEnnemie2)
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
		for(Volaille v : volailles)
		{
			if(v.getClass().getSimpleName().equals("OeufPoule"))
			 v.render(g);
		}
		
		//Ensuite les poules
		for(Volaille v : volailles)
		{
			if(v.getClass().getSimpleName().equals("Poule"))
			 v.render(g);
		}
	}
	
	/**
	 * Initialisation des poules
	 * @throws SlickException
	 */
	public void init() throws SlickException
	{
		for(Volaille v : volailles)
		{
			 v.init();
		}
	}
	
	
	/**
	 * Update des poules et oeufs
	 * On appelle leur methode update l'un apres l'autre
	 * @param delta
	 */
	public void update(int delta) throws SlickException
	{
		for(int i = 0; i < volailles.size(); i++)
		{	
			//update
			 volailles.get(i).update(delta);
			 volailles.get(i).setVision(vision);
			 
			 //Si c'est une Poule
			 if(volailles.get(i).getClass().getSimpleName().equals("Poule"))
			 {
				 // on regarde si elle a un oeuf et on la fait pondre
				 if((volailles.get(i)).getOeuf())
					 this.addOeufPoule(volailles.get(i).getX(), volailles.get(i).getY());
			 }
			 
			 //Si c'est un Oeuf on regarde si il a éclot et on met un poule à la place de l'oeuf
			 if(volailles.get(i).getClass().getSimpleName().equals("OeufPoule"))
			 {
				 if((volailles.get(i)).getEclot())
				 {
					 this.addPoule(volailles.get(i).getX(), volailles.get(i).getY());
					 volailles.remove(i);
				 }
			 }
			 
			 //Si 0pv on la tue
			 if(volailles.get(i).getPV() <= 0)
				 volailles.remove(i);
		}
	}
	
	/**
	 * Renvoie un tableau des unites qui se trouvent à moins de 300 de x et y
	 */
	public ArrayList<Unite> scan(float x, float y)
	{
		ArrayList<Unite> pouleTab = new ArrayList<>();
		for(Volaille n : volailles)
		{
			if(((n.getX() - x) > -300 && (n.getX() - x) < 300)
				&& ((n.getY() - y) > -300 && (n.getY() - y) < 300))
				pouleTab.add(n);
		}
		return pouleTab;
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
	 * Ajoute une poule à la faction
	 */
	private void addPoule(float x, float y)
	{
		Poule nouvellePoule = new Poule(map, ressources, factionEnnemie1, factionEnnemie2, this, x, y);
		this.volailles.add(nouvellePoule);
		nouvellePoule.init();
	}
	
	/**
	 * Ajoute un Oeuf à la faction
	 */
	private void addOeufPoule(float x, float y)
	{
		OeufPoule nouvelOeuf = new OeufPoule(map, x, y);
		this.volailles.add(nouvelOeuf);
		nouvelOeuf.init();
	}
	
	/**
	 * Renvoie la taille de la Faction
	 */
	public int getSize()
	{
		return volailles.size();
	}
	
	/**
	 * Actions au clavier pour controler et tester
	 * @param key
	 */
	public void KeyReleased(int key, float scale, float camX, float camY)
	{
		if(Input.KEY_P == key)
		{
			Point p = MouseInfo.getPointerInfo().getLocation();
			this.addPoule((float)p.getX()*(1/scale)-camX/scale,((float)p.getY()-30)*(1/scale)-camY/scale);
		}
		
		for(int i = 0; i < volailles.size(); i++)
		{
			 volailles.get(i).keyReleased(key);
		}
	}
	
	/**
	 * Actions au clavier pour controler et tester
	 * @param key
	 */
	public void KeyPressed(int key)
	{		
		for(int i = 0; i < volailles.size(); i++)
		{
			volailles.get(i).keyPressed(key);
		}
		if(key == Input.KEY_L)
			this.vision = true;
		if(key == Input.KEY_K)
			this.vision = false;
	}
	
	/**
	 * On ajoute une poule au double click
	 * le calcul des coordonées prend en compte l'echelle et le positionnement de la camera
	 */
	public void mouseClicked(int button, int x, int y, int clickCount, float scale, float camX, float camY)
	{
		if(button == 0 && clickCount == 2)
		{
			//this.addPoule(x*(1/scale)-camX/scale,y*(1/scale)-camY/scale);
		}
	}
}
