import java.io.*;

public abstract class State{

    public boolean quitflag = false;

	GameInterface game;

	public State(GameInterface g){
		this.game = g;
		//System.out.println("You made a State.");
	}

	abstract void init();
	abstract void run_iteration();
	abstract boolean input();
	abstract void end();
}