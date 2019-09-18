import java.util.*;
import java.util.stream.*;

class A {
	private String str;
	private int i;
}

public class HelloWorld{

	 public static void main(String []args){
		System.out.println("Hello World");
		
		//get Stream from Arr
		A [] a = new A[3];
		for (A el : a) {
			el = new A();
		}
		Stream<A> arrStream = Arrays.stream(a);
		
		//from List
		ArrayList<A> list = new ArrayList<>();
		list.add(new A());
		Stream<A> listStream = list.stream();
		
		//from Set
		Set<A> set = new HashSet<>();
		set.add(new A());
		Stream<A> setStream = set.stream();
		
		//from Map
		Map<String, A> map = new HashMap<>();
		map.put("first", new A());
		Set<Map.Entry<String, A>> setMap = map.entrySet();
		Stream<Map.Entry<String, A>> mapStream = setMap.stream();
		
		//list from stream through Collectors class methods
		List<A> listFromStream = listStream.collect(Collectors.toList());
		
		//full convert through collect()
		List<A> listFullV1 = listStream.collect(
			() -> new LinkedList<>(), //constructor 
			(listA, element) -> listA.add(element), //adder (2 parametrs)
			(listA, listB) -> listA.addAll(listB) //joiner (2 parameters)
		);
		
		List<A> listFullV2 = listStream.collect(
			LinkedList::new,
			LinkedList::add,
			LinkedList::addAll
		);
		
		//iterator
		Iterator<A> iterator = listStream.iterator();
		while(iterator.hasNext()) {
			System.out.println(iterator.next());
		}
		
		//spliterator
		
			//tryAdvance(Consumer accept(...)) == hasNext() + next()
			Spliterator<A> spl1 = listStream.spliterator();
			while(
				spl1.tryAdvance(
					(n) -> System.out.println(n)
				)
			);
			
			//do all action on list elements together (in one time)
			spl1.forEachRemaining(
				(n) -> System.out.println(n)
			);
			
			//trySplit() - divide on two equal half, null if error
			Spliterator<A> spl2 = spl1.trySplit();
			
			if(spl2 != null) {
				//print FIRSTS elements from listStream: 1, 2, 3
				spl2.forEachRemaining((n) -> System.out.println(n));
			}
			//print LASTS elements from listStream: 4, 5, 6
			spl1.forEachRemaining((n) -> System.out.println(n));
	 }
}