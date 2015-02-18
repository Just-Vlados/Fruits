import java.io.*;
import java.util.*;

/**
 * Created by Vlad on 16.02.2015.
 */
public class Sort implements Runnable {
    public void run() {
        HashSet<String> set = new HashSet<String>();
        String s1;
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new FileReader("fruits.in"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            while((s1 = bf.readLine()) != null){
                StringTokenizer token = new StringTokenizer(s1, " ");
                while(token.hasMoreTokens()) {
                    set.add(token.nextToken());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> listOut1 = new ArrayList <String>();
        for(String s: set){
            StringTokenizer tokenizer = new StringTokenizer(s, " ");
            while(tokenizer.hasMoreTokens()){
                listOut1.add(tokenizer.nextToken());
            }
        }
        Collections.sort(listOut1, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        Iterator<String> it = listOut1.iterator();
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter("juice2.out"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s;
        while(it.hasNext()){
            s = it.next();
            try {
                out.write(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
