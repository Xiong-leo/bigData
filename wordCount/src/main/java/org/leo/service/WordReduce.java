package org.leo.service;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class WordReduce extends Reducer<Text, IntWritable, Text, IntWritable> {

    int sum;
    IntWritable valueOut = new IntWritable();

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        sum = 0;
        for (IntWritable value : values) {
            sum += value.get();
        }
        valueOut.set(sum);
        context.write(key, valueOut);
    }
}
