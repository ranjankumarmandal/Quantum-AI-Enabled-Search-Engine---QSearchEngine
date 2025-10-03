package com.ranjan.quantumaisearchengine.crawler;

import com.ranjan.quantumaisearchengine.indexer.LuceneIndexer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class SimpleCrawler {
    private final LuceneIndexer indexer;
    public SimpleCrawler(LuceneIndexer indexer) { this.indexer = indexer; }


    public void crawlSeed(String seedUrl, int maxPages) throws Exception {
        Queue<String> q = new ArrayDeque<>();
        Set<String> seen = new HashSet<>();
        q.add(seedUrl); seen.add(seedUrl);
        int count = 0;


        while (!q.isEmpty() && count < maxPages) {
            String url = q.poll();
            try {
                var doc = Jsoup.connect(url).userAgent("MiniSearchBot/1.0").timeout(12000).get();
                String title = doc.title();
                String body = doc.text();
                indexer.indexDoc(url, title, body, System.currentTimeMillis());
                count++;
                for (Element a : doc.select("a[href]")) {
                    String next = a.absUrl("href");
                    if (next.startsWith("http") && next.length() < 200 && seen.add(next) && seen.size() < 5000) {
                        q.add(next);
                    }
                }
            } catch (Exception e) {
// log and continue in real system
            }
        }
        indexer.commit();
    }
}
