package main.tetris.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is one of of the core classes of my tetris clone.
 */
public class Tetris implements TetrisImpl {
	/**
	 * This is the current playfield containing all {@link Tetromino tetrominos} that has been placed.
	 * 
	 * <p>
	 * 
	 * This array has the dimension of <code>{@link #WIDTH} * {@link #HEIGHT}</code> 
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	protected final int[] board;
	
	/**
	 * This is the height of the {@link #board playfield}.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	protected final int HEIGHT;
	
	/**
	 * This is the width of the {@link #board playfield}.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	protected final int WIDTH;
	
	/**
	 * DAS is an abbreviation for <a href="http://tetris.wikia.com/wiki/DAS">Delayed Auto Shift</a>
	 * 
	 * <p>
	 * 
	 * This field controlls the amount of frames waited after a left
	 * or right move, after this amount has been reached the tetromino
	 * will start moving every {@link #ARR} frames.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a><br>
	 *      <a href="http://tetris.wikia.com/wiki/DAS">http://tetris.wikia.com/wiki/DAS</a>
	 */
	public int DAS = 5;
	
	/**
	 * ARR is an abbreviation for <i>Auto Repeat Rate</i>
	 * 
	 * <p>
	 * 
	 * After waiting for {@link #DAS} the tetromino will start moving
	 * every <i>ARR</i> frames.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public int ARR = 8;
	
	/**
	 * ARE is the <b><i>Entry delay</i></b> or <b><i>Spawn delay</i></b>
	 * of a piece.
	 * 
	 * <p>
	 * 
	 * When a tetromino has been placed on board you will get a new one
	 * and before you can play again the game will wait <i>ARE</i> frames.
	 * 
	 * <p>
	 * 
	 * ARE originates from the japanese word "あれ".
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a><br>
	 *      <a href="http://tetris.wikia.com/wiki/ARE">http://tetris.wikia.com/wiki/ARE</a>
	 */
	public int ARE = 30;
	
	/**
	 * This field is used in the formula to get the current
	 * {@link #getLevelSpeed speed} of a tetromino falling.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	protected int level = 0;
	
	/**
	 * This is the amount of lines that has been cleared from the
	 * {@link #board playfield}.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	protected int lines = 0;
	
	/**
	 * This is the current score of the game. The score is computed by
	 * a number of different factors.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a><br>
	 *      <a href="http://tetris.wikia.com/wiki/Scoring">http://tetris.wikia.com/wiki/Scoring</a>
	 */
	protected int score = 0;
	
	/**
	 * This is the current combo, the combo is determined by the amount
	 * of back to back rows you have cleard.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	protected int combo = 0;
	
	/**
	 * This is the current {@link Tetromino tetromino} being display
	 * on the {@link #board playfield}.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	protected Tetromino current;
	
	/**
	 * During play, you have the option to switch your active
	 * {@link Tetromino tetromino} to the last tetromino you swaped.
	 * 
	 * <p>
	 * 
	 * This action is called <a href="http://tetris.wikia.com/wiki/Hold_piece">holding</a>.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a><br>
	 *      <a href="http://tetris.wikia.com/wiki/Hold_piece">http://tetris.wikia.com/wiki/Hold_piece</a>
	 */
	protected Tetromino holding;
	
