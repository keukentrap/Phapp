package haakjeopenen.phapp.fragments.photos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import haakjeopenen.phapp.R;
import haakjeopenen.phapp.models.Photo;
import haakjeopenen.phapp.net.API;
import haakjeopenen.phapp.util.Notify;
import haakjeopenen.phapp.widgets.MultiSwipeRefreshLayout;

/**
 * Simple photo gallery
 * features:
 * - RecyclerViewAdapter
 * - Zoom in on photos
 */
public class PhotosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, PhotoZoomListener, Notify {

    ArrayList<Photo> images = new ArrayList<>();

    /**
     * Hold a reference to the current animator, so that it can be canceled mid-way.
     */
    private Animator mCurrentAnimator;

    /**
     * The system "short" animation time duration, in milliseconds. This duration is ideal for
     * subtle animations or animations that occur very frequently.
     */
    private int mShortAnimationDuration;

    private API api;
    private PhotoAdapter imageadapter;
    private GridView mPhotosGridview;
    private MultiSwipeRefreshLayout multiSwipeRefreshLayout;
    private ProgressBar mLoadingBar;

    public PhotosFragment() {
        api = API.getInstance(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        multiSwipeRefreshLayout = (MultiSwipeRefreshLayout) view;
        multiSwipeRefreshLayout.setSwipeableChildren(R.id.photosGridView);
        multiSwipeRefreshLayout.setOnRefreshListener(this);

        mLoadingBar = (ProgressBar) view.findViewById(R.id.loading);

        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        mPhotosGridview = (GridView) view.findViewById(R.id.photosGridView);

        //check if this is the first time loading
        if (imageadapter == null) {
            images = new ArrayList<>();
            imageadapter = new PhotoAdapter(getActivity(), images, this);
            api.loadPhotos(images, this);


        } else {
            notifyFinished();
        }

        return view;
    }

    public void notifyUpdate() {
        //System.out.println("notified UpdatePhotos");
        mPhotosGridview.setAdapter(imageadapter);
    }

    @Override
    public void notifyFinished() {
        multiSwipeRefreshLayout.setRefreshing(false);
        mLoadingBar.setVisibility(View.GONE);
        mPhotosGridview.setAdapter(imageadapter);
    }

    @Override
    public void onRefresh() {
        System.out.println("Refreshing");
        mLoadingBar.setVisibility(View.GONE);

        //TODO can we comment this out?
        ArrayList<Photo> list = new ArrayList<>();
        imageadapter = new PhotoAdapter(getActivity(), list, this);

        API.getInstance().loadPhotos(list, this);
    }


    /**
     * huge method copied from the Android Developer site to animate the zoom in function
     *
     * @param thumbView
     * @param photo
     */
    private void zoomImageFromThumb(final View thumbView, Photo photo) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) this.getView().findViewById(
                R.id.expanded_image);

        String imgurl = photo.imgurl;
        System.out.println(imgurl);
        try {
            Picasso.with(getActivity()).load(imgurl).into(expandedImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
//		expandedImageView.setImageResource(R.drawable.sample_4);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        getView().findViewById(R.id.swipe_container_photos)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });

        AnimatorSet darkenBackground = new AnimatorSet();
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
//
//			darkenBackground.play(ObjectAnimator.ofFloat(expandedImageView, View.ALPHA, 0.0f, 1.0f)).after(set);
//		}

        int colorFrom = Color.argb(0, 0, 0, 0);
        int colorTo = Color.argb(255, 0, 0, 0);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);

        colorAnimation.setDuration(250); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                expandedImageView.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        darkenBackground.play(colorAnimation).after(set);
        darkenBackground.start();
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                expandedImageView.setBackgroundColor(Color.argb(0, 0, 0, 0));
                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

    @Override
    public void onPhotoZoom(View thumbView, Photo photo) {
        zoomImageFromThumb(thumbView, photo);

    }
}
