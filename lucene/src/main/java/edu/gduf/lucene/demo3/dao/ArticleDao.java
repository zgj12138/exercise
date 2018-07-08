package edu.gduf.lucene.demo3.dao;

import edu.gduf.lucene.demo1.util.LuceneUtil;
import edu.gduf.lucene.demo3.entity.Article;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * lucene优化问题
 * @author ZGJ
 * create on 2018/5/7 22:02
 **/
public class ArticleDao {
    /**
     * 运行5次，生成多个cfs文件
     * @throws Exception
     */
    @Test
    public void add() throws Exception {
        Article article = new Article(1, "互联网", "阿里巴巴是一家互联网公司", 10);
        Document document = LuceneUtil.javaBean2Document(article);
        IndexWriter indexWriter = new IndexWriter(LuceneUtil.getDirectory(), LuceneUtil.getAnalyzer(), LuceneUtil.getMaxFieldLength());
        indexWriter.addDocument(document);
        indexWriter.close();
    }

    /**
     * 合并cfs文件，合并后压缩
     */
    @Test
    public void method1() throws Exception {
        Article article = new Article(1, "互联网", "阿里巴巴是一家互联网公司", 10);
        Document document = LuceneUtil.javaBean2Document(article);
        IndexWriter indexWriter = new IndexWriter(LuceneUtil.getDirectory(), LuceneUtil.getAnalyzer(), LuceneUtil.getMaxFieldLength());
        indexWriter.addDocument(document);
        //合并文件
        indexWriter.optimize();
        indexWriter.close();
    }

    @Test
    public void findAll() throws Exception {
        String keywords = "互";
        List<Article> articles = new ArrayList<>();

        QueryParser queryParser = new QueryParser(LuceneUtil.getVersion(), "content", LuceneUtil.getAnalyzer());
        Query query = queryParser.parse(keywords);
        IndexSearcher indexSearcher = new IndexSearcher(LuceneUtil.getDirectory());
        TopDocs topDocs = indexSearcher.search(query, 100);
        for (int i = 0; i < topDocs.scoreDocs.length; i++) {
            ScoreDoc scoreDoc = topDocs.scoreDocs[i];
            int no  = scoreDoc.doc;
            Document document = indexSearcher.doc(no);
            Article article = (Article) LuceneUtil.document2JavaBean(document, Article.class);
            articles.add(article);
        }
        articles.forEach(System.out::println);

    }

    /**
     * 设置合并因子，自动合并文件
     * @throws Exception
     */
    @Test
    public void method2() throws Exception {
        Article article = new Article(1, "互联网", "阿里巴巴是一家互联网公司", 10);
        Document document = LuceneUtil.javaBean2Document(article);
        IndexWriter indexWriter = new IndexWriter(LuceneUtil.getDirectory(), LuceneUtil.getAnalyzer(), LuceneUtil.getMaxFieldLength());
        indexWriter.addDocument(document);
        //设置合并因子，满足3个合并，如果不设置，默认是10
        indexWriter.setMergeFactor(3);
        indexWriter.close();
    }

    /**
     * 设置RAMDirectory，内存索引库
     * @throws Exception
     */
    @Test
    public void method3() throws Exception {
        Article article = new Article(1, "互联网", "阿里巴巴是一家互联网公司", 10);
        Document document = LuceneUtil.javaBean2Document(article);
        //硬盘索引库
        Directory fsDirectory = FSDirectory.open(new File("f:\\indexDB"));
        //内存索引库,需要将硬盘索引库中的内容同步到内存索引库中
        Directory ramDirectory = new RAMDirectory(fsDirectory);

        //指向硬盘索引库的流
        IndexWriter fsIndexWriter = new IndexWriter(fsDirectory, LuceneUtil.getAnalyzer(), true, LuceneUtil.getMaxFieldLength());
        //指向内存索引库的流
        IndexWriter ramIndexWriter = new IndexWriter(ramDirectory, LuceneUtil.getAnalyzer(), LuceneUtil.getMaxFieldLength());

        ramIndexWriter.addDocument(document);
        ramIndexWriter.close();

        //将内存索引库中的数据同步到硬盘索引库中
        fsIndexWriter.addIndexesNoOptimize(ramDirectory);
        fsIndexWriter.close();
    }
}
