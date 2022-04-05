package com.java.simple;

public class PartionPalindrome {

    public static void main(String[] args) {
        String str = "GEEK";
        System.out.println(getMinCutPalindrome(str, 0, str.length()));
        System.out.println(getMiCutDp(str));
    }

    private static int getMiCutDp(String str){
        int n = str.length();
        int[][] dp = new int[n][n];
        for(int gap=1; gap<n; gap++){
            for(int i=0; i+gap<n; i++){
                int j = i+gap;
                if(isPalindrome(str.substring(i, j+1))){
                    dp[i][j] = 0;
                } else {
                    dp[i][j] = Integer.MAX_VALUE;
                    for(int k=i; k<j; k++){
                        dp[i][j] = Math.min(dp[i][j], 1+dp[i][k]+dp[k+1][j]);
                    }
                }
            }
        }
        //print2dArray(dp);
        return dp[0][n-1];
    }

    private static void print2dArray(int[][] arr){
        for (int i=0; i< arr.length; i++){
            for (int j=0; j< arr.length; j++){
                System.out.print(arr[i][j]+" ");
            }
            System.out.println();
        }
    }

    private static int getMinCutPalindrome(String str, int i, int j){
        if(isPalindrome(str.substring(i, j))){
            return 0;
        }
        int result = Integer.MAX_VALUE;
        for(int k = i; k<j; k++){
            int leftPart = getMinCutPalindrome(str, i, k);
            int rightPart = getMinCutPalindrome(str, k+1, j);
            result = Math.min(result, 1+ leftPart+rightPart);
        }
        return result;
    }

    private static boolean isPalindrome(String str){
        int i=0, j = str.length()-1;
        while(i<j){
            if(str.charAt(i) == str.charAt(j)){
                i++;
                j--;
            } else {
                return false;
            }
        }
        return true;
    }
}
