package com.rookiefly.test.commons.lucene;

import org.apache.lucene.analysis.standard.UAX29URLEmailAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Created by rookiefly on 2016/4/19.
 */
public class IndexTest {

    @Test
    public void test01() throws IOException {
        String indexPath = "src/test/indices/lukeindex";
        IndexWriterConfig indexCfg;
        Path path = FileSystems.getDefault().getPath(indexPath);
        Directory directory = NIOFSDirectory.open(path);
        indexCfg = new IndexWriterConfig(new UAX29URLEmailAnalyzer());
        indexCfg.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        IndexWriter writer = new IndexWriter(directory, indexCfg);

        Document doc = new Document();
        doc.add(new StringField("aaa", "1", Field.Store.NO));
        doc.add(new StringField("bbb", "2", Field.Store.YES));

        // sanity check
        doc.add(new StringField("ccc", "3", Field.Store.YES));

        writer.addDocument(doc);
        writer.close();
    }
}
