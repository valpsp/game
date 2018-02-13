package jeu;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/**
 *La map qui sera instanciee, se base sur un fichier TiledMap 
 * @author Valentin
 *
 */
public class Map
{
	//La map est faite � l'aide d'un logiciel nomm� Tiled
	private TiledMap map;
	
	//R�f�rence du fichier.tmx
	String ref;

	/**
	 * Constructeur par d�faut
	 * @param ref
	 */
	public Map(String ref)
	{
		this.ref = ref;
	}
	
	/**
	 * Initialisation
	 */
	public void init() throws SlickException
	{
		this.map = new TiledMap(ref);
	}
	
	
	//NOTE: La map est constitu�e de plusieurs couches,
	//Il faut donc afficher certaines couches avant d'autres pour garder
	//une certaine coh�rence (par ex affichage des unit�s sur le sol mais derri�re les arbres
	
	/**
	 * Affichage de tous les �l�ments se trouvant derri�re les unit�s
	 */
	public void renderBackground()
	{
		this.map.render(0, 0, 0);
		this.map.render(0, 0, 1);
		this.map.render(0, 0, 2);

	}
	
	/**
	 * Affichage de tous les �l�ments se trouvant au dessus les unit�s
	 */
	public void renderOverground()
	{
		this.map.render(0, 0, 3);
		this.map.render(0, 0, 4);
	}
	
	/**
	 * Gestion des collisions (appel�e dans les fonctions de mouvement des unit�s)
	 * On passe en param�tre les futures coordonn�es de l'unit� et on v�rifie si
	 * un objet de collision est pr�sent � cet endroit
	 * @param futurX
	 * @param futurY
	 */
	public boolean collision(float futurX, float futurY)
	{
		Image tuile = this.map.getTileImage((int) futurX / 32,
											(int) futurY / 32,
											this.map.getLayerIndex("collision"));
		
		boolean collision = (tuile != null);
		return collision;
	}
	
	/**
	 * return l'objet TiledMap de la Map
	 */
	public TiledMap getTiledMap()
	{
		return map;
	}
}
