package com.java.Interviews;

import java.util.Arrays;

public class Interview {

  public static void main(String[] args) throws Exception {
   float[] movie = {1.01f, 1.75f, 2.75f, 2.09f};
    System.out.println(minDay(movie));
  }

  public static int minDay(float[] movie){
    int dayRequire = 0;
    Arrays.sort(movie);
    int start = 0;
    for(int end = movie.length-1; end >= start; end--){
      if(movie[end] > 1.99f){
        dayRequire++;
      } else if(movie[end] <= 1.99f){
        if(start != end && movie[start]+movie[end] <= 3f){
          start++;
        }
        dayRequire++;
      }

    }
    return dayRequire;
  }

}
