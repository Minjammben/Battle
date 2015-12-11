import java.io.*;
import java.util.*;

public class World implements WorldInterface{

	public ArrayList<Actor> actors = null;
	public ArrayList<String> graphics = null;

	private HashMap<String,Integer> allegiance = null;

	public Player player1 = null;
	public Player player2 = null;

	public int prevtimewaited = 0;

	public String killedcharacter = null;
	public boolean isgameover = false;

	public World(){
		actors = new ArrayList<Actor>();
		graphics = new ArrayList<String>();
		allegiance = new HashMap<String,Integer>();
	}

	public void init(ArrayList<Character> characters, HashMap<String,Integer> alleg){
		this.actors = new ArrayList<Actor>(characters);
		this.allegiance = new HashMap<String,Integer>(alleg);
		this.reset_game();
	}

	public void reset_game(){
		Graphics.draw_textln("NEW GAME");	
		this.player1 = new Player("You", (WorldInterface) this);
		this.player2 = new Player("Enemy", (WorldInterface) this);

		this.player2.isaicontrolled = true;

		String targetofp1 = null;
		String targetofp2 = null;
		for( int i = 0; i < this.actors.size(); ++i ){
			Character c = (Character) this.actors.get(i);
			c.reset_abilities();
			if( this.allegiance.get( c.name ) == 1 ){
				c.alleg = 1;
				c.isdead = false;
				c.currhp = c.maxhp;

				if( targetofp2 == null ) targetofp2 = c.name;
				this.player1.add_to_team(c);
			} else {
				c.alleg = 2;
				c.isdead = false;
				c.currhp = c.maxhp;
				if( targetofp1 == null ) targetofp1 = c.name;
				this.player2.add_to_team(c);
			}
		}

		this.isgameover = false;

		this.player1.init(targetofp1);
		this.player2.init(targetofp2);

		this.player1.update_ui();

		Graphics.typewrite_textln("\nGet ready...");

		try{ Thread.sleep(500); } catch( Exception e){};

		Graphics.typewrite_textln("...set...");

		try{ Thread.sleep(500); } catch( Exception e){};

		this.graphics.add("Battle!");
	}

	public void update(){
		this.player1.update();
		this.player2.update();

		int size = this.actors.size();
		for( int i = 0; i < size; ++i ){
			Actor a = this.actors.get(i);
			a.update();

			if( this.killedcharacter != null ){
				Character c = this.get_character( this.killedcharacter );
				this.add_graphic(this.killedcharacter + " is eradicated!");
				if( c.alleg == 1 ){
					this.add_graphic("Enemy team victory!");
				} else {
					this.add_graphic("Your victory!");
				}
				
				this.killedcharacter = null;
				this.isgameover = true;
			}
		}
	}

	public void draw(){
		this.prevtimewaited = 0;
		boolean drawui = this.isgameover;
		int size = this.graphics.size();
		for( int i = 0; i < size; ++i ){
			String s = this.graphics.get(i);

			if( s.indexOf("defensive") < 0 && s.indexOf("casting") < 0 ){
				drawui = true;
			}

			this.prevtimewaited += Graphics.typewrite_textln(s);
		}	

		if( drawui ){
			try{ Thread.sleep(500); } catch( Exception e){};
			this.prevtimewaited += 500; 

			Graphics.draw_textln("");
			ArrayList<Character> allies = new ArrayList<Character>();
			ArrayList<Character> enemies = new ArrayList<Character>();

			size = this.actors.size();
			for( int i = 0; i < size; ++i ){
				Character a = (Character) this.actors.get(i);
				if( a.alleg == 1 ){
					allies.add((Character)a);
				} else {
					enemies.add((Character)a);
				}
			}

			Graphics.draw_textln("Your team ---");
			for( int i = 0; i < allies.size(); ++i ){
				Character c = allies.get(i);
				c.draw();
			}

			Graphics.draw_textln("Enemy team ---");
			for( int i = 0; i < enemies.size(); ++i ){
				Character c = enemies.get(i);
				c.draw();
			}

			Graphics.draw_textln("");
			//Graphics.draw_textln("Your active target is "+this.player1.activetarget+".");
			//Graphics.draw_textln("");
		}

		this.graphics = new ArrayList<String>();

		if( this.isgameover ){
			this.show_game_over_screen();
		}	
	}

	public void destroy(){
		int arrsize = this.actors.size();
		for( int i = 0; i < arrsize; ++i ){
			Actor a = (Actor)this.actors.get(i);
			a.destroy();
		}		
	}

	public void add_actor( Actor a ){
		this.actors.add( a );
	}

	public void rem_actor( String name ){

	}

	public void show_game_over_screen(){
		Graphics.draw_textln("GAME OVER\n");	
		try{ Thread.sleep(5000); } catch( Exception e){};
		this.reset_game();
	}

	public Character get_character(String name){
		int size = this.actors.size();
		for( int i = 0; i < size; ++i ){
			Character c = (Character)this.actors.get(i);
			if( c.name.equals(name) ) return c;
		}
		return null;
	}

	public Character get_current_target(int alleg){
		if( alleg == 1 ){
			return this.get_character( this.player1.activetarget );
		} else {
			return this.get_character( this.player2.activetarget );
		}
	}

	public Player get_player(int i){
		if( i == 1 ){
			return this.player1;
		} else {
			return this.player2;
		}
	}

	public int get_time_waited(){
		return this.prevtimewaited;
	}

	public void add_graphic( String s ){
		this.graphics.add(s);
	}

	public void kill_character( String name ){
		this.killedcharacter = name;
	}

}

