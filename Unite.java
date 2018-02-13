package jeu;

/**
 * interface representant une volaille, un reptile ou un renard
 * @author Valentin
 *
 */
public interface Unite extends Entity
{
	float getX();
	float getY();
	void subirAttaque(int force);
	void setAttaqueSubie();
	Unite getUniteCible();
}
