package main.tetris;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import main.tetris.core.Tetromino;
import main.tetris.core.TetrisSimulator;

public class TetrisRender extends Canvas {
	private static final long serialVersionUID = 1L;
	public static final Color[] PIECE_COLOR = {
		Color.cyan,
		Color.orange,
		getBrighter(Color.blue, 1.1f),
		Color.yellow,
		Color.red,
		getBrighter(Color.magenta, 0.9f),
		Color.green,
	};
	
	public static final int MID = 3;
	
	public TetrisSimulator tetris;
	public TetrisRender() {
		tetris = new TetrisSimulator();
	}
	
	public TetrisRender(TetrisSimulator tetris) {
		this.tetris = tetris;
	}
	
	public void render(Graphics2D g) {
		int wi = getWidth();
		int he = getHeight();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, wi, he);
		int tw = tetris.getWidth() * 24;
		int offset = (wi / 2) - (tw / 2);
		
		g.translate(offset, 0);
		
		drawBoard(g);
		drawInformation(g);
		
		drawTetromino(g,
			tetris.getCurrentTetromino(),
			tetris.getX(), tetris.getY(), tetris.getRotation()
		);
		
		drawGhostpiece(g);
		
		//System.out.println(this.getBounds());
		//System.out.println(this.getParent());
		
		// debugTetrominos(g);
		
