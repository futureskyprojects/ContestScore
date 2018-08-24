package futuresky.projects.tracnghiem.chamthitracnghiem.NhapDapAn.Hand;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import futuresky.projects.tracnghiem.chamthitracnghiem.R;

public class DapAn_Holder extends RecyclerView.ViewHolder {
    CardView cv;
    TextView n;
    TextView a;
    TextView b;
    TextView c;
    TextView d;
    TextView end;
    public DapAn_Holder(View itemView) {
        super(itemView);
        this.n = (TextView) itemView.findViewById(R.id.number);
        this.a = (TextView) itemView.findViewById(R.id.a);
        this.b = (TextView) itemView.findViewById(R.id.b);
        this.c = (TextView) itemView.findViewById(R.id.c);
        this.d = (TextView) itemView.findViewById(R.id.d);
        this.end = (TextView) itemView.findViewById(R.id.hand_row_end);
    }
}
