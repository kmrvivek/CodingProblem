package com.java.problems;

public class InversionCount {

    public static void main(String[] args) {
        long arr[] = {2, 4, 1, 3, 5};
        long n = 5;
        long[] temp = new long[(int)n];
        InversionCount inversionCount = new InversionCount();
        System.out.println(inversionCount.megeSort(arr, temp,0, n-1));
    }

    public long megeSort(long arr[], long[] temp, long l, long r){
        long invCount = 0;
        if(l<r){
            long mid = l + (r-l)/2;
            invCount = megeSort(arr, temp, l, mid);
            invCount += megeSort(arr, temp,mid+1, r);

            invCount += merge(arr, temp, l, mid, r);

        }
        return invCount;
    }

    private long merge(long[] arr, long[] temp, long l, long mid, long r) {
        long invCount = 0;

        int i= (int) l;
        int j= (int) mid+1;
        int k = (int)l;
        while(i<=mid && j<=r){
            if(arr[i] <= arr[j]){
                temp[k++] = arr[i++];
            }else {
                invCount += mid+1-i;
                temp[k++] = arr[j++];
            }
        }
        while(i<=mid){
            temp[k++] = arr[i++];
        }
        while(j<=r){
            temp[k++] = arr[j++];
        }
        for(long m = l ; m <= r ; m++) {
            arr[(int) m] = temp[(int) m];
        }
        return invCount;
    }
}
