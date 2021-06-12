package com.udacity.webcrawler;

import com.udacity.webcrawler.json.CrawlResult;
import com.udacity.webcrawler.parser.PageParser;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ParallelCrawlTask extends RecursiveTask<CrawlResult> {
    List<String> startingUrls;
    Instant deadline;
    Map<String, Integer> counts = new HashMap<>();
    Set<String> visitedUrls = new HashSet<>();

    public ParallelCrawlTask(List<String> startingUrls, Instant deadline, Map<String, Integer> counts,
                             Set<String> visitedUrls) {
        this.startingUrls=startingUrls;
        this.deadline = deadline;
        this.counts=counts;
    }

    @Override
    protected CrawlResult compute() {
        List<ParallelCrawlTask> subTasks = startingUrls.stream().
                map(url-> new ParallelCrawlTask(url)).collect(Collectors.toList());
        return null;
    }



}
