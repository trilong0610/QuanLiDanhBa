package com.example.huynh.danhba;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DanhBaAdapter extends BaseAdapter{
    private Context context;
    private int layout;
    private List<DanhBa>mdanhBaList;
    private LayoutInflater inflater;
    private ArrayList<DanhBa> arraydanhba;
    private Typeface tf;

    public DanhBaAdapter(Context context, int layout, List<DanhBa> danhBaList) {
        this.context = context;
        this.layout = layout;
        this.mdanhBaList = danhBaList;
        this.inflater = LayoutInflater.from(context);
        this.arraydanhba = new ArrayList<DanhBa>();
        this.arraydanhba.addAll(mdanhBaList);

    }

    @Override
    public int getCount() {
        return mdanhBaList.size();
    }

    @Override
    public DanhBa getItem(int i) {
        return mdanhBaList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if(view==null)
        {
            view = inflater.inflate(R.layout.dong_danh_ba,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.ivHinh = (ImageView)view.findViewById(R.id.imageview1);
            viewHolder.tvTen = (TextView)view.findViewById(R.id.tvten);
            viewHolder.tvPhone = (TextView)view.findViewById(R.id.tvsdt);
            view.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder)view.getTag();
        }
        final DanhBa danhba = mdanhBaList.get(i);
        viewHolder.tvPhone.setText(danhba.getSdt());
        viewHolder.tvTen.setText(danhba.getTen());
        if(danhba.getHinh()!=null)
        {
            viewHolder.ivHinh.setImageBitmap(danhba.getHinh());


        }
        else
        {
            viewHolder.ivHinh.setImageResource(R.drawable.man);

        }
        tf = Typeface.createFromAsset(context.getApplicationContext().getAssets(),"fonts/fontmaxdep.TTF");
        viewHolder.tvTen.setTypeface(tf);
        viewHolder.tvPhone.setTypeface(tf);
        return view;
    }
    private class ViewHolder
    {
        ImageView ivHinh;
        TextView tvTen;
        TextView tvPhone;
    }
    public void filter(String ctext)
    {
        ctext = ctext.toLowerCase(Locale.getDefault());
        mdanhBaList.clear();
        if(ctext.length()==0)
        {
            mdanhBaList.addAll(arraydanhba);
        }
        else
        {
            for (DanhBa db:arraydanhba)
            {
                if(db.getTen().toLowerCase(Locale.getDefault()).contains(ctext))
                {
                    mdanhBaList.add(db);
                }

            }
        }
        notifyDataSetChanged();
    }
}
