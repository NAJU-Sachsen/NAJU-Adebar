package adebar.util;

public class Iteration {
	
	@SuppressWarnings({"rawtypes", "unused"})
	public static int countElements(Iterable i) {
		int count = 0;
		for (Object o : i) {
			count++;
		}
		return count;
	}
	
}
