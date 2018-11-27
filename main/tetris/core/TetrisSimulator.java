package main.tetris.core;

/**
 * This is the movement implementation of the {@link Tetris tetris} class.
 */
public class TetrisSimulator extends Tetris implements TetrisSimulatorImpl {
	/**
	 * This is the current tick frame.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	protected long frame = 0;
	
	/**
	 * This is the last tick the tetromino rotated.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	protected long lastRotationInput = 0;
	
	/**
	 * This is the last tick the tetromino moved its position key was held down.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	protected long lastMovementInput = 0;
	
	/**
	 * This is the last tick the softdrop key was held down.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	protected long lastSoftdropInput = 0;
	
	/**
	 * This is the frame the {@link Tetris#current current} tetromino spawned on screen.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	protected long lastSpawnFrame = 0;
	
	/**
	 * This is the frame the {@link Tetris#current current} tetromino started touching the ground.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	protected long lastLockFrame = 0;
	
	/**
	 * This is the frame the {@link Tetris#current current} tetromino fell.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	protected long lastDropFrame = 0;
	
	/**
	 * This is the current proggress of leveling up.
	 * 
	 * <p>
	 * 
	 * I'm using the variable leveling system mening that you level
	 * up after <code>5 * {@link Tetris#level level}</code> lines.
	 * 
	 * @see <a href="https://tetris.com/">https://tetris.com/</a>
	 */
	protected int levelProgress = 0;
	
	private boolean fastMove = false;
	private boolean switched = false;
	private boolean rotated  = false;
	private boolean locking  = false;
	private int softdropScore = 0;
	private int harddropScore = 0;
	
	public TetrisSimulator() {
		reset();
	}
	
	//public static final void main(String[] args) {}
	
	public void tick(boolean left, boolean right, boolean hold, boolean rotateleft, boolean rotateright, boolean rotateflip, boolean softdrop, boolean harddrop) {
		frame ++;
		
		if(hold && !switched) {
			if(holding == null) {
				holding = current;
				current = next;
				next = generateRandomTetromino();
			} else {
				Tetromino tmp = current;
				current = holding;
				holding = tmp;
			}
			
			harddropScore = 0;
			softdropScore = 0;
			
			respawn();
			
			switched = true;
			
			return;
		}
		
		tickRotation(rotateleft, rotateright, rotateflip);
		tickMovement(left, right);
		
		if(frame - lastSpawnFrame < ARE) {
			return;
		}
		tickGravity(softdrop, harddrop);
	}
	
	public void tickGravity(boolean softdrop, boolean harddrop) {
		lastDropFrame ++;
		
		if(lastDropFrame * UNIT > getLevelSpeed()) {
			if(!locking) y --;
			
			lastDropFrame = 0;
		}
		
		if(isLocationBlocked(x, y - 1, r)) {
			if(!locking) {
				locking = true;
				lastLockFrame = frame;
			}
		} else locking = false;
		
		if(locking) {
			if(frame - lastLockFrame > 30) {
				writeToArray();
				
				return;
			}
		}
		
		if(harddrop) {
			for(int a = y; a > -4; a--) {
				if(isLocationBlocked(x, a - 1, r)) {
					y = a;
					harddropScore += 18 - a;
					
					break;
				}
			}
			
			
			writeToArray();
			
			return;
		}
		
		if(softdrop && frame - lastSoftdropInput > 1) {
			lastSoftdropInput = frame;
			
			collisionMovement(x, y - 1);
		}
	}
	
	public void tickMovement(boolean left, boolean right) {
		int nx = x;
		
		if(left) {
			if(!fastMove) {
				fastMove = true;
				nx --;
				lastMovementInput = frame;
			} else if(frame - lastMovementInput > DAS) {
				nx --;
				lastMovementInput += ARR;
			}
		} else if(right) {
			if(!fastMove) {
				fastMove = true;
				nx ++;
				lastMovementInput = frame;
			} else if(frame - lastMovementInput > DAS) {
				nx ++;
				lastMovementInput = frame - DAS + ARR;
			}
		} else fastMove = false;
		
		if(x != nx && collisionMovement(nx, y)) {
			if(x == nx)
				lastLockFrame = frame;
		}
	}
	
