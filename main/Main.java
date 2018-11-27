package main;

import main.tetris.TetrisWindow;

public class Main {
	public static final boolean DEBUG = true;
	
	public static void main(String[] args) throws Exception {
		new Main();
	}
	
	public TetrisWindow window;
	public Main() {
		window = new TetrisWindow(60);
		
		window.initialize();
	}
}
