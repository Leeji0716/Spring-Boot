package com.example.textboard_spring.domain;

import com.example.textboard_spring.domain.Article;

import java.util.ArrayList;

// interface 모든 메서드가 추상메서드이면 인터페이스로 사용 가능
public interface Repository {
    void makeTestData();
    ArrayList<com.example.textboard_spring.domain.Article> findArticleByKeyword(String keyword);
    com.example.textboard_spring.domain.Article findArticleById(int id);
    void deleteArticle(Article article);

    void updateArticle(Article article, String newTitle, String newBody);
    ArrayList<Article> findAll();
    Article saveArticle(String title, String body);


}
