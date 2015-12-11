import java.io.*;
import java.util.ArrayList;

public abstract class StatusEffect {

	public static final int NUM = 1;
	public static final int RECOVERING = 0;

	public String name = null;
	public String charname = null;

	protected WorldInterface world;

	public StatusEffect(String name, String charname, WorldInterface w){
		this.name = name;
		this.world = w;
		this.charname = charname;

	}

	public static StatusEffect new_statuseffect(int type, String charname, WorldInterface w){
		switch( type ){
			case RECOVERING: return new Recovering("Recovering", charname, w);
			default: return new Recovering("Recovering", charname, w);
		}
	}

	public void pre_effect(Character c){}
	public void perform_effect(Character c){}
	public void post_effect(Character c){}
	public int on_taken_damage_effect(Character c, int damagetaken){ return damagetaken; }
	public int on_do_damage_effect(Character c, int damagedealt){ return damagedealt; }
	public void on_cooldown_effect(Character c){}
	public int on_heal_effect(Character c, int healamt){ return healamt; };

}

class Recovering extends StatusEffect { 

	public Recovering( String name, String charname, WorldInterface w){
		super(name, charname, w);
	} 

	public int on_do_damage_effect(Character c, int damagedealt){
		return damagedealt-(damagedealt/2);
	}
}