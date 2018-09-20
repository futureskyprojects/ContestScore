package futuresky.projects.tracnghiem.chamthitracnghiem.ThongKe.TiLeTraLoiDung;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import futuresky.projects.tracnghiem.chamthitracnghiem.R;

public class Data1Adapter extends ArrayAdapter<Data1> implements View.OnClickListener {
    Context myContext;
    ArrayList<Data1> ds;
    TextView n;
    TextView dapan[];
    TextView end;
    public Data1Adapter(Context myContext, ArrayList<Data1> ds)
    {
        super(myContext, R.layout.layout_hand_row, ds);
        this.myContext = myContext;
        this.ds = ds;
    }

    @Override
    public Data1 getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(myContext);
        convertView = layoutInflater.inflate(R.layout.layout_hand_row, null);
        // get view
        dapan = new TextView[4];
        n = (TextView) convertView.findViewById(R.id.number);
        dapan[0]  = (TextView) convertView.findViewById(R.id.a);
        dapan[1]  = (TextView) convertView.findViewById(R.id.b);
        dapan[2]  = (TextView) convertView.findViewById(R.id.c);
        dapan[3] = (TextView) convertView.findViewById(R.id.d);
        end  = (TextView) convertView.findViewById(R.id.hand_row_end);
        end.getLayoutParams().width = 150;
        // insert data
        n.setText(Integer.toString(position+1));
        dapan[ds.get(position).truePositon-1].setBackgroundResource(R.drawable.circle2);
        end.setText((Float.toString(((Math.round(ds.get(position).percent*10)/10))*100) + "%").replace(".0",""));
        return convertView;
    }

    @Override
    public void onClick(View view) {

    }
}
