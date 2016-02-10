package games.buendia.jhon.golazzos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import games.buendia.jhon.golazzos.R;

/**
 * Created by User on 08/01/2015.
 */

public class DrawerAdapterList extends BaseAdapter {

    private Context mContext;
    private String[] mTitles;

    public DrawerAdapterList(Context context, String[] mTitles) {
        mContext = context;
        this.mTitles = mTitles;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return mTitles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.navigation_drawer_list_item,
                parent, false);

        TextView labTitle = (TextView) view
                .findViewById(R.id.drawerMenuItemName);

        labTitle.setText(mTitles[position]);

        return view;
    }

}
