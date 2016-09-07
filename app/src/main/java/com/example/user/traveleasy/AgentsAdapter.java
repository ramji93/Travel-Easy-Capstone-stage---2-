package com.example.user.traveleasy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 16-08-2016.
 */
public class AgentsAdapter extends RecyclerView.Adapter<AgentsAdapter.AgentViewHolder> {

   private Context mcontext;
   private ArrayList<Booking_Item> mbooking_items;
   private AppCompatActivity mactivity;

    public AgentsAdapter(Context context, AppCompatActivity activity, ArrayList<Booking_Item> booking_items)
    {
        mcontext = context;
        mbooking_items = booking_items;
        mactivity = activity;

    }

     public AgentsAdapter()
     {


     }


    @Override
    public AgentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.agent_list_item,parent,false);

        view.setFocusable(true);
        return  new AgentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AgentViewHolder holder, final int position) {

        try {

            holder.Price.setText(""+mbooking_items.get(position).getPrice()+" $");

            holder.Price.setContentDescription("Price "+mbooking_items.get(position).getPrice()+ " USD");

            Agent agent = MainFragment.Agentlist.get(mbooking_items.get(position).getAgentid());
            holder.AgentName.setText(agent.getName());

            holder.AgentName.setContentDescription("agent name "+agent.getName());

            Glide.with(mcontext).load(new URL(agent.getImageurl())).override(600, 200).centerCrop().into(holder.AgentImage);
            holder.Book_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(mbooking_items.get(position).getDeeplink()));
                    if (intent.resolveActivity(mcontext.getPackageManager()) != null) {
                        mcontext.startActivity(intent);
                    }
                }
            });

            holder.Share_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String extraText = mbooking_items.get(position).getDeeplink();
                    intent.putExtra(Intent.EXTRA_TEXT, extraText);
                    mcontext.startActivity(Intent.createChooser(intent, "Share with Friends"));

                }
            });



            View view = holder.itemView;
            view.setTranslationY(mactivity.getWindowManager().getDefaultDisplay().getHeight());
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(300)
                    .start();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }



    }

    @Override
    public int getItemCount() {

        if (mbooking_items!=null)

        return mbooking_items.size();

        else
            return  0;
    }

    public class AgentViewHolder extends RecyclerView.ViewHolder {

        public ImageView AgentImage;
        public TextView AgentName;
        public TextView Price;
        public Button Book_button;
        public ImageButton Share_button;


        public AgentViewHolder(View itemView) {
            super(itemView);

            AgentImage = (ImageView) itemView.findViewById(R.id.agent_image);
            AgentName = (TextView) itemView.findViewById(R.id.agent_name);
            Price = (TextView) itemView.findViewById(R.id.Price);
            Book_button = (Button) itemView.findViewById(R.id.book_button);
            Share_button = (ImageButton) itemView.findViewById(R.id.share_button);

        }





    }

}
