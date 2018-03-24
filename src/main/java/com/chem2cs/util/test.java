package com.chem2cs.util;

import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;

public class test {

    public static void foo(String s){
        System.out.println(s.hashCode());
        s="world";
        System.out.println(s.hashCode());
    }

    public static void foo2(StringBuilder sb){
        sb.append("world");
    }
    public static void main(String[] args) {
        String s=new String("hello");
        foo(s);
        System.out.println(s);
        StringBuilder sb=new StringBuilder("hello");
        foo2(sb);
        System.out.println(sb);
    }
}
