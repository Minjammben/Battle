import java.io.*;
import java.util.ArrayList;

public abstract class Action{

	public static final int NUMACTIONS = 6;

	public static final int WAIT = 0;
	public static final int ATTACKSINGLE = 1;
	public static final int DEFEND = 2;
	public static final int FIREBALL = 3;
	public static final int ATTACKTUTORIAL = 4;
	public static final int RECOVER = 5;

	public static final int DPHYSICAL = 0;
	public static final int DMAGICAL = 1;

	public String name = "";
	public WorldInterface world;

	public int cooldown = 1;
	public int cdtimer = 0;

	public Action(String name, WorldInterface w){
		this.name = name;
		this.world = w;
	}

	public static Action new_action(int type, WorldInterface w){
		switch( type ){
			case WAIT: return new Wait("Wait", w);
			case ATTACKTUTORIAL: return new AttackTutorial("AttackTut", w);
			case ATTACKSINGLE: return new AttackSingle("Attack", w);
			case DEFEND: return new Defend("Defend", w);
			case FIREBALL: return new Fireball("Fireball", w);
			case RECOVER: return new Recover("Recover", w);
			default: return new Wait("Wait", w);
		}
	}

	public static int apply_damage( Character attacker, Character victim, int rawdamage, int dtype, boolean fromstatus, WorldInterface world ){
		int finaldamage = 0;
		float damage = rawdamage;

		if( dtype == Action.DPHYSICAL ){
			float armorratio = ((float)victim.armor/1.2f)/100f;
			//System.out.println("DEBUG: RAW DAMAGE " + damage + " REDUCED BY " + armorratio*100 + "%.");
			finaldamage = rawdamage - Math.round(damage * (armorratio));
		} else if( dtype == Action.DMAGICAL ){
			float armorratio = ((float)victim.armor/4f)/100f;
			//System.out.println("DEBUG: RAW DAMAGE " + damage + " REDUCED BY " + armorratio*100 + "%.");
			finaldamage = rawdamage - Math.round(damage * (armorratio));
		}

		if( !fromstatus ){
			for( int i = 0; i < attacker.effects.size(); ++i ){
				StatusEffect e = attacker.effects.get(i);
				finaldamage = e.on_do_damage_effect(attacker, finaldamage);
			}

			for( int i = 0; i < victim.effects.size(); ++i ){
				StatusEffect e = victim.effects.get(i);
				finaldamage = e.on_taken_damage_effect(victim, finaldamage);
			}			
		}

		victim.currhp -= finaldamage;
		if( victim.currhp <= 0 ){
			victim.currhp = 0;
			victim.isdead = true;
			world.kill_character( victim.name );
		}

		return finaldamage;		
	}

	public static int heal( Character healer, Character patient, int healamt, boolean fromstatus, WorldInterface w ){
		
		int mod = (healamt + patient.currhp - patient.maxhp);
		int finalheal = healamt - ((mod > 0) ? mod : 0);

		patient.currhp += finalheal;

		return finalheal;
	}

	public void pre_action(){};
	public boolean perform_action(Character c){
		if( this.cdtimer < this.cooldown ){
			return false;
		} 
		return true; 
	};
	public void post_action(){};
	public void get_frame(Character c){};
	public void reset(){};
	public void reset_timer(){this.cdtimer = 0; }
	public void reset_on_finish(){}
	public void reset_all(){};
	public void perform_set_action(Character c){};
	public void toggle(Character c){};

	public void increment_cooldown(){
		this.cdtimer++;
		if( this.cdtimer > this.cooldown ){
			this.cdtimer = this.cooldown;
		}
	}

	public boolean is_ready(){
		return this.cdtimer >= this.cooldown;	
	}

	public boolean is_active(){
		return false;
	}
}

abstract class ActiveAction extends Action {

	public int activecooldownmax = 0;
	public int activecdtimer = 0;
	public int failurecooldown = 0;
	public int successcooldown = 0;
	public boolean isactive = false;

	public ActiveAction(String name, WorldInterface w ){
		super(name,w);
	}

	public void reset(){
		this.activecdtimer = 0;
	}

	public void reset_all(){
		this.isactive = false;
		this.reset();
	}

	public boolean is_active(){
		return this.isactive;
	}

	public int interrupt_action(Character attacker, Character defender){
		return 0;
	}
}

abstract class ToggleAction extends Action {

	public boolean isactive = false;

	public ToggleAction(String name, WorldInterface w ){
		super(name,w);
	}

	public void reset_all(){
		this.isactive = false;
		this.reset();
	}

	public void perform_set_action(){
		this.isactive = true;
	}

	public void toggle(Character c){
		this.isactive = !this.isactive;
	}

	public boolean is_active(){
		return this.isactive;
	}
}

class Recover extends ToggleAction {

	int ticktimer = 0;
	int maxticktimer = 4;

	public Recover(String name, WorldInterface w){
		super(name,w);
		this.cooldown = 0;
		this.cdtimer = 0;
	}

	public boolean perform_action(Character c){
		if( ticktimer == maxticktimer ){
			int healamt = Action.heal( c, c, 3, false, this.world );
			this.world.add_graphic(c.name + " recovers " + healamt + "hp.");
			ticktimer = 0;
		} else {
			ticktimer++;
		} 
		return true;
	}

	public void perform_set_action(Character c){
		super.perform_set_action();	
	}

	public void reset(){
		ticktimer = 0;
		this.isactive = false;
	}

	public void toggle(Character c){
		super.toggle(c);
		if( this.isactive ){
			this.world.add_graphic(c.name + " starts to recover hp.");
			c.add_effect( StatusEffect.RECOVERING, c.name );
		} else {
			this.world.add_graphic(c.name + " stops recovering hp.");
			c.remove_effect( "Recovering" );
		}
	}
}