	/**
	 * This is the next {@link Tetromino tetromino} being sent to the screen.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	protected Tetromino next;
	
	/**
	 * This is the rotation for the {@link #current current} tetromino.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	protected int r;
	
	/**
	 * This is the x position for the {@link #current current} tetromino.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	protected int x;
	
	/**
	 * This is the y position for the {@link #current current} tetromino.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	protected int y;
	
	/**
	 * This initializes the <code>Tetris</code> class.
	 * This class does not include any code for movement and game
	 * mechanics, only {@link #board playfield} checks and settings.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public Tetris() {
		board = new int[400];
		HEIGHT = 40;
		WIDTH  = 10;
	}
	
	public double getLevelSpeed() {
		return Math.pow(0.8 - (level - 1) * 0.007, level - 1);
	}
	
	public void reset() {
		for(int i = 0; i < WIDTH * HEIGHT; i++)
			board[i] = 0;
		
		level = 0;
		lines = 0;
		score = 0;
		
		bag.clear();
		
		current = generateRandomTetromino();
		next    = generateRandomTetromino();
		holding = null;
		
		respawn();
	}
	
	public void respawn() {
		y = 18;
		
		if(current == Tetromino.O || current == Tetromino.I) x = 3;
		else x = 2;
		
		r =  0;
	}
	
	public boolean collisionMovement(int nx, int ny) {
		if(isLocationBlocked(nx, ny, r)) return false;
		x = nx;
		y = ny;
		
		return true;
	}
	
	public boolean collisionRotation(int nr) {
		if(current == Tetromino.O) return false;
		boolean flip = ((r + 1) & 3) != nr;
		
		int offset = nr * 10;
		
		if(!flip)
			offset = r * 10;
		
		if(current == Tetromino.I) {
			for(int i = 0; i < 5; i++) {
				int xn = WallKickData_1[offset + i * 2    ];
				int yn = WallKickData_1[offset + i * 2 + 1];
				
				if(!flip) {
					xn *= -1;
					yn *= -1;
				}
				
				if(!isLocationBlocked(x + xn, y + yn, nr)) {
					x += xn;
					y += yn;
					r  = nr;
					return true;
				}
			}
			
			return false;
		}
		
		for(int i = 0; i < 5; i++) {
			int xn = WallKickData_0[offset + i * 2    ];
			int yn = WallKickData_0[offset + i * 2 + 1];
			
			if(!flip) {
				xn *= -1;
				yn *= -1;
			}
			
			if(!isLocationBlocked(x + xn, y + yn, nr)) {
				x += xn;
				y += yn;
				r  = nr;
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isLocationBlocked(int x, int y, int r) {
		int tetromino = current.getRotated(r);
		
		for(int i = 0; i < 16; i++) {
			int xp = (i &  3) + x;
			int yp = (i >> 2) + y;
			int bit = (tetromino >> i) & 1;
			
			if(bit < 1)
				continue;
			
			if(yp < 0 || yp >= HEIGHT || xp < 0 || xp >= WIDTH
					  || board[xp + yp * WIDTH] > 0)
				return true;
		}
		
		return false;
	}
	
	public int clearRows() {
		int offset = 0;
		for(int i = 0; i < HEIGHT - 1; i++) {
			
			boolean full_row = true;
			for(int j = 0; j < WIDTH; j++) {
				if(board[j + (i + offset) * WIDTH] > 0) continue;
				
				full_row = false;
				break;
			}
			
			if(i + offset + 1 >= HEIGHT) break;
			
			for(int v = 0; v < WIDTH; v++) {
				board[v + i * WIDTH] = board[v + (i + offset) * WIDTH];
			}
			
			if(full_row) {
				offset ++;
				i --;
			}
		}
		
		lines += offset;
		
		return offset;
	}
	
	private LinkedList<Tetromino> bag = new LinkedList<Tetromino>();
	public Tetromino generateRandomTetromino() {
		if(bag.size() == 0) {
			List<Integer> ints = Arrays.asList(0, 1, 2, 3, 4, 5, 6);
			Collections.shuffle(ints);
			
			for(int i : ints) bag.add(Tetromino.get(i));
		}
		
		return bag.poll();
	}
	
	public int getWidth() {
		return WIDTH;
	}
	
	public int getHeight() {
		return HEIGHT;
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getLines() {
		return lines;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getCombo() {
		return combo;
	}
	
	public int getRotation() {
		return r;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Tetromino getCurrentTetromino() {
		return current;
	}
	
	public Tetromino getHoldTetromino() {
		return holding;
	}
	
	public Tetromino getNextTetromino() {
		return next;
	}
	
	public int[] getPlayfield() {
		return Arrays.copyOf(board, WIDTH * HEIGHT);
	}
}
