package main.tetris.core;

public interface TetrisSimulatorImpl {
	/**
	 * Every time this method gets called it increments the current {@link TetrisSimulator#frame frame}.
	 * 
	 * <p>
	 * 
	 * This method updates the game with the specified parameters and updates the game accordingly. 
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public void tick(boolean left,
					 boolean right,
					 boolean hold,
					 boolean rotateleft,
					 boolean rotateright,
					 boolean rotateflip,
					 boolean softdrop,
					 boolean harddrop);
	
	/**
	 * This method updates the postition of the {@link Tetris#current current} tetromino.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public void tickMovement(boolean left, boolean right);
	
	/**
	 * This method updates the rotation of the {@link Tetris#current current} tetromino.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public void tickRotation(boolean rotateleft, boolean rotateright, @Deprecated boolean rotateflip);
	
	/**
	 * This method updates the {@link Tetris#y} position depending on the {@link Tetris#level} speed.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public void tickGravity(boolean softdrop, boolean harddrop);
	
	/**
	 * This method will write the {@link Tetris#current current} tetromino to the
	 * {@link Tetris#board playfield}.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	void writeToArray();
	
	/**
	 * This method wall add to the score depending on the movement, softdrop,
	 * harddrop or rotation of the {@link Tetris#current current} tetromino
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	void calculateScore();
	
	/**
	 * This method will return the current {@link TetrisSimulator#frame frame}.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public long getFrame();
	
	/**
	 * This method will return a value from 0 to 30 meaning the amount of {@link TetrisSimulator#frame frames}
	 * that has gone since the {@link Tetris#current current} tetromino touched the ground.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public long getLockingIndex();
	
	/**
	 * This will return the current amount of {@link Tetris#lines lines}.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	public int getLevelProgress();
}
