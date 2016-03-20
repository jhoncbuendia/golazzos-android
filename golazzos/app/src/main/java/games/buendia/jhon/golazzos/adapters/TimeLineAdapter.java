package games.buendia.jhon.golazzos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.model.Story;

/**
 * Created by User on 27/02/2016.
 */
public class TimeLineAdapter extends BaseAdapter {

    private ArrayList<Story> storyArrayList;
    private Context context;
    private ListView listViewStorys;
    private LayoutInflater layoutInflater;

    public TimeLineAdapter(ArrayList<Story> storyArrayList, Context context, ListView listViewStorys) {
        this.storyArrayList = storyArrayList;
        this.context = context;
        this.listViewStorys = listViewStorys;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return storyArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return storyArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final ViewHolder holder;
        View v = listViewStorys.getChildAt(position - listViewStorys.getFirstVisiblePosition());

        if (v == null) {

            v = layoutInflater.inflate(R.layout.row_timeline, null);
            holder = new ViewHolder();
            holder.descriptionTextView = (TextView) v.findViewById(R.id.textViewDescripcion);
            holder.timeAgoTextView = (TextView) v.findViewById(R.id.textViewTiempoStory);
            holder.imageViewIconRow = (ImageView) v.findViewById(R.id.imageViewIcono);
            holder.progressBarLoaderImage = (ProgressBar) v.findViewById(R.id.progressBarImagenLoader);

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        final Story match = storyArrayList.get(position);

        holder.descriptionTextView.setText(match.getDescription());
        holder.timeAgoTextView.setText(match.getTimeAgo());

        String urlImage = match.getUrlImage();

        Picasso.with(context)
                .load(urlImage)
                .into(holder.imageViewIconRow, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBarLoaderImage.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        holder.progressBarLoaderImage.setVisibility(View.GONE);
                    }
                });

        return v;
    }

    static class ViewHolder {
        TextView timeAgoTextView;
        TextView descriptionTextView;
        ImageView imageViewIconRow;
        ProgressBar progressBarLoaderImage;
    }
}