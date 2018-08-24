package futuresky.projects.tracnghiem.chamthitracnghiem.Introduce;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;

import futuresky.projects.tracnghiem.chamthitracnghiem.MainActivity;
import futuresky.projects.tracnghiem.chamthitracnghiem.R;

public class IntroFragment extends android.support.v4.app.Fragment {

    private int mPage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getArguments().containsKey("page"))
            throw new RuntimeException("Không có số hiệu của mảnh giới thiệu!");
        mPage = getArguments().getInt("page");
    }

    public static IntroFragment newInstance(int page)
    {
        IntroFragment fragment = new IntroFragment();
        Bundle b = new Bundle();
        b.putInt("page", page);
        fragment.setArguments(b);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutResId;
        switch (mPage)
        {
            case 0:
                layoutResId = R.layout.fragment_intro1;
                break;
            case 1:
                layoutResId = R.layout.fragment_intro2;
                break;
            case 2:
                layoutResId = R.layout.fragment_intro3;
                break;
            case 3:
                layoutResId = R.layout.fragment_intro4;
                break;
            case 4:
                layoutResId = R.layout.fragment_intro5;
                break;
            default:
                layoutResId = R.layout.fragment_intro6;
                break;

        }
        View view = getActivity().getLayoutInflater().inflate(layoutResId, container, false);
        MaterialRippleLayout x = (MaterialRippleLayout) view.findViewById(R.id.finishButton);
        if (x!=null)
        {
            x.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myAc = new Intent(getContext(), MainActivity.class);
                    startActivity(myAc);
                    getActivity().finish();
                    Toast.makeText(getContext(), "Kết thúc phần giới thiệu!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        view.setTag(mPage);
        return view;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
