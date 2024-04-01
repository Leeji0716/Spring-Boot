package com.example.textboard_spring.domain;

import com.example.textboard_spring.base.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Scanner;

@Controller
public class ArticleController {
    CommonUtil commonUtil = new CommonUtil();
    ArticleRepository articleRepository = new ArticleRepository();

    @RequestMapping("/search")
    @ResponseBody
    public ArrayList<Article> search(@RequestParam (value="keyword", defaultValue = "") String keyword) {
        ArrayList<Article> searchedList = articleRepository.findArticleByKeyword(keyword);
        return searchedList;
    }

    @RequestMapping("/detail")
    @ResponseBody
    public Article detail(@RequestParam ("id") int id) {
        Article article = articleRepository.findArticleById(id);
        if (article == null) {
            return null;
        }
        article.increaseHit();

        // 객체를 -> json 문자열로 변환 -> jackson 라이브러리 사용
//        String jsonString = "";
//        try {
//            // ObjectMapper 인스턴스 생성
//            ObjectMapper objectMapper = new ObjectMapper();
//            // 객체를 JSON 문자열로 변환
//            jsonString = objectMapper.writeValueAsString(article);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return article;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam ("id") int id) {
        Article article = articleRepository.findArticleById(id);
        if (article == null) {
            return "없는 게시물입니다.";
        }
        articleRepository.deleteArticle(article);
        return id + "번 게시물이 삭제되었습니다.";
    }

    @RequestMapping("/update")
    @ResponseBody
    public String update(@RequestParam ("id") int id,
                         @RequestParam("newTitle") String newTitle,
                         @RequestParam("newBody") String newBody) {
        Article article = articleRepository.findArticleById(id);
        if (article == null) {
            return "없는 게시물입니다.";
        }
        articleRepository.updateArticle(article, newTitle, newBody);
        return id + "번 게시물이 수정되었습니다";
    }

    @RequestMapping("/list")
    public String list(Model model) {
        ArrayList<Article> articleList = articleRepository.findAll();
        model.addAttribute("articleList", articleList);
        return "list";
    }

    //입력 후 실제 데이터 저장 부분
    @RequestMapping("/add")
    public String add(@RequestParam("title") String title,
                      @RequestParam("body") String body,
                      Model model) {
//list 자체가 articleList를 받아서 실행되기 때문에 add에서도 받아야함
        articleRepository.saveArticle(title, body);
        ArrayList<Article> articleList = articleRepository.findAll();

        model.addAttribute("articleList", articleList);
        return "list";
    }

    //입력화면 보여주기
    @RequestMapping("/form")
    public String from(){
        return "form";
    }
}
