package org.migor.entropy;

/**
 * Created by damoeb on 7/26/14.
 */
public class Tets {
    public static void main(String[] args) {
        String[] us = new String[]{
                "http://www.youtube.com/watch?v=dhHnxmJ0bIo",
                "http://stackoverflow.com/questions/9321553/java-convert-integer-to-hex-integer",
                "http://en.wikipedia.org/wiki/Black_propaganda",
                "http://derstandard.at/2000003536974/Stadtschulrat-Haeupl-vorerst-gegen-Krauss-Bestellung",
                "http://derstandard.at/",
                "http://derstandard.at/2000003552905/Da-macht-es-sich-die-Justiz-recht-einfach",
                "lack propaganda is false information and material that purports to be from a source on one side of a conflict, but is actually from the opposing side. It is typically used to vilify, embarrass or misrepresent the enemy.[1] Black propaganda contrasts with grey propaganda, the source of which is not identified, and white propaganda, in which the real source is declared and usually more accurate information is given, albeit slanted, distorted and omissive. Black propaganda is covert in nature in that its aims, identity, significance, and sources are hidden."
        };
//        for(int i=0; i<10; i++) {
        for (String u : us) {
            System.out.println(Integer.toHexString(u.hashCode()));
        }
    }
}
