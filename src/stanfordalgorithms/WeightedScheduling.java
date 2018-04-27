package stanfordalgorithms;

import java.io.*;
import java.util.*;

public class WeightedScheduling {
    public static void main(String[] args) throws IOException{
        Scanner scanner = new Scanner(new File("jobs.txt"));

        Job[] jobs = new Job[scanner.nextInt()];
        for (int i = 0; i < jobs.length; i++) jobs[i] = new Job(scanner.nextInt(), scanner.nextInt());

        Arrays.sort(jobs);
        int timeElapsed = 0;
        long weightedCompletion = 0;
        for (Job job : jobs) {
            timeElapsed += job.length;
            weightedCompletion += (job.weight*timeElapsed);
        }

        System.out.println(weightedCompletion);
    }
}

class Job implements Comparable<Job>{
    int weight, length;

    Job(int weight, int length){
        this.weight = weight;
        this.length = length;
    }


    @Override
    public int compareTo(Job other) {
        return (other.weight*this.length) - (this.weight*other.length);
    }
}
