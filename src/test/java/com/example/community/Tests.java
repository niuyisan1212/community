package com.example.community;


import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @Author INSLYAB
 * @Date 2022/7/2 8:47
 */
public class Tests {

    public static void main(String[] args) {
        System.out.print(1);
    }

    public static int god(int m, int n){
        while(n != 0){
            int r = m % n;
            m = n;
            n = r;
        }
        return m;
    }

    public void print(int value){

    }

    public Boolean function(String code){
        int n = code.length();
        Deque<String> tags = new ArrayDeque<String>();

        int i = 0;
        while (i < n) {
            if (code.charAt(i) == '<') {
                if (i == n - 1) {
                    return false;
                }
                if (code.charAt(i + 1) == '/') {
                    int j = code.indexOf('>', i);
                    if (j < 0) {
                        return false;
                    }
                    String tagname = code.substring(i + 2, j);
                    if (tags.isEmpty() || !tags.peek().equals(tagname)) {
                        return false;
                    }
                    tags.pop();
                    i = j + 1;
                    if (tags.isEmpty() && i != n) {
                        return false;
                    }
                } else {
                    int j = code.indexOf('>', i);
                    if (j < 0) {
                        return false;
                    }
                    String tagname = code.substring(i + 1, j);
                    if (tagname.length() < 1 || tagname.length() > 9) {
                        return false;
                    }
                    for (int k = 0; k < tagname.length(); ++k) {
                        if (!Character.isUpperCase(tagname.charAt(k))) {
                            return false;
                        }
                    }
                    tags.push(tagname);
                    i = j + 1;
                }
            } else {
                if (tags.isEmpty()) {
                    return false;
                }
                ++i;
            }
        }
            return tags.isEmpty();
    }
}
