package edu.gduf.lucene.firstapp;

import edu.gduf.lucene.entity.Article;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * lucene的第一个例子
 * @author ZGJ
 * @date 2017/8/30 21:45
 **/
public class FirstApp {
    /**
     * 创建索引库
     */
    @Test
    public void createIndexDB() throws IOException {
        //创建Article对象
        Article article = new Article(1, "电商", "阿里巴巴是一家互联网电商公司");
        //创建Document对象
        Document document = new Document();
        //将Article对象的三个属性值分别绑定到Document对象
        /**
         * 参数1：document对象中的属性名叫xid, article对象中的属性名叫id,建议相同
         * 参数2：document对象中的属性的xid的值, 与article对象中的值相同
         * 参数3：是否将xid属性值存入原始记录表中转入词汇表
         * 参数4：是否将xid属性进行分词算法
         *       Index.ANALYZED 会进行词汇拆分
         *       Index.NOT_ANALYZED 不会拆分
         *       建议非id值都进行拆分
         */
        document.add(new Field("xid", article.getId().toString(), Field.Store.YES, Field.Index.ANALYZED));
        document.add(new Field("xtitle", article.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
        document.add(new Field("xcontent", article.getContent(), Field.Store.YES, Field.Index.ANALYZED));
        /**
         * 1. 索引库对应的硬盘中的目录
         * 2. 采用什么策略将文本和拆分
         * 3. 最多将文本拆分出多少词汇, limited表示1w个，如果不足1w,以实际为准
         */
        //索引库对应的硬盘中的目录
        Directory directory = FSDirectory.open(new File("f:\\indexDB"));
        //采用什么策略将文本和拆分
        Version version = Version.LUCENE_30;
        Analyzer analyzer = new StandardAnalyzer(version);
        //最多将文本拆分出多少词汇
        IndexWriter.MaxFieldLength maxFieldLength = IndexWriter.MaxFieldLength.LIMITED;
        IndexWriter indexWriter = new IndexWriter(directory,analyzer, maxFieldLength);
        //将document对象写入lucene索引库
        indexWriter.addDocument(document);
        //关闭IndexWriter对象
        indexWriter.close();
    }
    /**
     * 根据关键字从索引库中搜索符合条件的内容
     */
    @Test
    public void findIndexDB() throws Exception {
        /**
         * 前期准备工作
         */
        String keywords = "电";
        List<Article> articleList = new ArrayList<>();
        //索引库对应的硬盘中的目录
        Directory directory = FSDirectory.open(new File("f:\\indexDB"));
        //采用什么策略将文本和拆分
        Version version = Version.LUCENE_30;
        Analyzer analyzer = new StandardAnalyzer(version);
        //最多将文本拆分出多少词汇
        IndexWriter.MaxFieldLength maxFieldLength = IndexWriter.MaxFieldLength.LIMITED;
        //创建IndexSearch字符流镀锡
        IndexSearcher indexSearcher = new IndexSearcher(directory);
        //根据关键字，去索引库中的词汇表搜索
        /**
         * 创建查询解析器对象
         * 参数1： 分词器版本，推荐使用最高版本
         * 参数2： 对document对象的哪个属性进行搜索
         * 参数3： 分词器
         */
        QueryParser queryParser = new QueryParser(version, "xcontent", analyzer);
        //创建对象封装查询关键字
        Query query = queryParser.parse(keywords);
        /**
         * 参数1： 表示封装关键字对象，其他QueryParse表示查询解析器
         * 参数2：表示如果根据关键字的内容过多，只取前多少个，不足以实际为准
         */
        TopDocs topDocs = indexSearcher.search(query, 100);
        //迭代输出
        for(int i = 0; i < topDocs.scoreDocs.length; i++) {
            //取出封装编号和分数的对象
            ScoreDoc scoreDoc = topDocs.scoreDocs[i];
            //取出每一个编号
            int no = scoreDoc.doc;
            //根据标号去索引库中的原始记录表中查询对应点的document对象
            Document document = indexSearcher.doc(no);
            //获取document对象中的三个属性值
            String xid = document.get("xid");
            String xtitle = document.get("xtitle");
            String xcontent = document.get("xcontent");
            //封装到article对象
            Article article = new Article(Integer.parseInt(xid), xtitle, xcontent);
            //将article对象加入到集合中
            articleList.add(article);
        }
        articleList.forEach(System.out::println);
    }
}
