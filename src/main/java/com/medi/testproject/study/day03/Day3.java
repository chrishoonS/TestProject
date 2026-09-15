package com.medi.testproject.study.day03;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Day3 {

    public static void main(String[] args) {

        Member a = new Member(1L, "Kim");
        Member b = new Member(1L, "Lee");

        System.out.println(a.equals(b));
        System.out.println(a.hashCode());
        System.out.println(b.hashCode());

        Set<Member> members = new HashSet<>();

        members.add(a);
        members.add(b);

        System.out.println(members.size());
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
    }
}