class Wait extends Action{

	public Wait(String name, WorldInterface w){
		super(name,w);
		this.cooldown = 0;
	}

	public boolean perform_action(Character c){
		this.cdtimer = 0;
		return true;
	}
}

class AttackTutorial extends Action{

	public AttackTutorial(String name, WorldInterface w){
		super(name,w);
		this.cooldown = 12;
	}

	public boolean perform_action(Character c){
		super.perform_action(c);

		int damage = c.get_damage();
		Character victim = this.world.get_current_target( c.alleg );
		boolean wasblocked = false;

		if( victim.caction instanceof ActiveAction && victim.caction.is_active() ){
			ActiveAction d = (ActiveAction)victim.caction;
			this.cdtimer = d.interrupt_action( victim, c );
			if( this.cdtimer != 0 ) wasblocked = true;
		} 

		if( !wasblocked ) {
			damage = Action.apply_damage( c, victim, damage, Action.DPHYSICAL, false, this.world );
			this.world.add_graphic( c.name + " attacks " + victim.name + " for " + damage +" damage!");

			int counterchance = Math.round(((float)victim.counter)/2f);
			int calc = (int)(Math.random() * ((100) + 1));

			if( calc < counterchance ){
				
			}
			this.cdtimer = 0;
		}

		return true;
	}
}

class AttackSingle extends Action{

	public AttackSingle(String name, WorldInterface w){
		super(name,w);
		this.cooldown = 7;
	}

	public boolean perform_action(Character c){
		super.perform_action(c);

		int damage = c.get_damage();
		Character victim = this.world.get_current_target( c.alleg );
		boolean wasblocked = false;

		if( victim.caction instanceof ActiveAction && victim.caction.is_active() ){
			ActiveAction d = (ActiveAction)victim.caction;
			this.cdtimer = d.interrupt_action( victim, c );
			if( this.cdtimer != 0 ) wasblocked = true;
		} 

		if( !wasblocked ) {
			damage = Action.apply_damage( c, victim, damage, Action.DPHYSICAL, false, this.world );
			this.world.add_graphic( c.name + " attacks " + victim.name + " for " + damage +" damage!");

			int counterchance = Math.round(((float)victim.counter)/2f);
			int calc = (int)(Math.random() * ((100) + 1));

			if( calc < counterchance ){
				
			}
			this.cdtimer = 0;
		}

		return true;
	}
}

class Defend extends ActiveAction{

	public Defend(String name, WorldInterface w){
		super(name,w);

		this.cooldown = 7;
		this.activecooldownmax = 5;
		this.failurecooldown = 20;
		this.successcooldown = 7;
		this.reset();
	}

	public boolean perform_action(Character c){
		super.perform_action(c);

		this.isactive = true;
		if( this.activecdtimer >= this.activecooldownmax ){
			this.cdtimer = 0;
			this.cooldown = this.failurecooldown;
			this.world.add_graphic( c.name + " did not defend an attack.");
			this.isactive = false;
		} else {
			//this.world.add_graphic( c.name + " is defending...");
		}
		this.activecdtimer++;

		return true;
	}

	public void reset_all(){
		super.reset_all();
		this.cooldown = this.successcooldown;
	}

	public void reset_on_finish(){
		this.cooldown = this.failurecooldown;
	}

	public void perform_set_action(Character c){
		this.world.add_graphic( c.name + " enters a defensive stance.");
	}

	public int interrupt_action(Character defender, Character attacker){
		int recoildamage = defender.get_damage()/2;
		int finaldamage = Action.apply_damage( defender, attacker, recoildamage, Action.DPHYSICAL, false, this.world );

		this.cooldown = this.successcooldown;
		this.cdtimer = 0;
		this.isactive = false;

		this.world.add_graphic( attacker.name + " attacks but " + defender.name + " successfully blocks!");
		this.world.add_graphic( attacker.name + " takes " + finaldamage +" damage from the recoil.");
		return -defender.counter;
	}
}

class Fireball extends ActiveAction{

	int basedamage = 0;
	float ratio = 0.0f;

	public Fireball(String name, WorldInterface w){
		super(name,w);

		this.activecooldownmax = 5;
		this.failurecooldown = 20;
		this.successcooldown = 5;
		this.cooldown = 10;

		this.basedamage = 20;
		this.ratio = 0.5f;
	}

	public boolean perform_action(Character c){
		super.perform_action(c);

		this.isactive = true;
		if( this.activecdtimer >= this.activecooldownmax ){
			this.cdtimer = 0;
			this.cooldown = this.successcooldown;

			float moddamage = (float)c.get_damage();
			int damage = this.basedamage + Math.round( moddamage*this.ratio );

			Character victim = this.world.get_current_target( c.alleg );			
			int finaldamage = Action.apply_damage( c, victim, damage, Action.DMAGICAL, false, this.world );

			this.world.add_graphic( "------ BOOM! ------");
			this.world.add_graphic( c.name + " blasts "+victim.name+" with a fireball for " + finaldamage +" damage!");
			this.isactive = false;
		} 

		this.activecdtimer++;
		return true;
	}

	public void perform_set_action(Character c){
		this.world.add_graphic( c.name + " starts casting a fireball...");
	}

	public int interrupt_action(Character defender, Character attacker){
		this.cdtimer = 0;
		this.cooldown = this.failurecooldown;
		this.world.add_graphic( attacker.name + "'s attack interrupts " + defender.name + "'s fireball!");
		this.isactive = false;

		return 0;
	}	
}