	public void tickRotation(boolean rotateleft, boolean rotateright, boolean rotateflip) {
		int nr = r;
		
		if(rotateleft) {
			if(!rotated) {
				rotated = true;
				nr ++;
			}
		} else if(rotateright) {
			if(!rotated) {
				rotated = true;
				nr --;
			}
		} else if(rotateflip) {
			if(!rotated) {
				rotated = true;
				nr += 2;
			}
		} else rotated = false;
		nr &= 3;
		
		if(r != nr && collisionRotation(nr))
			lastLockFrame = frame;
	}
	
	public void reset() {
		lastMovementInput = 0;
		lastRotationInput = 0;
		lastSoftdropInput = 0;
		lastSpawnFrame = 0;
		lastLockFrame = 0;
		lastDropFrame = 0;
		harddropScore = 0;
		softdropScore = 0;
		levelProgress = 0;
		frame = 0;
		
		fastMove = false;
		switched = false;
		rotated = false;
		locking = false;
		
		super.reset();
	}
	
	public void writeToArray() {
		int tetromino = current.getRotated(r);
		
		for(int i = 0; i < 16; i++) {
			int xp = (i &  3) + x;
			int yp = (i >> 2) + y;
			int bit = (tetromino >> i) & 1;
			
			if(bit < 1)
				continue;
			
			if(yp < 0 || yp >= HEIGHT || xp < 0 || xp >= WIDTH) continue;
			
			board[xp + yp * WIDTH] = current.id + 1;
		}
		
		calculateScore();
		
		current = next;
		next = generateRandomTetromino();
		
		respawn();
		
		lastSpawnFrame = frame;
		switched = false;
		locking  = false;
		
		if(isLocationBlocked(x, y - 1, r))
			reset();
		else
			y--;
	}
	
	public void calculateScore() {
		boolean tspin = false;
		if(current == Tetromino.T) { // Check for T-Spins
			if(isLocationBlocked(x - 1, y, r)
			&& isLocationBlocked(x + 1, y, r)
			&& isLocationBlocked(x, y + 1, r)) tspin = true;
		}
		
		int cleared = clearRows();
		lines += cleared;
		
		if(cleared == 1) levelProgress += 1;
		else if(cleared == 2) levelProgress += 3;
		else if(cleared == 3) levelProgress += 5;
		else if(cleared == 4) levelProgress += 8;
		
		if(levelProgress > (5 * (level + 1))) {
			levelProgress -= 5 * (level);
			
			level ++;
		}
		
		if(softdropScore > 20) softdropScore = 20;
		if(harddropScore > 20) harddropScore = 20;
		if(harddropScore < 0) harddropScore = 0;
		
		score += softdropScore;
		score += harddropScore * 2;
		
		if(tspin) {
			if(cleared == 0) score += 100 * (level + 1);
			if(cleared == 1) score += 400 * (level + 1);
			if(cleared == 2) score += 1200 * (level + 1);
			if(cleared == 3) score += 1600 * (level + 1);
		} else {
			if(cleared == 2) score += 300 * (level + 1);
			if(cleared == 3) score += 500 * (level + 1);
			if(cleared == 4) score += 800 * (level + 1);
		}
		
		softdropScore = 0;
		harddropScore = 0;
	}
	
	public long getFrame() {
		return frame;
	}
	
	public long getLockingIndex() {
		if(!locking) return 0;
		long index = frame - lastLockFrame;
		
		return index > 30 ? 30:index;
	}
	
	public int getLevelProgress() {
		return levelProgress;
	}
}
