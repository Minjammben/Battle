import java.io.*;
import java.nio.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Graphics {

    public static final String WINDOWTITLE = "Power Fighter";

    public static TextField textfield = null;

    public static boolean lockbuttons = false;

    public Graphics() {
		System.out.println( "You made a Graphics." );
    }

    public static void init(){
        Graphics.textfield = new TextField();
        Graphics.textfield.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static int typewrite_textln(String s){
    	int timewaited = 0;
		for (int i = 0; i < s.length(); i++){
			char c = s.charAt(i);        
			System.out.print(c);
			try{ Thread.sleep(60); } catch( Exception e){};
			timewaited += 60;
		}   
		System.out.println("");	
		return timewaited;
    }

    public static void draw_textln(String text){
    	System.out.println(text);
    }

    public static void draw_text(String text){
    	System.out.print(text);
    }

    public static String get_input_text(){
        if( Graphics.textfield.ready ){
            String ret = Graphics.textfield.text;
            Graphics.textfield.ready = false;
            Graphics.textfield.text = "none";
            return ret;
        } else {
            return "none";
        }
    }

    public static int get_button_pressed(){
        if( Graphics.textfield.ready ){
            int ret = Graphics.textfield.buttonpressed;
            Graphics.textfield.ready = false;
            Graphics.textfield.buttonpressed = 0;
            return ret;
        } else {
            return 0;
        }    	
    }

    public static void set_cd_ui(int i, int val){
    	JTextField f = null;
    	JButton b = null;
    	switch( i ){
    		case 1: f = textfield.t1; b = textfield.b1; break;
    		case 2: f = textfield.t2; b = textfield.b2; break;
    		case 3: f = textfield.t3; b = textfield.b3; break;
    		case 4: f = textfield.t4; b = textfield.b4; break;
    		default: f = textfield.t1; b = textfield.b1; break;
    	}

    	if( val == -1 ){
    		f.setText("A");
    	} else if( val == -2 ) {
            f.setText("On");
        } else if( val == -3 ){
            f.setText("Off");
        } else {
    		f.setText(""+val);
    	}

    	if( (val == 0 || val == -2 ||val == -3) && !Graphics.lockbuttons ){
    		b.setEnabled(true);
    	} else {
    		b.setEnabled(false);
    	}
    }

    public static void disable_buttons(){
    	// JButton b = null;
    	// switch( i ){
    	// 	case 1: b = textfield.b1; break;
    	// 	case 2: b = textfield.b2; break;
    	// 	case 3: b = textfield.b3; break;
    	// 	case 4: b = textfield.b4; break;
    	// 	default: b = textfield.b1; break;
    	// }

        textfield.b1.setEnabled(false); 
        textfield.b2.setEnabled(false); 
        textfield.b3.setEnabled(false);
        textfield.b4.setEnabled(false); 

    	//b.setEnabled(false);
    }

    public static void set_description_ui(int i, String val){
    	JTextField f = null;
    	switch( i ){
    		case 1: f = textfield.d1; break;
    		case 2: f = textfield.d2; break;
    		case 3: f = textfield.d3; break;
    		case 4: f = textfield.d4; break;
    		default: f = textfield.d1; break;
    	}

    	f.setText(""+val);
    }

    public static void destroy(){
    	Graphics.textfield.close();
    }
}

class TextField extends JFrame {

    public boolean ready = false;
    public int buttonpressed = 1;
    public String text = "none";

    //Class Declarations
    public JTextField jtfText1, d1, d2, d3, d4, t1, t2, t3, t4;
    public JButton b1, b2, b3, b4;
    String disp = "";
    TextHandler handler = null;
    ButtonListener blistener = null;
    //Constructor
    public TextField() {
        super("Input Commands Here.");
        Container container = getContentPane();
        container.setLayout(new FlowLayout());

        d1 = new JTextField("Attack", 3);
        d1.setEditable(false);

        d2 = new JTextField("Defend", 3);
        d2.setEditable(false);

        d3 = new JTextField("Magic", 3);
        d3.setEditable(false);

        d4 = new JTextField("Heal", 3);
        d4.setEditable(false);

	 	b1 = new JButton("A");
	    b1.setVerticalTextPosition(AbstractButton.CENTER);
	    b1.setHorizontalTextPosition(AbstractButton.CENTER);
	    b1.setMnemonic(KeyEvent.VK_A);

	 	b2 = new JButton("B");
	    b2.setVerticalTextPosition(AbstractButton.CENTER);
	    b2.setHorizontalTextPosition(AbstractButton.CENTER);
	    b2.setMnemonic(KeyEvent.VK_B);

	 	b3 = new JButton("X");
	    b3.setVerticalTextPosition(AbstractButton.CENTER);
	    b3.setHorizontalTextPosition(AbstractButton.CENTER);
	    b3.setMnemonic(KeyEvent.VK_X);

	 	b4 = new JButton("Y");
	    b4.setVerticalTextPosition(AbstractButton.CENTER);
	    b4.setHorizontalTextPosition(AbstractButton.CENTER);
	    b4.setMnemonic(KeyEvent.VK_Y);

        t1 = new JTextField("10", 3);
        t1.setEditable(false);

        t2 = new JTextField("10", 3);
        t2.setEditable(false);

        t3 = new JTextField("10", 3);
        t3.setEditable(false);

        t4 = new JTextField("10", 3);
        t4.setEditable(false);

        jtfText1 = new JTextField(10);
        // jtfUneditableText = new JTextField("", 20);
        // jtfUneditableText.setEditable(false);
        //container.add(jtfText1);
        container.add(d1);
        container.add(d2);
        container.add(d3);
        container.add(d4);
        container.add(b1);
        container.add(b2);
        container.add(b3);
        container.add(b4);
        container.add(t1);
        container.add(t2);
        container.add(t3);
        container.add(t4);
        //container.add(jtfUneditableText);
        handler = new TextHandler();
        blistener = new ButtonListener();
        b1.addActionListener(blistener);
        b2.addActionListener(blistener);
        b3.addActionListener(blistener);
        b4.addActionListener(blistener);
        jtfText1.addActionListener(handler);
        //jtfUneditableText.addActionListener(handler);
        //setSize(325, 100);
        setBounds(500,500,220,140);
        setVisible(true);
    }

    public void close(){
		setVisible(false); 
		dispose();
    }

    private class TextHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if( e.getSource() == jtfText1 ){
                disp = e.getActionCommand();
            }

            text = disp;
            ready = true;
        }
    }

    private class ButtonListener implements ActionListener {
         public void actionPerformed(ActionEvent e) {
            if( e.getSource() == b1 ){
            	b1.setEnabled(false);
                buttonpressed = 1;
            } else if( e.getSource() == b2 ){
            	b2.setEnabled(false);
            	buttonpressed = 2;
            } else if( e.getSource() == b3 ){
            	b3.setEnabled(false);
            	buttonpressed = 3;
            } else if( e.getSource() == b4 ){ 
            	b4.setEnabled(false);
            	buttonpressed = 4;
            }

            Graphics.disable_buttons();

            ready = true;
        }   	
    }
}