		g.translate(-offset, 0);
	}
	
	public void drawInformation(Graphics2D g) {
		Color[] colors = {
			getBrighter(Color.white, 0.30f),
			getBrighter(Color.white, 0.36f)
		};
		
		g.setColor(Color.white);
		g.setFont(new Font("Consolas", Font.PLAIN, 19));
		g.drawString("Score", -150, 15);
		g.drawString("" + tetris.getScore(), -55, 15);
		
		g.drawString("Level", -150, 40);
		g.drawString("" + tetris.getLevel(),  -55, 40);
		g.drawString("" + tetris.getFrame(), -145, 65);
		g.drawString("Lines", 270, 15);
		g.drawString("" + tetris.getLines(), 370, 15);
		g.drawString("" + tetris.getLevelProgress(), 370, 40);
		g.drawString("" + tetris.getLockingIndex(), 370, 65);
		/** Draw Next Piece */ {
			g.setColor(Color.white);
			g.drawString("Next", -92, 110);
			g.drawString("Hold", -92, 254);
			
			g.setColor(colors[1]);
			g.fillRect(-124, 116, 104, 104);
			g.fillRect(-124, 260, 104, 104);
			
			// Next
			for(int y = 0; y < 4; y++) {
				for(int x = 0; x < 4; x++) {
					int idx = (x + y) & 1;
					g.setColor(colors[idx].brighter().brighter());
					g.fillRect((x - 5) * 24, (y + 5) * 24, 24, 24);
				}
			}
			drawTetromino(g, tetris.getNextTetromino(), -5, 11, 0);
			
			// Hold
			for(int y = 0; y < 4; y++) {
				for(int x = 0; x < 4; x++) {
					int idx = (x + y) & 1;
					g.setColor(colors[idx].brighter().brighter());
					g.fillRect((x - 5) * 24, (y + 11) * 24, 24, 24);
				}
			}
			drawTetromino(g, tetris.getHoldTetromino(), -5, 5, 0);
		}
	}
	public void drawGhostpiece(Graphics2D g) {
		boolean found = false;
		
		int a = tetris.getY();
		for(; a > -2; a--) {
			if(tetris.isLocationBlocked(tetris.getX(), a - 1, tetris.getRotation())) {
				found = true;
				break;
			}
		}
		
		if(found || a < 0) {
			drawTetromino(g,
				tetris.getCurrentTetromino(),
				tetris.getX(),
				a,
				tetris.getRotation(),
				new Color(1, 1, 1, 0.3f)
			);
		}
	}
	public void drawBoard(Graphics2D g) {
		Color[] colors = {
			getBrighter(Color.white, 0.30f),
			getBrighter(Color.white, 0.36f)
		};
		
		for(int y = 0; y < 20; y++) {
			for(int x = 0; x < 10; x++) {
				int idx = (x + y) & 1;
				g.setColor(colors[idx]);
				g.fillRect(x * 24, y * 24, 24, 24);
			}
		}
		
		int[] board = tetris.getPlayfield();
		for(int y = 0; y < 20; y++) {
			for(int x = 0; x < 10; x++) {
				int t = board[x + y * 10];
				if(t > 0) {
					drawBoxL(g, PIECE_COLOR[t - 1], x, 19 - y);
				}
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void debugTetrominos(Graphics2D g) {
		for(Tetromino t : Tetromino.values()) {
			for(int rota = 0; rota < 4; rota++) {
				drawTetromino(g, t, rota * 5, 16 - t.id * 5, rota);
			}
		}
	}
	
	public static void drawTetromino(Graphics2D g, int i, int x, int y, int r) {
		Tetromino t = Tetromino.get(i);
		if(t == null) return;
		
		drawTetromino(g, t, x, y, r);
	}
	
	public static void drawTetromino(Graphics2D g, Tetromino t, int x, int y, int r, Color... col) {
		if(t == null) return;
		
		int num = t.getRotated(r);
		Color c = col.length < 1 ? PIECE_COLOR[t.id]:col[0];
		
		for(int i = 0; i < 16; i++) {
			int xp = (i &  3) + x;
			int yp = (i >> 2) + y;
			int bit = (num >> i) & 1;
			
			if(bit < 1)
				continue;
			
			drawBox(g, c, xp * 24, (19 - yp) * 24);
		}
	}
	
	public static void drawBoxL(Graphics2D g, Color col, int x, int y) {
		drawBox(g, col, x * 24, y * 24);
	}
	public static void drawBox(Graphics2D g, Color col, int x, int y) {
		g.setColor(getBrighter(col, 1.5f));
		g.fillRect(x, y     , 24, 12);
		
		g.setColor(getBrighter(col, 0.4f));
		g.fillRect(x, y + 12, 24, 12);
		
		int[] yp = { y     , y + 24, y     , y + 24 };
		int[] xp = { x     , x + 24, x + 24, x      };
		
		g.setColor(getBrighter(col, 0.7f));
		g.fillPolygon(xp, yp, 4);
		
		g.setColor(col);
		g.fillRect(x + MID, y + MID, 24 - MID * 2, 24 - MID * 2);
		
	}
	
	public static Color getBrighter(Color col, float mul, boolean... alpha) {
		if(mul == 1) return col;
		
		float c_r = col.getRed();
		float c_g = col.getGreen();
		float c_b = col.getBlue();
		float c_a = col.getAlpha();
		
		if(mul < 1) {
			float i_m = 1 - mul;
			
			c_r -= c_r * i_m;
			c_g -= c_g * i_m;
			c_b -= c_b * i_m;
			c_a -= c_a * i_m;
		} else {
			float i_m = mul - 1;
			
			c_r += (255 - c_r) * i_m;
			c_g += (255 - c_g) * i_m;
			c_b += (255 - c_b) * i_m;
			c_a += (255 - c_a) * i_m;
		}
		
		c_r /= 255.0f;
		c_g /= 255.0f;
		c_b /= 255.0f;
		c_a /= 255.0f;
		
		c_r = c_r > 1 ? 1:(c_r < 0 ? 0:c_r);
		c_g = c_g > 1 ? 1:(c_g < 0 ? 0:c_g);
		c_b = c_b > 1 ? 1:(c_b < 0 ? 0:c_b);
		c_a = c_a > 1 ? 1:(c_a < 0 ? 0:c_a);
		
		return new Color(c_r, c_g, c_b, alpha.length > 0 ? c_a:(col.getAlpha() / 255.f));
	}
}
