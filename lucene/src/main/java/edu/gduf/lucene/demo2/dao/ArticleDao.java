package edu.gduf.lucene.demo2.dao;

import edu.gduf.lucene.demo1.entity.Article;
import edu.gduf.lucene.demo1.util.LuceneUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZGJ
 * @date 2017/8/31 23:37
 **/
public class ArticleDao {

    public int getAllRecord(String keywords) throws Exception {
        List<Article> articles = new ArrayList<>();
        QueryParser queryParser = new QueryParser(LuceneUtil.getVersion(), "content", LuceneUtil.getAnalyzer());
        Query query = queryParser.parse(keywords);
        IndexSearcher indexSearcher = new IndexSearcher(LuceneUtil.getDirectory());
        TopDocs topDocs = indexSearcher.search(query, 100);
        //返回真实的记录数，不受100影响
        return topDocs.totalHits;
    }
    public List<Article> findAll(String keywords,int start, int size) throws Exception {
        List<Article> articles = new ArrayList<>();

        QueryParser queryParser = new QueryParser(LuceneUtil.getVersion(), "content", LuceneUtil.getAnalyzer());
        Query query = queryParser.parse(keywords);
        IndexSearcher indexSearcher = new IndexSearcher(LuceneUtil.getDirectory());
        TopDocs topDocs = indexSearcher.search(query, 100);
        int max = Math.max(start + size, topDocs.totalHits);
        for(int i = start; i < max; i++) {
            ScoreDoc scoreDoc = topDocs.scoreDocs[i];
            int no = scoreDoc.doc;
            Document document = indexSearcher.doc(no);
            Article article = (Article) LuceneUtil.document2JavaBean(document, Article.class);
            articles.add(article);
        }
        return articles;
    }
}
