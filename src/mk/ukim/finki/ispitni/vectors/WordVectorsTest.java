package mk.ukim.finki.ispitni.vectors;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Word vectors test
 */
class WordVectors
{
    Map<String,List<Integer>> vectorByWord;
    List<String> wordsList;

    public WordVectors(String[] words, List<List<Integer>> vectors) {
        vectorByWord=new HashMap<>();
        wordsList =new ArrayList<>();
        for(int i=0;i<words.length;i++)
        {
            vectorByWord.put(words[i],vectors.get(i));
        }

    }

    public void readWords(List<String> wordsList) {
        this.wordsList =wordsList;
    }

    public List<Integer> slidingWindow(int n) {
        List<Integer> maxSizeList=new ArrayList<>();
        //for(int i=0;i<wordsList.size();i++)
        //{
       ////    for(int j=i+n;j<wordsList.size();j++)
       ////    {
       ////       wordsList.subList(i,j);
       ////
       ////    }
       ////}
       for(int i=0;i<wordsList.size()-n;i++)
       {
           List<String> firstList=wordsList.subList(i,i+n);
           List<List<Integer>>toFindMax=new ArrayList<>();
           for(String s:firstList)
           {
               if(!vectorByWord.containsKey(s))
               {
                   List<Integer> basic=new ArrayList<>();
                   basic.add(5);
                   basic.add(5);
                   basic.add(5);
                   basic.add(5);
                   basic.add(5);
                   toFindMax.add(basic);
               }
               else
               {
                   toFindMax.add(vectorByWord.get(s));
               }


           }
           for(int j=0;j<toFindMax.size();j++)
           {
               List<Integer> result=new ArrayList<>();
               result.add(0);
               result.add(0);
               result.add(0);
               result.add(0);
               result.add(0);
               List<Integer> helper=toFindMax.get(j);
               for(int k=0;k<5;k++)
               {
                   int value=result.get(k);
                   value+=helper.get(k);
                   result.add(k,value);
                   result.remove(k+1);
               }
               int max=result.stream()
                       .mapToInt(Integer::intValue)
                       .max()
                       .orElse(5);
               maxSizeList.add(max);
           }

       }

        return maxSizeList;
    }
}
public class WordVectorsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] words = new String[n];
        List<List<Integer>> vectors = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            words[i] = parts[0];
            List<Integer> vector = Arrays.stream(parts[1].split(":"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            vectors.add(vector);
        }
        n = scanner.nextInt();
        scanner.nextLine();
        List<String> wordsList = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            wordsList.add(scanner.nextLine());
        }
        WordVectors wordVectors = new WordVectors(words, vectors);
        wordVectors.readWords(wordsList);
        n = scanner.nextInt();
        List<Integer> result = wordVectors.slidingWindow(n);
        System.out.println(result.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));
        scanner.close();
    }
}

