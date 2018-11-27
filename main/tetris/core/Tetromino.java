package main.tetris.core;

public enum Tetromino {
	I(0, 0x0f00, 0x2222, 0x00f0, 0x4444),
	J(1, 0x8e00, 0x6440, 0x0e20, 0x44c0),
	L(2, 0x2e00, 0x4460, 0x0e80, 0xc440),
	O(3, 0x6600, 0x6600, 0x6600, 0x6600),
	S(4, 0x6c00, 0x4620, 0x06c0, 0x8c40),
	T(5, 0x4e00, 0x4640, 0x0e40, 0x4c40),
	Z(6, 0xc600, 0x2640, 0x0c60, 0x4c80);
	
	private int[] rot;
	public final int id;
	private Tetromino(int id, int r1, int r2, int r3, int r4) {
		this.id = id;
		
		rot = new int[] { r1, r2, r3, r4 };
	}
	
	public int getRotated(int rot) {
		return this.rot[rot & 3];
	}
	
	public static Tetromino get(int id) {
		switch(id) {
			case 0: return I;
			case 1: return L;
			case 2: return J;
			case 3: return O;
			case 4: return S;
			case 5: return T;
			case 6: return Z;
		}
		
		return null;
	}
}