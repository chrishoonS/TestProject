package com.medi.testproject.study;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Day003 {

    public static void main(String[] args) {

        Member a = new Member(1L, "Kim");
        Member b = new Member(1L, "Lee");

        System.out.println(a.equals(b));  // true
        System.out.println(a.hashCode()); // 12345
        System.out.println(b.hashCode()); // 12345

        Set<Member> members = new HashSet<>();

        members.add(a); // 12345
        members.add(b); // 12345

        System.out.println(members.size()); // 1

        String a1 = new String("hello");
        String b1 = a1.intern();
        String c1 = "hello";
        System.out.println("++++++++++");
        System.out.println(a1);
        System.out.println(b1);
        System.out.println(c1);
        System.out.println("++++++++++");

        System.out.println(a1 == b1); // false
        System.out.println(a1 == c1); // false
        System.out.println(b1 == c1); // true
    }

    static class Member {

        Long id;
        String name;

        Member(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {

            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Member member = (Member) o;

            return Objects.equals(id, member.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}