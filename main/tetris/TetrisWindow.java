package main.tetris;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class TetrisWindow extends Canvas {
	private static final long serialVersionUID = 1L;
	public static final int HEIGHT = 550;
	public static final int WIDTH = 550;
	private final int fps;
	
	private Thread render_thread;
	
	public TetrisRender render;
	public JFrame frame;
	
	public TetrisWindow(int fps) {
		this.fps = fps;
		render = new TetrisRender();
		
		frame = new JFrame("Tetris - AI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH + 6, HEIGHT + 29);
		frame.setResizable(false);

		Input input = new Input();
		frame.addFocusListener(input);
		frame.addKeyListener(input);
		
		render.setBounds(0, 0, WIDTH, HEIGHT);
		frame.add(this);
	}
	
	public void initialize() {
		frame.setVisible(true);
		
		Start();
	}
	
	public void Start() {
		if(render_thread != null) return;
		
		render_thread = new Thread(new Runnable() {
			public void run() {
				double tick = 1000.0 / fps;
				
				int frames = 0;
				double old = System.currentTimeMillis();
				while(true) {
					long now = System.currentTimeMillis();
					
					if(now > old) {
						old += tick;
						
						if(((++frames) % fps) == 0) {
							//System.out.println("FPS: " + frames);
							
							frames = 0;
						}
						
						render();
					} else {
						try {
							Thread.sleep(1);
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		
		render_thread.start();
	}
	
	public BufferStrategy bs;
	public void render() {
		if(bs == null) return;
		Graphics2D g = (Graphics2D)bs.getDrawGraphics();
		frame.requestFocus();
		render.render(g);
		
		bs.show();
		
		
		boolean left = Input.keys[KeyEvent.VK_LEFT];
		boolean right = Input.keys[KeyEvent.VK_RIGHT];
		boolean hold = Input.keys[KeyEvent.VK_W];
		boolean rotateleft = Input.keys[KeyEvent.VK_D];
		boolean rotateright = Input.keys[KeyEvent.VK_A];
		boolean rotateflip = Input.keys[KeyEvent.VK_S];
		boolean softdrop = Input.keys[KeyEvent.VK_DOWN];
		boolean harddrop = Input.keys[KeyEvent.VK_SPACE];
		
		render.tetris.tick(
			left,
			right,
			hold,
			rotateleft,
			rotateright,
			rotateflip,
			softdrop,
			harddrop
		);
	}
	
	public void paint(Graphics g) {
		if(bs == null) {
			createBufferStrategy(2);
			bs = getBufferStrategy();
			return;
		}
	}
}
