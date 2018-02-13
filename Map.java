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
	//La map est faite à l'aide d'un logiciel nommé Tiled
	private TiledMap map;
	
	//Référence du fichier.tmx
	String ref;

	/**
	 * Constructeur par défaut
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
	
	
	//NOTE: La map est constituée de plusieurs couches,
	//Il faut donc afficher certaines couches avant d'autres pour garder
	//une certaine cohérence (par ex affichage des unités sur le sol mais derrière les arbres
	
	/**
	 * Affichage de tous les éléments se trouvant derrière les unités
	 */
	public void renderBackground()
	{
		this.map.render(0, 0, 0);
		this.map.render(0, 0, 1);
		this.map.render(0, 0, 2);

	}
	
	/**
	 * Affichage de tous les éléments se trouvant au dessus les unités
	 */
	public void renderOverground()
	{
		this.map.render(0, 0, 3);
		this.map.render(0, 0, 4);
	}
	
	/**
	 * Gestion des collisions (appelée dans les fonctions de mouvement des unités)
	 * On passe en paramètre les futures coordonnées de l'unité et on vérifie si
	 * un objet de collision est présent à cet endroit
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
