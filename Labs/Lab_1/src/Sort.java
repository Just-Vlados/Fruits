import java.io.*;
import java.util.*;

/**
 * Created by Vlad on 16.02.2015.
 */
public class Sort implements Runnable {
    public void run() {
        HashSet<String> setofFruits = new HashSet<String>();
        String fruits;
        try {
            BufferedReader bf = new BufferedReader(new FileReader("fruits.in"));
            while ((fruits = bf.readLine()) != null) {
                StringTokenizer token = new StringTokenizer(fruits, " ");
                while (token.hasMoreTokens()) {
                    setofFruits.add(token.nextToken());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> listofSortFruits = new ArrayList<String>();
        for (String s : setofFruits) {
            StringTokenizer tokenizer = new StringTokenizer(s, " ");
            while (tokenizer.hasMoreTokens()) {
                listofSortFruits.add(tokenizer.nextToken());
            }
        }
        Collections.sort(listofSortFruits, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        Iterator<String> it = listofSortFruits.iterator();
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("juice2.out"));
            String fruit;
            while (it.hasNext()) {
                fruit = it.next();
                try {
                    out.write(fruit);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
