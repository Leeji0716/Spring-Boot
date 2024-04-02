package com.example.textboard_spring.domain;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class ArticleController {
    Repository articleRepository = new ArticleMySQLRepository();


    @RequestMapping("/search")
    public String search(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                         Model model){
        ArrayList<Article> searchedList = articleRepository.findArticleByKeyword(keyword);
        model.addAttribute("aritlcleList", searchedList);
        return "list";
    }

    @RequestMapping("/detail/{id}")
    public String detail(@PathVariable("id") int id, Model model) {

        Article article = articleRepository.findArticleById(id);

        if (article == null) {
            return "없는 게시물입니다.";
        }
        article.increaseHit();
        model.addAttribute("article", article);
        return "detail";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable ("id") int id) {
        Article article = articleRepository.findArticleById(id);
        if (article == null) {
            return "없는 게시물입니다.";
        }
        articleRepository.deleteArticle(article);
        return "redirect:/list";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") int id,
                         @RequestParam("title") String title,
                         @RequestParam("body") String body){
        Article article = articleRepository.findArticleById(id);
        if (article == null) {
            throw new RuntimeException("없는 게시물입니다.");
        }
        articleRepository.updateArticle(article, title, body);
        return "redirect:/detail/%d".formatted(id);
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") int id, Model model){
        Article article = articleRepository.findArticleById(id);
        if (article == null) {
            throw new RuntimeException("없는 게시물입니다.");
        }
        model.addAttribute("article", article);
        return "updateForm";
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
        // list(html) 자체가 articleList를 받아서 실행되기 때문에 add에서도 받아야함
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
