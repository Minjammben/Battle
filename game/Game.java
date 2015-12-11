import java.io.*;

public class Game implements GameInterface{

    State state;
    boolean isrunning = true;
    int nextstateid = -1;

    public Game() {
		//System.out.println( "You made a Game." );
    }

    public void start(){
        this.set_state(0);
        this.main_loop();
    }

	public void change_state(int stateid){
		this.nextstateid = stateid;
	}


    public void set_state(int stateid){
    	switch( stateid ){
    		case 0: this.state = new TutorialState( (GameInterface)this ); break;
    		case 1: this.state = new WorldState( (GameInterface)this ); break;
    	}
    }

    public void main_loop(){
        while( this.isrunning ){
            this.state.run_iteration();
            if( this.nextstateid != -1 ){
            	this.set_state(this.nextstateid);
            	this.nextstateid = -1;
            }

            if( this.state.quitflag == true ){ 
                this.isrunning = false; 
            } 
        }
        this.end();
    }

    public void end(){
    	System.out.println("Exit program.");
        this.state.end();
    }
    
}