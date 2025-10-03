package com.ranjan.quantumaisearchengine.indexer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;


@Service
public class LuceneIndexer implements Closeable {
    private final Directory dir;
    private final Analyzer analyzer;
    private final IndexWriter writer;


    public LuceneIndexer(@Value("${app.index.path}") String indexPath) throws IOException {
        this.dir = FSDirectory.open(Path.of(indexPath));
        this.analyzer = new StandardAnalyzer();


        IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
        cfg.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        cfg.setSimilarity(new BM25Similarity());
        this.writer = new IndexWriter(dir, cfg);
    }


    public void indexDoc(String url, String title, String body, long timestamp) throws IOException {
        Document doc = new Document();
        doc.add(new StringField("url", url, Field.Store.YES));
        doc.add(new TextField("title", title == null ? "" : title, Field.Store.YES));
        doc.add(new TextField("body", body == null ? "" : body, Field.Store.NO));
        doc.add(new LongPoint("timestamp", timestamp));
        doc.add(new StoredField("timestampStored", timestamp));


        writer.updateDocument(new Term("url", url), doc);
    }


    public void commit() throws IOException { writer.commit(); }


    @Override
    public void close() throws IOException { writer.close(); dir.close(); analyzer.close(); }
}
