package io.github.kbiakov.newsreader.screens.articles;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import io.github.kbiakov.newsreader.models.Article;

interface ArticlesView extends MvpLceView<List<Article>> {
    void showArticle(String articleUrl);
}
