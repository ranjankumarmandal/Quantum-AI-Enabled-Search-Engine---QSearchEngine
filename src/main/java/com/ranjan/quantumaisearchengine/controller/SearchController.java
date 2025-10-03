package com.ranjan.quantumaisearchengine.controller;

import com.ranjan.quantumaisearchengine.crawler.SimpleCrawler;
import com.ranjan.quantumaisearchengine.search.LuceneSearcher;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/api")
public class SearchController {
    private final LuceneSearcher searcher;
    private final SimpleCrawler crawler;


    public SearchController(LuceneSearcher searcher, SimpleCrawler crawler) {
        this.searcher = searcher;
        this.crawler = crawler;
    }


    @GetMapping("/search")
    public Map<String, Object> search(@RequestParam String q,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) throws Exception {
        int from = Math.max(0, page) * Math.max(1, size);
        var results = searcher.search(q, from, size);
        return Map.of("query", q, "page", page, "size", size, "results", results);
    }


    // Convenience endpoint to demo crawl (avoids writing a CLI runner right now)
    @PostMapping("/crawl")
    public Map<String, Object> crawl(@RequestParam String seed, @RequestParam(defaultValue = "50") int maxPages) throws Exception {
        crawler.crawlSeed(seed, maxPages);
        return Map.of("status", "ok", "seed", seed, "indexed", maxPages);
    }
}
