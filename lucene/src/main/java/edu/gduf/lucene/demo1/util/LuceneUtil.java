package edu.gduf.lucene.demo1.util;

import edu.gduf.lucene.demo1.entity.Article;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * lucene工具类，单例类
 * @author ZGJ
 * @date 2017/8/31 0:08
 **/
public class LuceneUtil {


    private static Directory directory;
    private static Version version;
    private static Analyzer analyzer;
    private static IndexWriter.MaxFieldLength maxFieldLength;

    static {
        try {
            directory = FSDirectory.open(new File("f:\\indexDB"));
            version = Version.LUCENE_30;
            analyzer = new StandardAnalyzer(version);
            maxFieldLength = IndexWriter.MaxFieldLength.LIMITED;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    private LuceneUtil() {}

    public static Directory getDirectory() {
        return directory;
    }

    public static void setDirectory(Directory directory) {
        LuceneUtil.directory = directory;
    }

    public static Version getVersion() {
        return version;
    }

    public static void setVersion(Version version) {
        LuceneUtil.version = version;
    }

    public static Analyzer getAnalyzer() {
        return analyzer;
    }

    public static void setAnalyzer(Analyzer analyzer) {
        LuceneUtil.analyzer = analyzer;
    }

    public static IndexWriter.MaxFieldLength getMaxFieldLength() {
        return maxFieldLength;
    }

    public static void setMaxFieldLength(IndexWriter.MaxFieldLength maxFieldLength) {
        LuceneUtil.maxFieldLength = maxFieldLength;
    }

    /**
     * javabean转document
     * @param object
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Document javaBean2Document(Object object) throws Exception {
        Document document = new Document();
        /**
         * 通过反射获取对象的属性
         */
        Class clazz = object.getClass();
        //为了和lucene包下的Field区分
        java.lang.reflect.Field[] reflectFields = clazz.getDeclaredFields();
        for(java.lang.reflect.Field reflectField : reflectFields) {
            reflectField.setAccessible(true);
            String name  = reflectField.getName();
            String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
            Method method = clazz.getMethod(methodName);
            String value = method.invoke(object).toString();

            //加入到Document对象中
            document.add(new Field(name, value, Field.Store.YES, Field.Index.ANALYZED));
        }
        return document;
    }

    /**
     * document转javabean
     * @param document
     * @param clazz
     * @return
     */
    public static Object document2JavaBean(Document document, Class clazz) throws Exception{
        Object object = clazz.newInstance();
        java.lang.reflect.Field[] reflectFields = clazz.getDeclaredFields();
        for(java.lang.reflect.Field reflectField : reflectFields) {
            reflectField.setAccessible(true);
            String name  = reflectField.getName();
            String value = document.get(name);
            BeanUtils.setProperty(object, name, value);
        }
        return object;
    }

    public static void main(String[] args) throws Exception {
        Article article = new Article(1, "电商", "高并发分布式电商网站");
        Document document = javaBean2Document(article);
        System.out.println(document.get("title"));
        Article article1 = (Article) document2JavaBean(document, Article.class);
        System.out.println(article1.getTitle());
    }
}
