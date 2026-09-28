package com.medi.testproject.study;

import java.util.HashSet;
import java.util.Set;

public class JavaTest {


    public static void main(String[] args) {

        String a = "hello";
        String b = new String("hello");
        String c = b.intern();

        Set<String> set = new HashSet<>();

        set.add(a);
        set.add(b);
        set.add(c);

        System.out.println(a == b);       // ①
        System.out.println(b == c);       // ①
        System.out.println(a.equals(b));  // ②
        System.out.println(a == c);       // ③
        System.out.println(set.size());   // ④
    }

}