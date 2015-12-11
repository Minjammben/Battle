import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.nio.*;
import java.util.*;

public class Main {

    public static void main (String[] args) {
		System.out.println( "Begin Program." );

		Graphics.init();
		GameInterface g = new Game();
		g.start();
		Graphics.destroy();
    }
}