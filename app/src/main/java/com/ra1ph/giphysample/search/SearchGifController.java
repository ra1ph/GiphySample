package com.ra1ph.giphysample.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.conductor.RouterTransaction;
import com.ra1ph.giphysample.GiphyApplication;
import com.ra1ph.giphysample.R;
import com.ra1ph.giphysample.base.ButterKnifeController;
import com.ra1ph.giphysample.search.adapter.SearchGifAdapter;
import com.ra1ph.giphysample.search.domain.GifModel;
import com.ra1ph.giphysample.watch.WatchGifController;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Mikhail Korshunov on 09.08.17.
 */

public class SearchGifController extends ButterKnifeController implements SearchGifContract.View, SearchGifAdapter.OnGifClickListener {
    private static final int GRID_SPAN_COUNT = 4;

    @BindView(R.id.search_controller_search_view)
    SearchView searchView;
    @BindView(R.id.search_controller_recycler_view)
    RecyclerView gridRecyclerView;
    @BindView(R.id.search_controller_empty_view)
    TextView emptyView;
    @BindView(R.id.search_controller_thumbs_up_counter)
    TextView thumbsUpCounter;
    @BindView(R.id.search_controller_thumbs_down_counter)
    TextView thumbsDownCounter;

    @Inject
    SearchGifContract.Presenter presenter;
    @Inject
    Picasso picasso;

    private SearchGifAdapter gifAdapter;
    private PublishSubject<Integer> loadMoreSubject;

    @Override
    protected void onContextAvailable(@NonNull Context context) {
        super.onContextAvailable(context);
        if (presenter == null) {
            setupSearchGifComponent(context);
        }
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.search_controller, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        setupSearchView();
        setupRecyclerView(getActivity());
        presenter.bindView();
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
        presenter.unbindView();
    }

    @Override
    public void onGifClick(GifModel gifModel) {
        presenter.gifSelected(gifModel);
    }

    @Override
    public void showGifs(List<GifModel> gifs) {
        gridRecyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        gifAdapter.replaceItems(gifs);
    }

    @Override
    public void showNothingFound() {
        showEmptyView(R.string.search_controller_nothing_found);
    }

    @Override
    public void showEmptyQuery() {
        showEmptyView(R.string.search_controller_enter_text);
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showUpThumbsCount(long count) {
        thumbsUpCounter.setText(String.format(Locale.getDefault(), "Thumbs up: %d", count));
    }

    @Override
    public void showDownThumbsCount(long count) {
        thumbsDownCounter.setText(String.format(Locale.getDefault(), "Thumbs down: %d", count));
    }

    public void showGif(GifModel gifModel) {
        getRouter().pushController(RouterTransaction.with(new WatchGifController(gifModel)));
    }

    private void setupSearchGifComponent(Context context) {
        GiphyApplication.get(context)
                .getAppComponent()
                .searchGifComponent(new SearchGifModule(this))
                .inject(this);
    }

    private void setupSearchView() {
        PublishSubject<String> querySubject = PublishSubject.create();
        querySubject.skip(1)
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    presenter.seekGif(s);
                    loadMoreSubject.onNext(0);
                    gridRecyclerView.scrollToPosition(0);
                });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                querySubject.onNext(newText);
                return true;
            }
        });
    }

    private void setupRecyclerView(Context context) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, GRID_SPAN_COUNT);
        gridRecyclerView.setLayoutManager(gridLayoutManager);
        gifAdapter = new SearchGifAdapter(picasso, this);
        gridRecyclerView.setAdapter(gifAdapter);

        loadMoreSubject = PublishSubject.create();
        gridRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = gridLayoutManager.getItemCount();
                int lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                if (totalItemCount <= (lastVisibleItem + GRID_SPAN_COUNT)) {
                    loadMoreSubject.onNext(totalItemCount);
                }
            }
        });
        loadMoreSubject.distinctUntilChanged()
                .subscribe(currentCount -> {
                    if (currentCount > 0) {
                        presenter.loadMore();
                    }
                });

    }

    private void showEmptyView(@StringRes int text) {
        emptyView.setVisibility(View.VISIBLE);
        gridRecyclerView.setVisibility(View.GONE);
        emptyView.setText(text);
    }
}
