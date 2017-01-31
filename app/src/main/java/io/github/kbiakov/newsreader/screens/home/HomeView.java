package io.github.kbiakov.newsreader.screens.home;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import io.github.kbiakov.newsreader.models.entities.Source;

public interface HomeView extends MvpLceView<List<Source>> {
    void listArticles(String sourceId);
}
