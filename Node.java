package linked;

public class Node <T> {

	public Node  prev, next;
	
	private T data;
	
	public Node() {
		//System.out.println("Created without data");
	}
	
	public Node(T t) {
		//data = t;
		this.setData(t);
		
		//System.out.println("Created with data " + this.getData());
	}
	
	public void setData(T newData) {
		data = newData;
	}
	
	public T getData() {
		return data;
	}
	
}