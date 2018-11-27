package main.tetris;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener, FocusListener {
	public static  boolean[] keys = new boolean[65535];
	private static boolean[] last = new boolean[65535];
	
	public Input() {
		
	}
	
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(!keys[code]) last[code] = true;
		keys[code] = true;
	}

	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		last[code] = false;
		keys[code] = false;
	}
	
	public static boolean IsKeyDown(int id) {
		boolean press = last[id];
		last[id] = false;
		return press;
	}
	
	public void focusLost(FocusEvent e) {
		for(int i = 0; i < 65535; i++) {
			if(keys[i]) keys[i] = false;
		}
	}

	public void focusGained(FocusEvent e) {
		
	}
	public void keyTyped(KeyEvent e) {}
}
