package com.zillow;

public class DefaultDemo {
    /**
     * Legacy Interface
     */
    interface LegacyInterface {
        public String sayHi();

        /*
        default String sayMaybe() {
            return "maybe".toUpperCase();
        }
        */


        /*
        static public String toEmoticon(String ascii) {
            char[]  chars = Character.toChars(0x2753);
            if(ascii.equalsIgnoreCase(":)")) {
                chars = Character.toChars(0x1F600);
            } else if(ascii.equalsIgnoreCase(":(")) {
                chars = Character.toChars(0x1F622);
            }
            return String.valueOf(chars);
        }
        */
    }


    interface NewInterface {
        default String sayMaybe() { return "12312";}
    }


    /**
     * Legacy Client
     */
    static class LegacyClient implements LegacyInterface, NewInterface {
        @Override
        public String sayHi() {
            return "Hi from legacy!!".toUpperCase();
        }
    }




    public static void main(String []args) {

        LegacyInterface l = new LegacyClient();
        System.out.println(l.sayHi());

        //The method is now implemented in the interface
        //System.out.println(l.sayMaybe());

        //Interfaces now support static methods
        //System.out.println(LegacyInterface.toEmoticon(":)"));
    }
}




