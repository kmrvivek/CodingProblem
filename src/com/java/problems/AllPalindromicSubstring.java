package com.java.problems;

public class AllPalindromicSubstring {

    public static void main(String[] args) {
        String str = "abccbc";
        palindromicSubstring(str);

    }

    private static void palindromicSubstring(String str){
        Pali[][] dp = new Pali[str.length()][str.length()];
        for(int i=0; i<str.length(); i++){
            dp[i][i] = new Pali(true, str.substring(i,i+1));
        }

        int j = 0;
        int num = str.length();
        while(j++ <= str.length() && num-- >= 0){
            for (int i=0; i<num; i++){
                int gap = j+i;
                if(str.charAt(i) != str.charAt(gap)){
                    dp[i][gap] = new Pali(false, str.substring(i, gap+1));
                }
                    boolean b = str.charAt(i) == str.charAt(gap);
                    if(b && gap-i == 1){
                        dp[i][gap] = new Pali(true, str.substring(i, gap+1));
                    } else if(b &&  dp[i+1][gap-1] != null && dp[i+1][gap-1].isPalindrome){
                        dp[i][gap] = new Pali(true, str.substring(i, gap+1));
                    } else if(b && dp[i+1][gap-1] != null && !dp[i+1][gap-1].isPalindrome)
                        dp[i][gap] = new Pali(false, str.substring(i, gap+1));
                    }

            }

        for (int i=0; i<str.length(); i++){
            for(int k=0; k<str.length(); k++){
               if(dp[i][k] != null && dp[i][k].isPalindrome){
                   System.out.println(dp[i][k].val);
               }
            }
        }
        /*
            for (int i=0; i<str.length(); i++){
                for(int k=0; k<str.length(); k++){
                    System.out.print(dp[i][k]+ " ");
                }
                System.out.println();
            }

         */
        }

}

class Pali {

    boolean isPalindrome;
    String val;

    public Pali(boolean isPalindrome, String val) {
        this.isPalindrome = isPalindrome;
        this.val = val;
    }

    @Override
    public String toString() {
        return  "("+isPalindrome +"  "+val+")";
    }
}
