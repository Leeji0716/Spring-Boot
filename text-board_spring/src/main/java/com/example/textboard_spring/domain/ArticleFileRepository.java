package com.example.textboard_spring.domain;

import com.example.textboard_spring.base.CommonUtil;
import com.example.textboard_spring.domain.Article;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ArticleFileRepository implements Repository {
    private int latestId = 1;
    private ArrayList<Article> articleList = new ArrayList<>(); // 저장소

    public void makeTestData() {
        System.out.println("테스트 데이터 생성하지 않습니다.");
    }
    public ArticleFileRepository() {
        this.articleList = loadPostsFromFile("article.json");

        if (articleList.size() == 0) {
            latestId = 0;
            return;
        }

        int index = this.articleList.size() - 1; // 개수 - 1. 마지막 인덱스
        Article article = articleList.get(index);
        latestId = article.getId();
    }

    private ArrayList<Article> loadPostsFromFile(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // 파일로부터 Post 객체 리스트를 읽어옵니다.
            return mapper.readValue(new File(filePath), new TypeReference<ArrayList<Article>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("파일을 읽는 도중 오류가 발생했습니다: " + e.getMessage());
            // 파일 읽기 실패 시 빈 리스트를 반환합니다.
            return new ArrayList<>();
        }
    }


    public Article saveArticle(String title, String body) {
        // 번호는 latestId, 제목이 title, 내용이 body, 조회수 0, 등록날짜 현재시간인 게시물을
        // json 파일로 저장

        CommonUtil commonUtil = new CommonUtil();

        latestId++;

        Article a1 = new Article(latestId, title, body, 0, commonUtil.getCurrentDateTime());
        articleList.add(a1);

        ObjectMapper mapper = new ObjectMapper();

        try {
            // 객체를 JSON 형태로 변환하여 파일에 저장
            mapper.writeValue(new File("article.json"), articleList);
            System.out.println("객체가 JSON 형태로 파일에 저장되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return a1;
    }

    public ArrayList<Article> findAll() {
        // json 파일을 읽어와서 ArrayList로 반환

        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Article> articleList = new ArrayList<>();

        try {
            // JSON 파일로부터 Post 객체의 리스트를 읽어옵니다.
            articleList = mapper.readValue(new File("article.json"), new TypeReference<ArrayList<Article>>() {
            });
            return articleList;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return articleList;

    }

    public com.example.textboard_spring.domain.Article findArticleById(int id) {
        // id에 해당하는 게시물(article) 반환
        for (Article article : articleList) {
            if (article.getId() == id) {
                // 일치하는 id를 가진 Article을 찾으면 반환
                return article;
            }
        }
        // 일치하는 Article이 없을 경우 null 반환
        return null;
    }

    public void deleteArticle(Article article) {
        // 매개변수로 받은 article 객체를 articleList에서 삭제
        articleList.remove(article);

        // 변경된 articleList를 다시 파일에 저장하는 로직을 추가할 수 있습니다.
        // 이는 저장소의 일관성을 유지하는 데 도움이 됩니다.
        ObjectMapper mapper = new ObjectMapper();
        try {
            // 객체를 JSON 형태로 변환하여 파일에 저장
            mapper.writeValue(new File("article.json"), articleList);
            System.out.println("게시물이 삭제되고 JSON 파일이 업데이트 되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("파일 저장 중 오류가 발생했습니다: " + e.getMessage());
        }

    }

    public void updateArticle(Article article, String title, String body) {
        // todo5 - 매개변수로 받은 article 객체의 제목과 내용을 변경
        Article target = findArticleById(article.getId());

        if (target != null) {
            target.setTitle(title);
            target.setBody(body);
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            // 객체를 JSON 형태로 변환하여 파일에 저장
            mapper.writeValue(new File("article.json"), articleList);
            System.out.println("게시물이 삭제되고 JSON 파일이 업데이트 되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("파일 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public ArrayList<com.example.textboard_spring.domain.Article> findArticleByKeyword(String keyword) {
        // todo6 - 매개변수로 받은 keyword를 포함하는 제목을 가진 게시물을 찾아서 반환
        ArrayList<Article> searchedList = new ArrayList<>();

        for (Article article : articleList) {
            if (article.getTitle().contains(keyword)) {
                searchedList.add(article);
            }
        }

        return searchedList;
    }

}
