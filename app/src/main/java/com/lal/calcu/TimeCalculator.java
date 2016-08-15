package com.lal.calcu;



/**
 * Created by sgs12 on 27/03/16.
 */
public class TimeCalculator {


    private static TimeCalculator instance;
    private int number;
    private int sum;
    private static Integer MIN = 1000;
    private static Integer MAX = 9999;


    public TimeCalculator(){

    }


    public int getNumber(){

        number = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        int partialSum = 0;
        int partialNum = number;

        while (partialNum > 9 ) {
            partialSum = 0;
            while (partialNum > 0) {
                int rem;
                rem = partialNum % 10;
                if(rem == 0)
                    getNumber();
                partialSum = partialSum + rem;
                partialNum = partialNum / 10;
            }
            partialNum = partialSum;
        }
        sum = partialSum;
        return number;
    }


    public boolean verifySum(int number){
        return sum==number;
    }
}
