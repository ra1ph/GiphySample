package com.ra1ph.giphysample.watch;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.ra1ph.giphysample.GiphyApplication;
import com.ra1ph.giphysample.R;
import com.ra1ph.giphysample.base.ButterKnifeController;
import com.ra1ph.giphysample.search.domain.GifModel;
import com.ra1ph.giphysample.utils.BundleBuilder;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Mikhail Korshunov on 10.08.17.
 */

public class WatchGifController extends ButterKnifeController implements WatchGifContract.View {
    private static final String GIF_MODEL_BUNDLE_KEY = "gif_model";
    @BindView(R.id.watch_controller_exoplayer)
    SimpleExoPlayerView exoPlayerView;
    @BindView(R.id.watch_controller_toolbar)
    Toolbar toolbar;
    @BindView(R.id.watch_controller_up_btn)
    Button voteUpBtn;
    @BindView(R.id.watch_controller_down_btn)
    Button voteDownBtn;

    @Inject
    WatchGifContract.Presenter presenter;

    private SimpleExoPlayer exoPlayer;
    private DefaultBandwidthMeter bandwidthMeter;

    public WatchGifController(Bundle args) {
        super(args);
    }

    public WatchGifController(GifModel gifModel) {
        this(new BundleBuilder(new Bundle())
                .putSerializable(GIF_MODEL_BUNDLE_KEY, gifModel)
                .build());
    }

    @Override
    protected void onContextAvailable(@NonNull Context context) {
        super.onContextAvailable(context);
        if (presenter == null) {
            setupWatchGifComponent();
        }
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.watch_controller, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);
        setupPlayer(getActivity());
        setupToolbar();

        presenter.setModel((GifModel) getArgs().getSerializable(GIF_MODEL_BUNDLE_KEY));
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
        exoPlayer.release();
    }

    @OnClick(R.id.watch_controller_up_btn)
    void onVoteUpClick() {
        presenter.thumbUp();
    }

    @OnClick(R.id.watch_controller_down_btn)
    void onVoteDownClick() {
        presenter.thumbDown();
    }

    @Override
    public void playMp4Video(String url) {
        Context context = getActivity();
        if (context == null) {
            return;
        }

        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(context,
                        Util.getUserAgent(context, "GiphySample"),
                        bandwidthMeter);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource videoSource =
                new ExtractorMediaSource(Uri.parse(url),
                        dataSourceFactory,
                        extractorsFactory,
                        null,
                        null);

        exoPlayer.prepare(videoSource);
    }

    @Override
    public void showVote(GifModel.Vote vote) {
        voteUpBtn.setSelected(vote.equals(GifModel.Vote.UP));
        voteDownBtn.setSelected(vote.equals(GifModel.Vote.DOWN));
    }

    private void setupWatchGifComponent() {
        GiphyApplication.get(getActivity())
                .getAppComponent()
                .watchGifComponent(new WatchGifModule(this))
                .inject(this);
    }

    private void setupToolbar() {
        toolbar.setTitle(R.string.watch_gif_controller_title);
    }

    private void setupPlayer(Context context) {
        bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        exoPlayer =
                ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        exoPlayerView.setPlayer(exoPlayer);

        exoPlayerView.setUseController(false);
        exoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        exoPlayer.setPlayWhenReady(true);

    }
}
