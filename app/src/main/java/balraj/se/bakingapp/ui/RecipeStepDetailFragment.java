package balraj.se.bakingapp.ui;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import balraj.se.bakingapp.model.Step;
import balraj.se.bakingapp.R;
import balraj.se.bakingapp.utils.NetworkConnectivityUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A fragment representing a single Recipe detail screen.
 * in two-pane mode (on tablets) or a {@link RecipeStepDetailActivity}
 * on handsets.
 */
public class RecipeStepDetailFragment extends Fragment {

    public static final String ARG_ITEM = "item_id";
    public static final String STEP_INDEX_KEY = "index";
    public static final String STEP_SIZE_KEY = "size";
    public static final String TWO_PANE_KEY = "two_pane";
    private static final String EXO_PLAYER_USER_AGENT = "exoplayer-codelab";
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;
    Button prevBtn;
    Button nextBtn;
    @BindView(R.id.step_desc_scroll_view)
    ScrollView scrollView;
    @BindView(R.id.recipe_step_detail)
    TextView recipeStepDetailTv;
    @BindView(R.id.video_thumbnail_iv)
    ImageView thumbnailIv;

    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer simpleExoPlayer;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private ComponentListener componentListener;
    private ProgressBar progressBar;
    private Step mStep;
    private OnStepDetailItemClick onStepDetailItemClick;
    private int stepIndex;
    private int stepListSize;
    private boolean mTwoPane;
    private static final String PLAYBACK_POS = "playback_pos";
    private static final String PLAY_WHEN_READY = "play_when_ready";
    private static final String CURRENT_WINDOW = "curr_window";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle args = getArguments();
        assert args != null;
        //get data from bundle sent by calling activity
        if (args.containsKey(STEP_INDEX_KEY)) {
            stepIndex = args.getInt(STEP_INDEX_KEY);
        }
        if (args.containsKey(STEP_SIZE_KEY)) {
            stepListSize = args.getInt(STEP_SIZE_KEY);
        }
        if (args.containsKey(TWO_PANE_KEY)) {
            mTwoPane = args.getBoolean(TWO_PANE_KEY);
        }
        if (args.containsKey(ARG_ITEM)) {
            mStep = args.getParcelable(ARG_ITEM);
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (activity != null) {
                android.support.v7.app.ActionBar actionBar = activity.getSupportActionBar();
                //change title only if no two pane layout
                if (actionBar != null && !mTwoPane) {
                    actionBar.setTitle("");
                }
            }
        }


    }

    @SuppressLint("CheckResult")
    private void setRecipeThumbnail(String imageUrl) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(ContextCompat.getDrawable(getContext(), R.drawable.recipe_fallback_drawable));

        //load image using Glide
        Glide.with(getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(imageUrl)
                .into(thumbnailIv);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);
        ButterKnife.bind(this, rootView);
        if (mStep != null) {
            if (rootView.findViewById(R.id.recipe_step_detail) != null)
                ((TextView) rootView.findViewById(R.id.recipe_step_detail)).setText(mStep.getDescription());
            prevBtn = rootView.findViewById(R.id.prev_btn);
            nextBtn = rootView.findViewById(R.id.next_btn);
            onStepDetailItemClick = getActivity() instanceof RecipeDetailActivity ?
                    (RecipeDetailActivity) getActivity() : (RecipeStepDetailActivity) getActivity();
            simpleExoPlayerView = rootView.findViewById(R.id.exo_player_view);
            progressBar = rootView.findViewById(R.id.exo_progress_bar);

            //check network connectivity
            if (!NetworkConnectivityUtil.isNetworkAvailable(getContext())) {
                emptyLayout.setVisibility(View.VISIBLE);
            }
            //make buttons invisible if not present in current layout set up
            if ((stepIndex == 0) && (prevBtn != null)) {
                prevBtn.setVisibility(View.INVISIBLE);
            }
            if ((stepIndex == stepListSize - 1) && nextBtn != null) {
                nextBtn.setVisibility(View.INVISIBLE);
            }
            //open previous step
            if (prevBtn != null) {
                prevBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if ((stepIndex - 1) < 0) {
                            Toast.makeText(getContext(), R.string.already_first_item, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        onStepDetailItemClick.onNextPrevClick(stepIndex - 1);
                    }
                });
            }

            //open next step
            if (nextBtn != null) {
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if ((stepIndex + 1) >= stepListSize) {
                            Toast.makeText(getContext(), R.string.already_last_item, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        onStepDetailItemClick.onNextPrevClick(stepIndex + 1);
                    }
                });
            }

            if(TextUtils.isEmpty(mStep.getThumbnailURL())) {
                thumbnailIv.setVisibility(View.GONE);
            } else {
                setRecipeThumbnail(mStep.getThumbnailURL());
            }

            //restore exoplayer view state
            if(savedInstanceState != null) {
                playbackPosition = savedInstanceState.getLong(PLAYBACK_POS);
                playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
            }
        }
        //listen for exo player changes
        componentListener = new ComponentListener();
        return rootView;
    }

    //retry network check
    @OnClick(R.id.retry_btn)
    public void onClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        if (NetworkConnectivityUtil.isNetworkAvailable(getContext())) {
            emptyLayout.setVisibility(View.INVISIBLE);
            initializePlayer();
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    //initialise exo player
    private void initializePlayer() {
        handleExoPlayerCheck();
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        simpleExoPlayer.addListener(componentListener);
        simpleExoPlayerView.setPlayer(simpleExoPlayer);
        Uri uri = Uri.parse(mStep.getVideoURL());
        MediaSource mediaSource = buildMediaSource(uri);
        simpleExoPlayer.prepare(mediaSource, false, false);
        simpleExoPlayer.seekTo(playbackPosition);
        simpleExoPlayer.setPlayWhenReady(playWhenReady);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(EXO_PLAYER_USER_AGENT)).
                createMediaSource(uri);
    }

    //helper function to handle exo player view
    private void handleExoPlayerCheck() {
        //hide exo player on no network and show retry button
        if (!NetworkConnectivityUtil.isNetworkAvailable(getContext())) {
            simpleExoPlayerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            if (emptyLayout == null) {
                return;
            }
            emptyLayout.setVisibility(View.VISIBLE);
        }
        //if video url is empty show only description and notify user using snackbar
        else if (TextUtils.isEmpty(mStep.getVideoURL())) {
            simpleExoPlayerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.INVISIBLE);
            Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.step_detail_layout_container),
                    R.string.no_video_url_notify, Snackbar.LENGTH_LONG);
            snackbar.show();
            scrollView.setVisibility(View.VISIBLE);
            recipeStepDetailTv.setText(mStep.getDescription());
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && !mTwoPane) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && !mTwoPane)
            hideSystemUi();
        if ((Util.SDK_INT <= 23 || simpleExoPlayer == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(simpleExoPlayer != null) {
            playbackPosition = Math.max(simpleExoPlayer.getCurrentPosition(), playbackPosition);
            outState.putLong(PLAYBACK_POS, playbackPosition);
            outState.putBoolean(PLAY_WHEN_READY, playWhenReady);
        }

    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        simpleExoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            playbackPosition = simpleExoPlayer.getCurrentPosition();
            currentWindow = simpleExoPlayer.getCurrentWindowIndex();
            playWhenReady = simpleExoPlayer.getPlayWhenReady();
            simpleExoPlayer.removeListener(componentListener);
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    public interface OnStepDetailItemClick {
        void onNextPrevClick(int position);
    }

    private class ComponentListener extends Player.DefaultEventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady,
                                         int playbackState) {
            switch (playbackState) {
                case Player.STATE_IDLE:
                    break;
                case Player.STATE_BUFFERING:
                    if (progressBar != null && (mStep != null && !TextUtils.isEmpty(mStep.getVideoURL())))
                        progressBar.setVisibility(View.VISIBLE);
                    break;
                case Player.STATE_READY:
                    if (progressBar != null)
                        progressBar.setVisibility(View.INVISIBLE);
                    break;
                case Player.STATE_ENDED:
                    progressBar.setVisibility(View.INVISIBLE);
                    break;
                default:
                    break;
            }
        }
    }


}
