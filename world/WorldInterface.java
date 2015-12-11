import java.util.*;

interface WorldInterface {
	
	public Character get_character(String name);
	public Character get_current_target(int alleg);
	public Player get_player(int i);
	public int get_time_waited();
	public void add_graphic(String s);
	public void kill_character(String name);
	public void init(ArrayList<Character> characters, HashMap<String,Integer> alleg);
	public void update();
	public void draw();
	public void destroy();

}