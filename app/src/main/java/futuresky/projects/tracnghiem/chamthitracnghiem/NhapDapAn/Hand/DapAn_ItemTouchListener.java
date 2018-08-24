package futuresky.projects.tracnghiem.chamthitracnghiem.NhapDapAn.Hand;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class DapAn_ItemTouchListener implements RecyclerView.OnItemTouchListener{
    DapAn_RecycleViewItemClickListener clickListener;
    GestureDetector gestureDetector;
    public DapAn_ItemTouchListener(Context myContext, final RecyclerView recyclerView, final DapAn_RecycleViewItemClickListener clickListener)
    {
        this.clickListener = clickListener;
        this.gestureDetector = new GestureDetector(myContext, new GestureDetector.SimpleOnGestureListener()
        {
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (!(child == null || this.clickListener == null || !this.gestureDetector.onTouchEvent(e))) {
            this.clickListener.onClick(child, rv.getChildLayoutPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
