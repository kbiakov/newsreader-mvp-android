package io.github.kbiakov.newsreader.api.providers;

import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import io.github.kbiakov.newsreader.api.ApiService;
import io.github.kbiakov.newsreader.models.entities.Source;
import io.github.kbiakov.newsreader.models.json.SourceJson;
import io.github.kbiakov.newsreader.models.response.SourcesResponse;
import io.reactivex.Observable;

public class SourcesProvider {

    private ApiService service;

    @Inject
    SourcesProvider(ApiService service) {
        this.service = service;
    }

    public Observable<List<Source>> getSources() {
        return service
                .getSources(null, null, null)
                .onErrorReturn(t -> {
                    if (t instanceof UnknownHostException) { // no internet
                        return SourcesResponse.empty();
                    }
                    return SourcesResponse.invalid(t);
                })
                .map(SourcesResponse::getData)
                .map(SourceJson::asEntities)
                .toObservable();
    }
}
