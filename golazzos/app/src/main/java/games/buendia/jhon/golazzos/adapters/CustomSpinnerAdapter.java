package games.buendia.jhon.golazzos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import games.buendia.jhon.golazzos.R;

/**
 * Created by User on 07/02/2016.
 */
public class CustomSpinnerAdapter extends BaseAdapter {

    private LayoutInflater inflator;
    private String[] mCounting;

    public CustomSpinnerAdapter(Context context, String[] counting){
        inflator = LayoutInflater.from(context);
        mCounting = counting;
    }

    @Override
    public int getCount() {
        return mCounting.length;
    }

    @Override
    public Object getItem(int position) {
        return mCounting[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflator.inflate(R.layout.adapter_spinner, null);
        TextView tv = (TextView) convertView.findViewById(R.id.textView);
        tv.setText(mCounting[position]);
        return convertView;
    }
}
