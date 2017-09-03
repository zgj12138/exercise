package edu.gduf.lucene.demo1.dao;

import edu.gduf.lucene.demo1.entity.Article;
import edu.gduf.lucene.demo1.util.LuceneUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author ZGJ
 * @date 2017/8/31 23:01
 **/
public class ArticleDao {
    @Test
    public void add() throws Exception {
        Article article = new Article(1, "电商", "阿里巴巴是一家互联网电商公司");
        Document document = LuceneUtil.javaBean2Document(article);
        IndexWriter indexWriter = new IndexWriter(LuceneUtil.getDirectory(), LuceneUtil.getAnalyzer(), LuceneUtil.getMaxFieldLength());
        indexWriter.addDocument(document);
        indexWriter.close();
    }
    @Test
    public void addAll () throws Exception {
        IndexWriter indexWriter = new IndexWriter(LuceneUtil.getDirectory(), LuceneUtil.getAnalyzer(), LuceneUtil.getMaxFieldLength());

        Article article1 = new Article(1, "电商", "唯品会是一家互联网电商公司");
        Document document1 = LuceneUtil.javaBean2Document(article1);
        indexWriter.addDocument(document1);
        Article article2 = new Article(2, "电商", "京东是一家互联网电商公司");
        Document document2 = LuceneUtil.javaBean2Document(article2);
        indexWriter.addDocument(document2);
        Article article3 = new Article(3, "电商", "淘宝是一家互联网电商公司");
        Document document3 = LuceneUtil.javaBean2Document(article3);
        indexWriter.addDocument(document3);
        Article article4 = new Article(4, "电商", "苏宁易购是一家互联网电商公司");
        Document document4 = LuceneUtil.javaBean2Document(article4);
        indexWriter.addDocument(document4);

        indexWriter.close();

    }
    @Test
    public void update() throws Exception {
        Article articleNew = new Article(4, "电商", "苏宁易购是一家线上线下结合的电商公司");
        Document document = LuceneUtil.javaBean2Document(articleNew);
        IndexWriter indexWriter = new IndexWriter(LuceneUtil.getDirectory(), LuceneUtil.getAnalyzer(), LuceneUtil.getMaxFieldLength());
        /**
         * 参数1：term表示需要更新的document对象
         * 参数2：新的document对象
         */
        indexWriter.updateDocument(new Term("id", "4"), document);
        indexWriter.close();
    }
    @Test
    public void delete() throws IOException {
        IndexWriter indexWriter = new IndexWriter(LuceneUtil.getDirectory(), LuceneUtil.getAnalyzer(), LuceneUtil.getMaxFieldLength());
        indexWriter.deleteDocuments(new Term("id", "4"));
        indexWriter.close();
    }
    @Test
    public void deleteAll() throws IOException {
        IndexWriter indexWriter = new IndexWriter(LuceneUtil.getDirectory(), LuceneUtil.getAnalyzer(), LuceneUtil.getMaxFieldLength());
        indexWriter.deleteAll();
        indexWriter.close();
    }

    @Test
    public void findAll() throws Exception {
        findAllByKeywords("电商");
    }
    public void findAllByKeywords(String keywords) throws Exception {
        List<Article> articles = new ArrayList<>();
        QueryParser queryParser = new QueryParser(LuceneUtil.getVersion(), "content", LuceneUtil.getAnalyzer());
        Query query = queryParser.parse(keywords);
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
