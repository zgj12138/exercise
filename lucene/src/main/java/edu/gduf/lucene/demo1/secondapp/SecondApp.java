package edu.gduf.lucene.demo1.secondapp;

import edu.gduf.lucene.demo1.entity.Article;
import edu.gduf.lucene.demo1.util.LuceneUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * @author ZGJ
 * @date 2017/8/31 22:35
 **/
public class SecondApp {
    @Test
    public void createIndexDB() throws Exception {
        Article article = new Article(1, "电商", "阿里巴巴是一家互联网电商公司");
        Document document = LuceneUtil.javaBean2Document(article);
        IndexWriter indexWriter = new IndexWriter(LuceneUtil.getDirectory(), LuceneUtil.getAnalyzer(), LuceneUtil.getMaxFieldLength());
        indexWriter.addDocument(document);
        indexWriter.close();
    }
    @Test
    public void searchIndexDB() throws Exception {
        String keyword = "电";
        List<Article> articles = new ArrayList<>();

        QueryParser queryParser = new QueryParser(LuceneUtil.getVersion(), "content", LuceneUtil.getAnalyzer());
        Query query = queryParser.parse(keyword);
        IndexSearcher indexSearcher = new IndexSearcher(LuceneUtil.getDirectory());
        TopDocs topDocs = indexSearcher.search(query, 100);
        for(int i = 0; i < topDocs.scoreDocs.length; i++) {
            ScoreDoc scoreDoc = topDocs.scoreDocs[i];
            int no = scoreDoc.doc;
            Document document = indexSearcher.doc(no);
            Article article = (Article) LuceneUtil.document2JavaBean(document, Article.class);
            articles.add(article);
        }
        articles.forEach(System.out::println);
    }
}
