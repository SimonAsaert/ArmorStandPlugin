package tld.sima.armorstand.events;

public class Pair<T1, T2> {
	public T1 left;
	public T2 right;
	
	public Pair() {
		
	}
	
	public Pair(T1 left, T2 right) {
		this.left = left;
		this.right = right;
	}
	
	public void setLeft(T1 left) {
		this.left = left;
	}
	
	public void setRight(T2 right) {
		this.right = right;
	}
	
	public T1 getLeft() {
		return left;
	}
	
	public T2 getRight() {
		return right;
	}
}
