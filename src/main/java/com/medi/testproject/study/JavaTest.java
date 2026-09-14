package com.medi.testproject.study;

public class JavaTest {

    public static void main(String[] args) {

        Member a = new Member(1L, "Kim");
        Member b = a;

        change(b);

        Member c = new Member(1L, "Lee");

        System.out.println(a == b);       // ①
        System.out.println(a.equals(b));  // ②

        System.out.println(a == c);       // ③
        System.out.println(a.equals(c));  // ④
    }

    static void change(Member member) {
        member.name = "Lee";

        member = new Member(2L, "Park");
    }

    static class Member {

        Long id;
        String name;

        Member(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public boolean equals(Object obj) {
            Member other = (Member) obj;
            return this.id.equals(other.id);
        }
    }
}