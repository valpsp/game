package jeu;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * Classe regroupant toutes les nourritures presentes sur la map, elle les met a jour, les supprime quand c'est
 * necessaire et permet de les scanner pour que l'IA des unites sache si des nourritures se trouvent a proximite
 * @author Valentin
 *
 */
public class Ressources
{
	private ArrayList<Nourriture> nourritures = new ArrayList<Nourriture>();
	
	public Ressources()
	{
		//for(long i = 0; i < 500; i++)
		//this.nourritures.add(new Nourriture(700,700));
		
		


	}
	
	/**
	 * Affichage des nourritures
	 * @param g
	 */
	public void render(Graphics g)
	{		
		for(Nourriture n : nourritures)
		{
			 n.render(g);
		}
	}
	
	/**
	 * Initialisation des nourritures
	 * @throws SlickException
	 */
	public void init() throws SlickException
	{
		for(Nourriture n : nourritures)
		{
			 n.init();
		}
	}
	
	/**
	 * Update des nourritures
	 * @param delta
	 */
	public void update(int delta) throws SlickException
	{
		for(int i = 0; i < nourritures.size(); i++)
		{	
			 nourritures.get(i).update(delta);
			 
			 if(nourritures.get(i).getReserve() <= 0)
				 nourritures.remove(i);
		}
	}

	/**
	 * Appelée par les unités dans le scan des ressources
	 * (le scan s'effectue réellement ici)
	 * L'unité passe en paramètre ses coordonnées et on return un tableau
	 * des ressources qu'elle peut detecter
	 */
	public ArrayList<Nourriture> scan(float x, float y)
	{
		ArrayList<Nourriture> nourritureTab = new ArrayList<>();
		for(Nourriture n : nourritures)
		{
			//On vérifie si l'unité est assez proche pour pouvoir miner
			//Si oui, on renvoie l'index de la ressource minable
			if(((n.getX() - x) > -300 && (n.getX() - x) < 300)
				&& ((n.getY() - y) > -300 && (n.getY() - y) < 300))
				nourritureTab.add(n);
		}
		return nourritureTab;
	}
	
	/**
	 * return la Nourriture se trouvant à l'indicie i
	 */
	public Nourriture getNourriture(int i)
	{
		return nourritures.get(i);
	}
	
	/**
	 * Ajoute une Nourriture en x, y
	 */
	private void addNourriture(float x, float y)
	{
		Nourriture nouvelleNourriture = new Nourriture( x, y);
		this.nourritures.add(nouvelleNourriture);
		nouvelleNourriture.init();
	}
	
	/**
	 * On ajoute une nourriture au double click
	 * le calcul des coordonées prend en compte l'echelle et le positionnement de la camera
	 */
	public void mouseClicked(int button, int x, int y, int clickCount, float scale, float camX, float camY)
	{
		if(button == 1)
		{
			this.addNourriture(x*(1/scale)-camX/scale,y*(1/scale)-camY/scale);
			this.addNourriture(x*(1/scale)-camX/scale+30,y*(1/scale)-camY/scale);
			this.addNourriture(x*(1/scale)-camX/scale+12,y*(1/scale)-camY/scale+12);

		}
	}
	
}
