package Exceptions;

public class IllegalPurseContentException extends Exception{
	
	private boolean torn;
	
	public IllegalPurseContentException(boolean torn) {
		this.torn = true;
	}
	
	public boolean isTorn() {
		return this.torn;
	}
	
	private static final long serialVersionUID = 4442229L;
}
