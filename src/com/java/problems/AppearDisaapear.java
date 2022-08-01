package com.java.problems;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppearDisaapear {

        public static void main(String args[] ) throws Exception {


            Scanner s = new Scanner(System.in);
            int t = s.nextInt();                 // Reading input from STDIN
            int a[] = new int[t];
            for(int i=0; i<t; i++){
                a[i] = s.nextInt();
            }
            int num = s.nextInt();
            int[][] vec = new int[num][2];
            for(int i=0; i<num; i++){
                vec[i][0] = s.nextInt();
                vec[i][1] = s.nextInt();
            }
            checkNumbersPosition(a, vec);
        }
        static void checkNumbersPosition(int[] a, int[][] vec){
            List<Integer> ans = new ArrayList<>();
            int n = a.length - 1;
            int q = vec.length;
            for(int i=0; i<q; i++){
                int time = vec[i][0];
                int number = vec[i][0];
                if(number > n){
                    ans.add(-1);
                    continue;
                }

                int turn = time/n;
                int rem = time % n;
                if(rem == 0 && turn%2 == 1){
                    ans.add(-1);
                    continue;
                }
                if(rem == 0 && turn%2 == 0){
                    ans.add(a[number]);
                    continue;
                }

                if(turn%2 == 0){
                    int cursize = n-rem;
                    if(cursize <number){
                        ans.add(-1);
                        continue;
                    }
                    ans.add(a[number+rem]);
                } else {
                    int cursize = rem;
                    if(cursize <number){
                        ans.add(-1);
                        continue;
                    }
                    ans.add(a[number]);
                }

            }
            for(int i: ans){
                System.out.println(i);
            }
        }


}
