import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class AutocompleteTest {


      
      //add test
   	@Test(timeout = 10000)
   	public void testAdd() {
   		ArrayList<Term> tList = new ArrayList<Term>();
   		Term[] terms =
   				new Term[] {new Term("ape", 6), 
   				new Term("app", 4), 
   				new Term("ban", 2),
   				new Term("bat", 3),
   				new Term("bee", 5),
   				new Term("car", 7),
   				new Term("cat", 1)};
   		String[] queries = {"", "a", "ap", "ape", "app", "b", "ba", "ban", 
   				"bat", "be", "bee",	"c", "ca", "car", "cat", "f"};
   		for(Term t: terms)
   			tList.add(t);
   		ArrayList<ArrayList<Term>> orders = allPermutes(tList);
   		HashSet<ArrayList<String>> outputs = 
   				new HashSet<ArrayList<String>>();
   		for(ArrayList<Term> order: orders){
         // names adn weights lists
   			String[] names = new String[order.size()];
   			double[] weights = new double[order.size()];
   			for(int i = 0; i < order.size(); i++){
   				names[i] = order.get(i).getWord();
   				weights[i] = order.get(i).getWeight();
   			}
   			Autocomplete.TrieAutocomplete impAuto = new Autocomplete.TrieAutocomplete(names, weights);
   			ArrayList<String> result = new ArrayList<String>();
   			for(String query: queries){
   				result.add(impAuto.topMatch(query));
   			}
   			outputs.add(result);
   			assertTrue("results in add order",
   					result.size() <= 1);
   		}
   	}
   	//topmatch test
   	@Test(timeout = 10000)
   	public void testTopKMatches() {
   		Autocomplete.Autocompletor test = getData();
   		String[] queries = {"", "", "", "", "a", "ap", "b", "ba", "d"};
   		int[] ks = {8, 1, 2, 3, 1, 1, 2, 2, 100};
   		String[][] results = {
   				{"car", "ape", "bee", "app", "bat", "ban", "cat"},
   				{"car"}, 
   				{"car", "ape"}, 
   				{"car", "ape", "bee"}, 
   				{"ape"}, 
   				{"ape"},
   				{"bee", "bat"},
   				{"bat", "ban"},
   				{}
   		};
   		for(int i = 0; i < queries.length; i=i+2){
   			String query = queries[i];
   			String[] reported = iterToArr(test.topMatches(query, ks[i]));
   			String[] actual = results[i];
   			assertArrayEquals("wrong  "+query+" "+ks[i],
   					actual, reported);
   		}
   	}
   
   	
      //topmatch test
   	@Test(timeout = 10000)
   	public void testTopMatch() {
   		Autocomplete.Autocompletor test = getData();
   		String[] queries = {"", "a", "ap", "b", "ba", "c", "ca", "cat", "d", " "};
   		String[] results = {"car", "ape", "ape", "bee", "bat", "car", "car", "cat", "", ""};
   		for(int i = 0; i < queries.length; i=i+2){
   			String query = queries[i];
   			String reported = test.topMatch(query);
   			String actual = results[i];
   			assertEquals("wrong  "+query, actual, reported);
   		}
   	}
      Term[] terms =
			new Term[] {new Term("ape", 6), 
			new Term("app", 4), 
			new Term("ban", 2),
			new Term("bat", 3),
			new Term("bee", 5),
			new Term("car", 7),
			new Term("cat", 1)};
	   String[] names= {"ape", "app", "ban", "bat", "bee", "car", "cat"};;
   	double[] weights = {6, 4, 2, 3, 5, 7, 1};
   
   	public Autocomplete.Autocompletor getData(){
   		return getData(names, weights);
   	}
   
   	public Autocomplete.Autocompletor getData(String[] names, double[] weights){
   		return new Autocomplete.TrieAutocomplete(names, weights);
   	}
   
   	public class Autoincompletor extends Autocomplete.TrieAutocomplete{
   
   		public Autoincompletor(String[] terms, double[] weights) {
   			super(terms, weights);
   		}
   
   		@Override
   		public Iterable<String> topMatches(String prefix, int k){
   			return new LinkedList<String>();
   		}
   
   	}
   
   	public ArrayList<ArrayList<Term>> allPermutes(ArrayList<Term> arr){
   		if (arr.size() == 1){
   			ArrayList<ArrayList<Term>> output = new
   					ArrayList<ArrayList<Term>>();
   			output.add(arr);
   			return output;
   		}
   		ArrayList<ArrayList<Term>> output = 
   				new ArrayList<ArrayList<Term>>();
   		for(int i = 0; i < arr.size(); i++){
   			ArrayList<Term> arrcopy = new ArrayList<Term>(arr);
   			arrcopy.remove(i);
   			ArrayList<ArrayList<Term>> subPermutes = allPermutes(arrcopy);
   			for(ArrayList<Term> permute: subPermutes)
   				permute.add(arr.get(i));
   			output.addAll(subPermutes);
   		}
   		return output;
   	}
   	
   
   	
   
   	private String[] iterToArr(Iterable<String> it) {
   		ArrayList<String> list = new ArrayList<String>();
   		for (String s: it)
   			list.add(s);
   		return list.toArray(new String[0]);
   	}
}
