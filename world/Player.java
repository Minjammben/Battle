import java.io.*;
import java.util.*;

public class Player extends Actor{

	public String name = "none";
	public boolean isaicontrolled = false;

	public String activecharacter = null;
	public String activetarget = null;

	public ArrayList<Character> team;

	public Player(String name, WorldInterface w){
		super(w);

		this.name = name;
		this.world = w;

		this.team = new ArrayList<Character>();
		//System.out.println( " which was also a Player ("+name+").");
	}

	public void init(String target){
		this.activecharacter = this.team.get(0).name;
		this.activetarget = target;
	}

	public void draw(){

	}

	public void update(){
		if( this.isaicontrolled ){
			this.ai();
		} else {
			this.update_ui();
		}
	}

	public void update_ui(){
		Character c = this.world.get_character( this.activecharacter );

		for( int i = 1; i < c.abilities.size(); ++i ){
			Action a = c.abilities.get(i);
			if( a.name.equals("Recover") ){
				if( a.is_active() ){
					Graphics.set_cd_ui( i, -2 );
				} else {
					Graphics.set_cd_ui( i, -3 );
				}
			} else if( a.is_active() ){
				Graphics.set_cd_ui( i, -1 );
			} else {
				Graphics.set_cd_ui( i, a.cooldown - a.cdtimer );
			}
		}

		Graphics.lockbuttons = false;
	}

	public void ai(){
		Character c = this.world.get_character( this.activecharacter );

		if( c.caction.name.equals("Wait") ){
			for( int i = 1; i < c.abilities.size(); ++i ){
				Action a = c.abilities.get(i);
				if( a.is_ready() && !a.is_active() ){
					c.set_active_ability(i);
					break;
				}
			}
		}
	}

	public void destroy(){
	}

	public void add_to_team(Character c){
		this.team.add(c);
	}

	public void reset_team(){
		this.team = new ArrayList<Character>();
	}

	public void perform_turn(){

	}

}