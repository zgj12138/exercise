package edu.gduf.lucene.demo2.service;

import edu.gduf.lucene.demo1.entity.Article;
import edu.gduf.lucene.demo2.entity.Page;
import edu.gduf.lucene.demo2.dao.ArticleDao;

import java.util.List;

/**
 * 业务层
 *
 * @author ZGJ
 * @date 2017/9/3 22:39
 **/
public class ArticleService {
    private ArticleDao articleDao = new ArticleDao();

    /**
     * 根据关键字和页数，查询内容
     *
     * @param keywords
     * @param curPage
     * @return
     * @throws Exception
     */
    public Page show(String keywords, int curPage) throws Exception {
        Page page = new Page();
        page.setCurPageNO(curPage);
        page.setPerPageSize(2);
        int allRecordNO = articleDao.getAllRecord(keywords);
        page.setAllRecordNO(allRecordNO);

        int pageNO = 0;
        if (page.getAllRecordNO() % page.getPerPageSize() == 0) {
            pageNO = page.getAllRecordNO() / page.getPerPageSize();
        } else {
            pageNO = page.getAllRecordNO() / page.getPerPageSize() + 1;
        }
        page.setAllPageNO(pageNO);

        int size = page.getCurPageNO();
        int start = (page.getCurPageNO() - 1) * size;
        List<Article> articles = articleDao.findAll(keywords, start, size);
        page.setArticleList(articles);
        return page;
    }

    public static void main(String[] args) throws Exception {
        ArticleService articleService = new ArticleService();
        Page page = articleService.show("电商", 1);
        System.out.println(page);
    }
}
