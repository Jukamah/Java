package linked;

public class Single <T> {
	
	private Node first;
	
	public Single() {
		first = null;
	}
	
	public void add(T t) {
		Node node = first, temp = new Node(t);
		temp.next = null;
		
		if(node == null)
			first = temp;
		else {
			while(node.next != null) {
				node = node.next;
			}
			
			node.next = temp;
		}
		
	}
	
	public T get(int i) {
		Node node = first;
		
		while(i > 0) {
			if(node == null)
				throw new NullPointerException("Null Node Accessed");
			
			node = node.next;
			i--;
			
			//System.out.println("We should only be here if its non 0 natural index");
		}
		
		//System.out.println("Should return " + node.getData());
		
		return (T)node.getData();
	}
	
	public void set(int i, T t) {
		Node node = first;
		
		while(i > 0) {
			if(node == null)
				throw new NullPointerException("Null Node Accessed");
			
			node = node.next;
			i--;
			
			//System.out.println("We should only be here if its non 0 natural index");
		}
		
		node.setData(t);
	}
	
	public void insert(int i, T t) {
		
	}
	
	public void removeLast()  {
		Node node = first;
		
		if(node == null)
			return;
		else
			if(node.next == null)
				first = null;
			else
				do {
					if((node.next).next == null)
						node.next = null;
				} while(node.next != null);
		
		
	}
	
	@Override
	public String toString() {
		String string = new String("");
		
		Node node = first;
		
		System.out.println("Testing toString()");
		if(node != null)
			do {
				string += (node.getData() + "\n");
				node = node.next;
				
				//System.out.println("Are we here");
			} while(node != null);
		
		return string;
	}
}
