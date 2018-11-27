package main.tetris.core;

import java.util.Random;

public interface TetrisImpl {
	static final Random RANDOM = new Random();
	static final double UNIT = 1 / 60.0;
	
	/**
	 * This is the wall kick table for the <code>J</code>, <code>L</code>, <code>O</code>, <code>S</code>, <code>T</code> and <code>Z</code> tetromino.<br>
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	static final int[] WallKickData_0 = {
		0, 0, -1, 0, -1,  1, 0, -2, -1, -2,
		0, 0,  1, 0,  1, -1, 0,  2,  1,  2,
		0, 0,  1, 0,  1,  1, 0, -2,  1, -2,
		0, 0, -1, 0, -1, -1, 0,  2, -1,  2,
	};
	
	/**
	 * This is the wall kick table for the <code>I</code> tetromino.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	static final int[] WallKickData_1 = {
		0, 0, -2, 0,  1, 0, -2, -1,  1,  2,
		0, 0, -1, 0,  2, 0, -1,  2,  2, -1,
		0, 0,  2, 0, -1, 0,  2,  1, -1, -2,
		0, 0, -1, 0,  2, 0, -1,  2,  2,  1,
	};
	
	/**
	 * The method for calculating speed uses the following formula.
	 * 
	 * <p>
	 * 
	 * <code>(0.8 - (Level - 1) * 0.007) ^ (Level - 1)</code>
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a><br>
	 *      <a href="http://tetris.wikia.com/wiki/Tetris_Worlds#Gravity">http://tetris.wikia.com/wiki/Tetris_Worlds</a>
	 */
	public double getLevelSpeed();
	
	/**
	 * This method will reset all variables dealing with the {@link Tetris#board playfield}. 
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public void reset();
	
	/**
	 * This method will place the {@link Tetris#current current} tetromino on screen acording to the
	 * spawning rules.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a><br>
	 *      <a href="http://tetris.wikia.com/wiki/Tetris_Guideline">http://tetris.wikia.com/wiki/Tetris_Guideline<a>
	 */
	public void respawn();
	
	/**
	 * This method checks if the {@link Tetris#current current} tetromino can exist
	 * at the position <code>(nx, ny)</code> and if possible it will set the current
	 * x and y value to that new position.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	boolean collisionMovement(int nx, int ny);
	
	/**
	 * This method performs the checks called <a href="http://tetris.wikia.com/wiki/SRS#Wall_Kicks">wall kicking</a>,
	 * this uses a premade table made of different offsets and checks if any one of
	 * those would allowed, and if so the final rotation and position will be set.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a><br>
	 *      <a href="http://tetris.wikia.com/wiki/SRS#Wall_Kicks">http://tetris.wikia.com/wiki/SRS</a>
	 */
	boolean collisionRotation(int nr);
	
	/**
	 * This method checks if the {@link Tetris#current current} tetromino
	 * can exist at the position <code>(x, y)</code> with a rotation of <code>r</code>
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	boolean isLocationBlocked(int x, int y, int r);
	
	/**
	 * This method will clear all full rows on the {@link Tetris#board playfield}.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	int clearRows();
	
	/**
	 * This method uses generates a random {@link Tetromino tetromino}.
	 * 
	 * <p>
	 * 
	 * It uses {@link java.util.Random} to generate a number between 0 and 6 (inclusive).
	 * After that it checks whether or not that id was the last one, if so it generates a
	 * new number. This will make the piece distrubution more even at times.
	 * 
	 * <p>
	 * 
	 * The chance of getting another piece than the {@link Tetris#current current} one is 8/49,
	 * and the chance of gettnig the same is 1/49. 
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a><br>
	 *      <a href="http://tetris.wikia.com/wiki/Random_Generator">http://tetris.wikia.com/wiki/Random_Generator</a>
	 */
	public Tetromino generateRandomTetromino();
	
	/**
	 * This method will return a copy of the current {@link Tetris#board playfield}.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public int[] getPlayfield();
	
	/**
	 * This method will return the {@link Tetris#WIDTH width} of the {@link Tetris#board playfield}.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public int getWidth();
	
	/**
	 * This method will return the {@link Tetris#HEIGHT height} of the {@link Tetris#board playfield}.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public int getHeight();
	
	/**
	 * This method will return the current {@link Tetris#level level}.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public int getLevel();
	
	/**
	 * This method will return the current amount of {@link Tetris#lines lines} cleared from
	 * the {@link Tetris#board playfield}.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public int getLines();
	
	/**
	 * This method will return the current score of the game.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public int getScore();
	
	/**
	 * This will return the amount of consecutive {@link Tetris#lines} cleared.
	 * The {@link Tetris#combo} will reset to zero if no lineclear has been made
	 * within 1 second .
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public int getCombo();
	
	/**
	 * This method will return the current {@link Tetris#r rotation} of the current tetromino.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public int getRotation();
	
	/**
	 * This method will return the current {@link Tetris#x x} position of our current tetromino.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public int getX();
	
	/**
	 * This method will return the current {@link Tetris#y y} position of our current tetromino.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public int getY();
	
	/**
	 * This method will return the {@link Tetris#current current} tetromino.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public Tetromino getCurrentTetromino();
	
	/**
	 * This method will return the current {@link Tetris#holding held} tetromino.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public Tetromino getHoldTetromino();
	
	/**
	 * This method will return the {@link Tetris#next next} tetromino.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public Tetromino getNextTetromino();
}
