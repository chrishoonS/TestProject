package com.medi.testproject.study;

public class Day005 {

    public static void main(String[] args) {
        Counter a = new Counter();
        Counter b = new Counter();

        a.increase();
        a.increase();
        b.increase();

        System.out.println(a.count);      // ① 2
        System.out.println(b.count);      // ② 1
        System.out.println(Counter.total);// ③ 3

        System.out.println("===============");
        User.print();

    }

    static class Counter {

        static int total = 0;
        int count = 0;

        void increase() {
            total++;
            count++;
        }
    }

    static class User {
        // 둘다 User class에 속함
        String name = "Kim";
        static int count = 10;

        static void print() {
            // print()도 static 메서드이므로 같은 클래스 수준에 있는 count에 직접 접근 가능.
            System.out.println(count); // ①
//            System.out.println(name);  // ② 인스턴스 변수 : static 메서드에서는 인스턴스 변수에 직접 접근 불가

            User user = new User();

            System.out.println(user.name); // ③
        }
    }

}

