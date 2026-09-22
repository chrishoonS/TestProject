package com.medi.testproject.study;

public class Day004 {

    public static void main(String[] args) {

        String a = "hello";
        String b = "hello";
        String c = new String("hello");

        System.out.println(a == b);       // ① : a, b가 String Pool의 동일 "hello" 객체 참조
        System.out.println(a.equals(b));  // ② : String.equals()는 문자열 내용 비교

        System.out.println(a == c);       // ③ : a는 String Pool의 "hello"를 참조하고, c는 new String()으로 생성한 별도 객체를 참조하므로 false.
        System.out.println(a.equals(c));  // ④ : 객체는 달라도 문자열 내용은 "hello"로 동일

        String d = c.intern();

        System.out.println(a == d);       // ⑤ : intern()이 Pool의 "hello" 참조 반환
        System.out.println(c == d);       // ⑥ : c는 별도 객체, d는 Pool 객체 참조

        /**
         * Heap
         * c ─────────→ String 객체 "hello"
         *
         *
         * String Pool
         * a ──┐
         * b ──┼──────→ String 객체 "hello"
         * d ──┘
         **/
        System.out.println("==================");

        String x = "Java";
        String y = x;

        x = x + " Spring";

        System.out.println(x);       // ① : 새로운 결과 문자열 "Java Spring"이 만들어지고, a가 해당 객체를 참조
        System.out.println(y);       // ② : b = a 시점에 a의 참조값이 b에 복사되어, a와 b가 동일한 "Java" String 객체를 참조
        System.out.println(x == y);  // ③ : Reference Type에서 ==는 같은 객체를 참조하는지 비교.

        
        System.out.println("==================");

        StringBuilder a1 = new StringBuilder("Java");
        StringBuilder b1 = a1;

        a1.append(" Spring");

        System.out.println(a1);        // ①
        System.out.println(b1);        // ②
        System.out.println(a1 == b1);  // ③

        System.out.println("==================");

        StringBuilder a2 = new StringBuilder("Java");
        StringBuilder b2 = a2;

        a2.append(" Spring");

        System.out.println(a2);       // ①
        System.out.println(b2);       // ②
        System.out.println(a2 == b2);  // ③

        /**
         * sb ──→ StringBuilder 객체
         *           │
         *           ├─ "Java"
         *           ├─ "JavaSpring"
         *           └─ "JavaSpringBoot"
         * 동일한 StringBuilder 인스턴스의 상태가 변경
         **/

    }
}