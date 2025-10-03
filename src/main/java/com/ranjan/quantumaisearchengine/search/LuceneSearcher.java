package com.ranjan.quantumaisearchengine.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;


@Service
public class LuceneSearcher implements Closeable {
    private final Directory dir;
    private final Analyzer analyzer;


    public LuceneSearcher(@Value("${app.index.path}") String indexPath) throws IOException {
        this.dir = FSDirectory.open(Path.of(indexPath));
        this.analyzer = new StandardAnalyzer();
    }


    public List<Map<String, Object>> search(String q, int from, int size) throws Exception {
        try (IndexReader reader = DirectoryReader.open(dir)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            searcher.setSimilarity(new BM25Similarity());


            QueryParser qp = new QueryParser("body", analyzer);
            Query query = qp.parse(QueryParser.escape(q));


            TopDocs top = searcher.search(query, Math.max(from + size, 1));


            List<Map<String, Object>> out = new ArrayList<>();
            var scorer = new QueryScorer(query);
            var highlighter = new Highlighter(new SimpleHTMLFormatter("<mark>", "</mark>"), scorer);
            var fragmenter = new SimpleSpanFragmenter(scorer, 90);
            highlighter.setTextFragmenter(fragmenter);


            for (int i = from; i < Math.min(top.scoreDocs.length, from + size); i++) {
                Document d = searcher.doc(top.scoreDocs[i].doc);
                String title = Optional.ofNullable(d.get("title")).orElse("");
                String url = d.get("url");
                String snippet = title; // fallback snippet (we didn't store body)
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("title", title.isBlank() ? url : title);
                item.put("url", url);
                item.put("score", top.scoreDocs[i].score);
                item.put("snippet", snippet);
                out.add(item);
            }
            return out;
        }
    }


    @Override
    public void close() throws IOException { dir.close(); analyzer.close(); }
}
