package linked;

public class Demo {
	
	public static void main(String[] args) {
		System.out.println("Testing\n");
		
		demoSingle();
	}
	
	public static void demoSingle() {
		Single singleList = new Single();
		
		System.out.println("List created via Singely Nodes\n");
		
		//singleList.get(0);	Testing Exception, Empty List
		
		singleList.add(1);
		//System.out.println("Getting " + singleList.get(0));
		
		singleList.set(0, 5);
		//System.out.println("Getting " + singleList.get(0));
		
		singleList.add(1);
		System.out.println("Singley Linked List:\n" + singleList.toString());
		
		//singleList.add(3);
		//singleList.removeLast();
		//System.out.println("Singley Linked List:\n" + singleList.toString());
		
		singleList.removeLast();
		System.out.println("Singley Linked List:\n" + singleList.toString());
	}

}
