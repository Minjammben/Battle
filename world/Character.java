import java.io.*;
import java.util.ArrayList;

public class Character extends Actor{

	public String name = "";
	public Action caction = null;
	public ArrayList<Action> abilities = null;
	public ArrayList<StatusEffect> effects = null;

	public int alleg = 0;

	//stats
	//DAMAGE based off of total values of lvl
	public int weaponlvl = 1;		//higher weapon lvl, better equipable weapons
	public int speed = 1;			//higher speed, lower ability cooldown
	public int itemlvl = 0;			//higher item lvl, better usable items
	public int maglvl = 0;			//higher mag lvl, better usable spells
	public int armor = 10;			//higher armor, more resistance to damage
	public int counter = 1;			//number = %chance/2 to auto counter a physical attack
	public int counterdamage = 1; 	//bonus damage that your counter does
	public int resist = 1;   		//number = %chance/2 to resist an incoming status effect
	public int dotdamage = 1; 		//higher dotdamage, higher damage over time spells and effects

	public int maxhp = 50;
	public int currhp = 50;

	public Character(String name, ArrayList<Integer> abil, WorldInterface w){
		super(w);

		this.name = name;

		this.abilities = new ArrayList<Action>();
		this.abilities.add( Action.new_action(0, (WorldInterface) this.world)); 
		for( int i = 0; i < abil.size(); ++i ){
			this.abilities.add( Action.new_action(abil.get(i), (WorldInterface) this.world) );
		}

		this.effects = new ArrayList<StatusEffect>();

		this.caction = this.abilities.get(0);
	}

	public void draw(){

		String effectstr = "";

		for( int i = 0; i < this.effects.size(); ++i ){
			StatusEffect e = this.effects.get(i);
			effectstr += " " + e.name;
		}

		if( effectstr.equals("") ){
			Graphics.draw_textln(" "+this.name+": "+this.currhp+"/"+this.maxhp+" hp.");			
		} else {
			Graphics.draw_textln(" "+this.name+": "+this.currhp+"/"+this.maxhp+" hp. Status Effects:" + effectstr);
		}
	}

	public void update(){
		//reset to the 'Wait' action when another action is finished
		if( this.caction.is_ready() == false ){
			this.caction = this.abilities.get(0);
		}

		if( this.isdead ) return;

		this.caction.perform_action(this);

		for( int i = 0; i < this.abilities.size(); ++i ){
			Action a = this.abilities.get(i);

			if( a.is_active() && !a.name.equals(this.caction.name) ){
				a.perform_action(this);
			}

			a.increment_cooldown();
		}
	}

	public void destroy(){
	}

	public boolean set_active_ability(int i){
		Action a = this.abilities.get(i);
		if( this.caction.name.equals("Wait") || this.caction.name.equals("Defend") ){
			this.caction.reset_all();
			this.caction = a;
			this.caction.reset();

			this.caction.perform_set_action(this);

			return true;
		} else {
			return false;
		}
	}

	public void reset_abilities(){
		this.effects = new ArrayList<StatusEffect>();

		for( int i = 0; i < this.abilities.size(); ++i ){
			Action a = this.abilities.get(i);
			a.reset_timer();
			a.reset_all();
			a.reset();
		} 
	}

	public void toggle_ability(int ind){
		Action a = this.abilities.get(ind);
		a.toggle(this);
	}

	public int get_damage(){
		return weaponlvl + speed + itemlvl + maglvl + armor + counter + counterdamage + resist + dotdamage;
	}

	public void add_effect(int i, String charname){
		StatusEffect e = StatusEffect.new_statuseffect(0, charname, this.world);
		this.effects.add(e);
	}

	public void remove_effect(String name){
		for( int i = 0; i < this.effects.size(); ++i ){
			StatusEffect e = this.effects.get(i);
			if( e.name.equals(name) ){
				this.effects.remove(i);
				break;
			}
		}
	}

}