package jeu;


import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;

/**
 * Classe principale contenant le main
 * elle contient les fonctions render, init et updates meres qui appellent celles de toutes les autres
 * (map, unite, ressources etc..)
 * @author Valentin
 *
 */
public class Game extends BasicGame
{
	private static GameContainer container;
	private Map map = new Map("src/main/ressources/map/mapOK.tmx");
	private Camera camera = new Camera();
	Ressources ressources = new Ressources();
	private FactionPoule factionPoule = new FactionPoule(map, ressources, null, null);
	private FactionVipere factionVipere = new FactionVipere(map, ressources, null, null);
	private Renard renard = Renard.getInstance(map, ressources, factionPoule, factionVipere,400, 400);
	private Hud hud = new Hud(factionPoule, factionVipere, renard);

	
	/**
	 * Constructeur
	 */
	public Game()
	{
		super("Poules Renards Vipères");
	}

	/**
	 * Affichage
	 */
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException
	{
		this.camera.render(g);
		this.map.renderBackground();
		this.factionVipere.render(g);
		this.factionPoule.render(g);
		this.renard.render(g);
		this.ressources.render(g);
		this.map.renderOverground();
		this.hud.render(g);
	}

	/**
	 * Initialisation
	 */
	@Override
	public void init(GameContainer container) throws SlickException
	{
		this.container = container;
		this.map.init();
		
		this.factionPoule.setFactionEnnemie1(factionVipere);
		this.factionPoule.setFactionEnnemie2(renard);
		this.factionPoule.init();
		
		this.factionVipere.setFactionEnnemie1(factionPoule);
		this.factionVipere.setFactionEnnemie2(renard);
		this.factionVipere.init();
		
		this.renard.init();
		
		this.ressources.init();
		
		
		this.hud.init();
	}
	
	/**
	 * Mise a jour
	 */
	@Override
	public void update(GameContainer container, int delta) throws SlickException
	{
		this.factionPoule.update(delta);
		this.factionVipere.update(delta);
		this.renard.update(delta);
		this.ressources.update(delta);
	}
	
	/**
	 * Gestion des touches appuyees pour les tests
	 */
	@Override
	public void keyPressed(int key, char c)
	{
		
		factionPoule.KeyPressed(key);
		factionVipere.KeyPressed(key);
	}
	
	@Override
	public void keyReleased(int key, char c) 
	{
		if(Input.KEY_ESCAPE == key)
			container.exit();
	
		float scale = this.camera.getScale();
		float camX = this.camera.getX();
		float camY = this.camera.getY();
		
		camera.keyReleased(key);
		factionPoule.KeyReleased(key, scale, camX, camY);
		factionVipere.KeyReleased(key, scale, camX, camY);
	}
	
	/**
	 * Gestion de la souris pour les tests
	 */
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount)
	{
			float scale = this.camera.getScale();
			float camX = this.camera.getX();
			float camY = this.camera.getY();

			factionPoule.mouseClicked(button, x, y, clickCount, scale, camX, camY);
			factionVipere.mouseClicked(button, x, y, clickCount, scale, camX, camY);
			ressources.mouseClicked(button, x, y, clickCount, scale, camX, camY);
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy)
	{
		camera.mouseDragged(oldx, oldy, newx, newy);
	}
	
	@Override
	public void mouseWheelMoved(int change)
	{
		camera.mouseWheelMoved(change);
	}
	
	/**
	 * Fonction principale
	 */
	public static void main(String[] args) throws SlickException
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		AppGameContainer app = new AppGameContainer(new Game(), (int)screenSize.getWidth()-70, (int)screenSize.getHeight()-100, false);
		app.setTargetFrameRate(60);
		app.start();
	}
}
