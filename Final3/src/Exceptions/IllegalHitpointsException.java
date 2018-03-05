package Exceptions;

public class IllegalHitpointsException extends Exception{
	
	private int value;
	private String message = "The value enter for hitpoints is a wrong value.";
	
	public IllegalHitpointsException(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	private static final long serialVersionUID = 1112229L;
}
