package edu.gduf.lucene.demo3.entity;

/**
 * 文章
 * @author ZGJ
 * @date 2017/8/30 21:42
 **/
public class Article {
    /**
     * 编号
     */
    private Integer id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 字数
     */
    private int count;

    public Article() {
    }

    public Article(Integer id, String title, String content, int count) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.count = count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", count=" + count +
                '}';
    }
}
