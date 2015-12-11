import java.io.*;
import java.util.*;
import javax.swing.*;

public class TutorialState extends State{

	int wantstutorial = 0;
	JOptionPane pane = null;
	JDialog dialog = null;

	WorldInterface world = null;

	public TutorialState(GameInterface g){
		super(g);
		this.init();
	}

	void init(){
		pane = new JOptionPane(new JLabel("",JLabel.CENTER)); 
		dialog = pane.createDialog(""); 
		dialog.setModal(true);  
		dialog.setBounds(696, 296, 390, 230);	
	}

	void create_world(){
		String[] names = {"playerchar1", "enemychar1", "playerchar2", "playerchar3", "enemychar2", "enemychar3"};
		this.world = new World();

		ArrayList<Character> characters = new ArrayList<Character>();
		ArrayList<Integer> defaultactions = new ArrayList<Integer>();
		defaultactions.add(1);
		defaultactions.add(2);
		defaultactions.add(3);
		defaultactions.add(5);

		ArrayList<Integer> defaultactions2 = new ArrayList<Integer>();
		defaultactions2.add(4);

		characters.add( new Character(names[0], defaultactions, this.world) );	
		characters.add( new Character(names[1], defaultactions2, this.world) );	

		HashMap<String,Integer> alleg = new HashMap<String,Integer>();
		alleg.put("playerchar1", 1);
		alleg.put("enemychar1", 2);

    	this.world.init(characters, alleg);	
	}

