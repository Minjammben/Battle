import java.io.*;
import java.util.*;

public class WorldState extends State{

	WorldInterface world;

	public WorldState(GameInterface g){
		super(g);

		//System.out.println(" which was also a WorldState.");

		this.world = new World();
		this.init();
	}

	void init(){

		String[] names = {"playerchar1", "enemychar1", "playerchar2", "playerchar3", "enemychar2", "enemychar3"};

		ArrayList<Character> characters = new ArrayList<Character>();
		ArrayList<Integer> defaultactions = new ArrayList<Integer>();
		defaultactions.add(1);
		defaultactions.add(2);
		defaultactions.add(3);
		defaultactions.add(5);

		ArrayList<Integer> defaultactions1 = new ArrayList<Integer>();
		defaultactions1.add(1);
		defaultactions1.add(2);
		defaultactions1.add(3);		
		for( int i = 0; i < 1; ++i ){
			characters.add( new Character(names[i], defaultactions, this.world) );
		}	
		for( int i = 1; i < 2; ++i ){
			characters.add( new Character(names[i], defaultactions1, this.world) );
		}	

		HashMap<String,Integer> alleg = new HashMap<String,Integer>();
		alleg.put("playerchar1", 1);
		//alleg.put("playerchar2", 1);
		//alleg.put("playerchar3", 1);
		alleg.put("enemychar1", 2);
		//alleg.put("enemychar2", 2);
		//alleg.put("enemychar3", 2);

    	this.world.init(characters, alleg);
	}

	void run_iteration(){
		int waittime = 0;
		int wtimewaited = this.world.get_time_waited();
		if( !this.input() ){
			while( waittime + wtimewaited < 1000 ){
				if( this.input() ) break;
				waittime+=10;
				try{ Thread.sleep(10); } catch( Exception e){};
			}
		}
		this.world.update();
		this.world.draw();
	}

	void pause(){

	}

	void end(){
		this.world.destroy();
	}

 	public boolean input() {
 		int cmd = Graphics.get_button_pressed();
 		if( cmd >= 1 && cmd <= 3 ){
 			Player p = this.world.get_player(1);
 			//Action a = p.abilities.get(cmd);
 			Character c = this.world.get_character( p.activecharacter );
 			if( !c.set_active_ability(cmd) ){
 				Graphics.draw_textln("You can't use that until the current action is finished!");
 				return false;
 			} else {
 				Graphics.disable_buttons();
 				Graphics.lockbuttons = true;
 				return true;
 			}
 		} else if( cmd == 4 ) {
 			Player p = this.world.get_player(1);
 			Character c = this.world.get_character( p.activecharacter );
 			c.toggle_ability(cmd);
 		}

 		return false;
    }
}