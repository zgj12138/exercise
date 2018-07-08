package edu.gduf.lucene.demo2.servlet;

import edu.gduf.lucene.demo2.entity.Page;
import edu.gduf.lucene.demo2.service.ArticleService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ZGJ
 * create on 2018/5/5 17:45
 **/
public class ArticleServlet extends HttpServlet {

    ArticleService articleService = new ArticleService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");

        String keywords = req.getParameter("keywords");
        if(keywords == null || keywords.trim().length() == 0) {
            keywords = "电商";
        }

        String curPage = req.getParameter("curPage");
        if(curPage == null || curPage.trim().length() == 0) {
            curPage = "1";
        }

        try {
            Page page = articleService.show(keywords, Integer.parseInt(curPage));
            req.setAttribute("page", page);
            req.setAttribute("keywords", keywords);
            req.getRequestDispatcher("/list.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
