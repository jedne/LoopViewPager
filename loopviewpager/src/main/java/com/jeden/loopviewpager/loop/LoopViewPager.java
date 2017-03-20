package com.jeden.loopviewpager.loop;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jeden on 2017/3/10.
 */

public class LoopViewPager extends ViewPager {

    public static final Float mMaxScaleOffset = 0.08f;
    private int mCurrentPage;
    private int mOldDataCount;
    private FragmentStatePagerAdapter mAdapter;

    public LoopViewPager(Context context) {
        super(context);
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        super.addOnPageChangeListener(new MyCyclePageChangeListener(listener));
    }

    @Override
    public int getCurrentItem() {
        return changePosition(super.getCurrentItem());
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item + 2);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(new MyCycleOnTouchListener(l));
    }

    @Override
    public int getOffscreenPageLimit() {
        return super.getOffscreenPageLimit();
    }

    @Override
    public void setOffscreenPageLimit(int limit) {
        super.setOffscreenPageLimit(limit + 4);
    }

    public class MyCycleOnTouchListener implements OnTouchListener {
        private OnTouchListener l;

        public MyCycleOnTouchListener(OnTouchListener l) {
            this.l = l;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mCurrentPage = LoopViewPager.super.getCurrentItem();
            }
            if (this.l != null)
                return l.onTouch(v, event);
            return false;
        }
    }

    public class MyCyclePageChangeListener implements OnPageChangeListener {

        OnPageChangeListener listener;
        private int mViewPagerState = 0;

        public MyCyclePageChangeListener(OnPageChangeListener listener) {
            this.listener = listener;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffset == 0 && positionOffsetPixels == 0) {
                if (position == 1 || position == 0) {
                    setCurrentItem(position + mOldDataCount, false);
                    return;
                } else if (position == mOldDataCount + 2) {
                    setCurrentItem(2, false);
                    return;
                } else if(position == mOldDataCount + 3) {
                    setCurrentItem(3, false);
                    return;
                }

                if (mViewPagerState != 1) {
                    scaleFragmentView(mAdapter.getItem(position - 1), 1.0f);
                    scaleFragmentView(mAdapter.getItem(position), 0.0f);
                    scaleFragmentView(mAdapter.getItem(position + 1), 1.0f);
                }
            } else {
                float tempOffset = Math.abs(position - mCurrentPage + positionOffset);
                scaleFragmentView(mAdapter.getItem(mCurrentPage == position ? position + 1 : position), 1 - tempOffset);
                scaleFragmentView(mAdapter.getItem(mCurrentPage), tempOffset);
            }
            if (this.listener != null) {
                this.listener.onPageScrolled(changePosition(position), positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (this.listener != null) {
                this.listener.onPageSelected(changePosition(position));
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mViewPagerState = state;
            if (this.listener != null) {
                this.listener.onPageScrollStateChanged(state);
            }
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        this.mAdapter = (FragmentStatePagerAdapter) adapter;
        mOldDataCount = adapter.getCount() - 4;

        super.setOnTouchListener(new MyCycleOnTouchListener(null));
        super.addOnPageChangeListener(new MyCyclePageChangeListener(null));
        super.setAdapter(adapter);

        setOffscreenPageLimit(mOldDataCount);
        setCurrentItem(0);
    }

    public void scaleFragmentView(Fragment fragment, Float offset) {
        if(fragment == null)
            return;
        View view = fragment.getView();
        if (view == null) {
            return;
        }

        if(offset > 1 || offset < 0){
            return;
        }

        Float scale = 1 - mMaxScaleOffset * offset;

        view.setScaleX(scale);
        view.setScaleY(scale);
    }

    public int changePosition(int position) {
        if (position == 1) {
            return mOldDataCount - 1;
        } else if (position == 0) {
            return mOldDataCount - 2;
        } else if (position == mOldDataCount + 2) {
            return 0;
        } else if (position == mOldDataCount + 3) {
            return 1;
        }
        return position - 2;
    }
}
