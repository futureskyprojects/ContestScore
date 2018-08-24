package futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.DapAn;

import android.content.Context;
import android.support.v7.view.menu.MenuBuilder;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.DapAn.DapAn;
import futuresky.projects.tracnghiem.chamthitracnghiem.R;

public class DapAnAdapter extends ArrayAdapter<DapAn> implements View.OnClickListener {
    Context myContext;
    private ArrayList<DapAn> dsDapAn;
    TextView SoMaDe;
    public DapAnAdapter(ArrayList<DapAn> dsDapAn, Context myContext)
    {
        super(myContext, R.layout.item_dapan,dsDapAn);
        this.dsDapAn = dsDapAn;
        this.myContext = myContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(myContext);
        convertView = inflater.inflate(R.layout.item_dapan, parent, false);
        SoMaDe = (TextView) convertView.findViewById(R.id.made_showra);
        SoMaDe.setText(dsDapAn.get(position).getMaDe());
        return convertView;
    }

    @Override
    public void onClick(View view) {
    }
}