	void run_iteration(){
		int result = 0;
		if( wantstutorial == 0 ){
			Object[] options = { "YUP", "NOPE" };
			result = JOptionPane.showOptionDialog(null, "Do you want to see the tutorial?", "A simple question.", 
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, options, options[1]);
		}

		if( result == 0 ){
			System.out.println("Initiate tutorial.");
			wantstutorial = 1;

			String s1 = null;

			s1 =""+
				"                   Welcome to 'Battle!' by Benjamin Brown.\n"+
				"In this game you and your enemy have a team of characters,\n"+
				"and your goal is to eradicate the oposing team.\n"+
				"(Press OK to continue)"; 
			pane.setMessage(s1);	
			dialog.setVisible(true); 

			this.create_world();
			this.world.update();
			this.world.draw();

			s1 =""+
				"In the console window, a game has been started.  You should\n"+
				" see some messages indicating that a battle has begun, and\n"+
				" you should see an overview of the characters on the teams of\n"+
				" you and your enemy.\n"+
				"(Press OK to continue)"; 
			pane.setMessage(s1);	
			dialog.setVisible(true);

			s1 =""+
				"In this example, the enemy team has one member and your team\n"+ 
				" has one member.  Each of you has 50 hit points. To eradicate\n"+
				" a character, you must reduce that character's hit point value\n"+
				" to zero. You have a couple of ways to do this.\n"+
				"(Press OK to continue)"; 
			pane.setMessage(s1);	
			dialog.setVisible(true); 

			s1 =""+
				"Somewhere on your screen you should see an input window.\n"+ 
				" This window has four buttons labeled Attack, Defend, Magic,\n"+
				" and Heal. These four are the actions that you can take.\n"+
				"(Press OK to continue)"; 
			pane.setMessage(s1);	
			dialog.setVisible(true); 

			s1 =""+
				"Below each button is a number. This number indicates the\n"+
				" time until the above action is ready to be used. You \n"+
				" cannot click the button to perform the action until the \n"+
				" cooldown timer is at '0'.\n"+
				"(Press OK to continue)"; 
			pane.setMessage(s1);	
			dialog.setVisible(true); 

			for( int i = 0; i < 7; ++i ){
				try{ Thread.sleep(1000); } catch( Exception e){};
				this.world.update();
				this.world.draw();
			}

			s1 =""+
				"Now that the timer has ticked to '0', the action is ready \n"+
				" to be used. The 'Attack' button is ready, so let's use\n"+
				" that one.\n"+
				"(Press OK to continue)"; 
			pane.setMessage(s1);	
			dialog.setVisible(true); 

			Character c = this.world.get_character( "playerchar1" );
 			c.set_active_ability(1);
 			Graphics.disable_buttons();
 			Graphics.lockbuttons = true;

			for( int i = 0; i < 2; ++i ){
				try{ Thread.sleep(1000); } catch( Exception e){};
				this.world.update();
				this.world.draw();
			} 			

			s1 =""+
				"The console window now indicates that your character attacked\n"+
				" 'enemychar1'. The status of both teams has also been redrawn\n"+
				" and it shows that 'enemychar1' lost a fair amount of hp. \n"+
				" In the mean time it also looks like the 'Defend' button is\n"+
				" ready, so let's use that one now.\n"+
				"(Press OK to continue)"; 
			pane.setMessage(s1);	
			dialog.setVisible(true); 

 			c.set_active_ability(2);
 			Graphics.disable_buttons();
 			Graphics.lockbuttons = true;

			for( int i = 0; i < 2; ++i ){
				try{ Thread.sleep(1000); } catch( Exception e){};
				this.world.update();
				this.world.draw();
			} 

			s1 =""+
				"The console window now indicates that your character is defending.\n"+
				" 'Defend' is an active ability. This means your character will be\n"+
				" defending for a few seconds. While a character is defending, that \n"+
				" character will block the next incoming 'Attack' from another\n"+
				" character.  Notice that the cooldown indicator changes to 'A' to\n"+
				" indicate that the ability is active.\n"+
				"(Press OK to continue)"; 
			pane.setMessage(s1);	
			dialog.setVisible(true); 

			for( int i = 0; i < 3; ++i ){
				try{ Thread.sleep(1000); } catch( Exception e){};
				this.world.update();
				this.world.draw();
			} 

			s1 =""+
				"Your character has successfully defended an attack from enemychar1\n"+
				" and, as a bonus, enemychar1 took some damage.  If the enemy did\n"+
				" not attack, after a while your character would leave the defensive\n"+
				" stance, and the 'Defend' ability would go on a large cooldown. \n"+
				" Be careful how you time your uses of 'Defend', it is vital to\n"+
				" victory, but misuse can be disasterous.\n"+
				"(Press OK to continue)"; 
			pane.setMessage(s1);	
			dialog.setVisible(true); 

			s1 =""+
				"The action 'Magic' is available now, so let's use it. 'Magic' is\n"+
				" also an active ability like 'Defend' except it works in an\n"+
				" inverse way.  Your character must 'channel' for a few seconds\n"+
				" before unleashing the ability.  Here the spell is 'fireball'.\n"+
				"(Press OK to continue)"; 
			pane.setMessage(s1);	
			dialog.setVisible(true); 

 			c.set_active_ability(3);
 			Graphics.disable_buttons();
 			Graphics.lockbuttons = true;

			for( int i = 0; i < 7; ++i ){
				try{ Thread.sleep(1000); } catch( Exception e){};
				this.world.update();
				this.world.draw();
			} 

			s1 =""+
				"WOW! That was a lot of damage! Be careful though when using the\n"+ 
				" 'Magic' command. While channeling you can be interrupted by an\n"+
				" attack from an ememy character and the spell will go on a large \n"+
				" cooldown.\n"+
				"(Press OK to continue)"; 
			pane.setMessage(s1);	
			dialog.setVisible(true); 

			s1 =""+
				"Your final ability is a the 'Heal' ability.  This one is unlike\n"+ 
				" the previous ones because it is a toggle ability: it can either \n"+
				" be on or off. When heal is on, your character will recover hp \n"+
				" every few seconds, however, any damage that your character \n"+
				" deals will be reduced by half.\n"+
				"(Press OK to continue)"; 
			pane.setMessage(s1);	
			dialog.setVisible(true); 

			s1 =""+
				"This concludes the tutorial.  Once you click okay, you will be\n"+
				" thrown into the game, so be ready!";
			pane.setMessage(s1);	
			dialog.setVisible(true); 

			this.game.change_state( 1 );
		} else {
			System.out.println("You don't want the tutorial.");
			this.game.change_state( 1 );
		}
	}

 	public boolean input() {
 		return true;
    }

	void end(){

	}
}