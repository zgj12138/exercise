package edu.gduf.lucene.demo3.entity;

import edu.gduf.lucene.demo1.entity.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于Article的分页类
 * @author ZGJ
 * @date 2017/9/3 22:36
 **/
public class Page {
    //当前页数
    private Integer curPageNO;
    //每页显示记录数,默认是2
    private Integer perPageSize = 2;
    //总记录数
    private Integer allRecordNO;
    //总页数
    private Integer allPageNO;
    //总页数
    private List<Article> articleList = new ArrayList<>();

    public Integer getCurPageNO() {
        return curPageNO;
    }

    public void setCurPageNO(Integer curPageNO) {
        this.curPageNO = curPageNO;
    }

    public Integer getPerPageSize() {
        return perPageSize;
    }

    public void setPerPageSize(Integer perPageSize) {
        this.perPageSize = perPageSize;
    }

    public Integer getAllRecordNO() {
        return allRecordNO;
    }

    public void setAllRecordNO(Integer allRecordNO) {
        this.allRecordNO = allRecordNO;
    }

    public Integer getAllPageNO() {
        return allPageNO;
    }

    public void setAllPageNO(Integer allPageNO) {
        this.allPageNO = allPageNO;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    @Override
    public String toString() {
        return "Page{" +
                "curPageNO=" + curPageNO +
                ", perPageSize=" + perPageSize +
                ", allRecordNO=" + allRecordNO +
                ", articleList=" + articleList +
                '}';
    }
}
