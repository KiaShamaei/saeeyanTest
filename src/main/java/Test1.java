import java.util.*;

public class Test1 {
    //Write a Java Program to count the number of words in a string using HashMap?
    //I'm writing a method to do count word and return it after that print it in console
    public  List<Map.Entry<String, Integer>> countWord (String sentence){
        String[] splitStringArray = sentence.split(" ");
        List<String> listTemp = new ArrayList<String>();
        Map<String,Integer> outputMap = new HashMap<>();
        listTemp = Arrays.asList(splitStringArray);
        for (int i=0 ; i < listTemp.size() ; i ++ ) {
            outputMap.put(listTemp.get(i),i);
        }
        List<Map.Entry<String, Integer>> orderOutPut = new LinkedList<>(outputMap.entrySet());
        orderOutPut.sort(Comparator.comparing(Map.Entry::getValue));
        return orderOutPut;
    }

    public static void main(String[] args) {
        Test1 test1 = new Test1();
        System.out.println(test1.countWord("this is a test form "));
    }

}
