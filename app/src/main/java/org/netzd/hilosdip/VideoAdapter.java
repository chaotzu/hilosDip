package org.netzd.hilosdip;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import org.netzd.hilosdip.webservices.Entity;
import org.netzd.hilosdip.webservices.JSONParser;
import org.netzd.hilosdip.webservices.Petition;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alumno12 on 24/02/18.
 */


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private List<Video> videos=null;

    private Context context = null;

    public VideoAdapter(List<Video> videos, Context context) {
        this.videos = videos;
        this.context = context;
    }

    public VideoAdapter(List<Video> contacts) {
        if(contacts==null){
            this.videos=new ArrayList<>();
        }else {
            this.videos = contacts;
        }
    }




    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View item= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video,parent,false);
        return new VideoViewHolder(item);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {

        //holder.thumbnailImageView.setImageResource(videos.get(position).getPoster());
        holder.titleTextView.setText(videos.get(position).getTitle());
        holder.typeTextView.setText(videos.get(position).getType());

        //Biblioteca manejo de imagenes
        Glide.with(context).load(videos.get(position).getPoster()).into(holder.thumbnailImageView);


        //ANTES de la biblioteca
        /*BitmapFactory.Options bmOptions;
        bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;

        Bitmap bm = LoadImage(videos.get(position).getPoster(), bmOptions);
        holder.thumbnailImageView.setImageBitmap(bm);*/


    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder{

        protected ImageView thumbnailImageView=null;
        protected TextView titleTextView=null;
        protected TextView typeTextView=null;

        public VideoViewHolder(View itemView) {
            super(itemView);
            thumbnailImageView=(ImageView) itemView.findViewById(R.id.thumbnailImageView);
            titleTextView=(TextView) itemView.findViewById(R.id.titleTextView);
            typeTextView=(TextView) itemView.findViewById(R.id.typeTextView);
        }
    }


    private Bitmap LoadImage(String URL, BitmapFactory.Options options)
    {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            JSONParser rss = new JSONParser();
            //leo la cadena de bytes de la conexion
            in = rss.createConnection(URL,new Petition(Entity.NONE)).getInputStream();
            //convierte en archivo bitmap
            bitmap = BitmapFactory.decodeStream(in, null, options);
            in.close();
        } catch (IOException e1) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
