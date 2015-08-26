package itocorp.ibms.bibliamontesiao.view.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import itocorp.ibms.bibliamontesiao.R;
import itocorp.ibms.bibliamontesiao.model.Versiculo;

public class VersiculoAdapter extends ArrayAdapter<Versiculo> {
    public VersiculoAdapter(Context context) {
        super(context, R.layout.item_versiculo, new ArrayList<Versiculo>());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_versiculo, parent, false);
        }

        Versiculo versiculo = getItem(position);
        TextView title = (TextView)view.findViewById(R.id.title);
        TextView text = (TextView)view.findViewById(R.id.versiculo);
        if (versiculo.mType == Versiculo.TYPE_TITLE) {
            title.setText(versiculo.mText);
            title.setVisibility(View.VISIBLE);
            text.setVisibility(View.GONE);
        } else {
            text.setText(Html.fromHtml("<i><b>" + versiculo.mNumber + "</b></i> " + versiculo.mText));
            text.setVisibility(View.VISIBLE);
            title.setVisibility(View.GONE);
        }
        return view;
    }
}
