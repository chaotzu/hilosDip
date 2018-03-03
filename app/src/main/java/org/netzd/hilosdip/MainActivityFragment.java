package org.netzd.hilosdip;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.netzd.hilosdip.webservices.Entity;
import org.netzd.hilosdip.webservices.JSONParser;
import org.netzd.hilosdip.webservices.Petition;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView moviesRecyclerView = null;
    private ProgressBar moviesProgressBar = null;

    private List<Video> videos = null;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        moviesRecyclerView = (RecyclerView) view.findViewById(R.id.moviesRecyclerView);
        moviesProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);


        if(NetworkConnection.isConnectionAvailable(getContext())) {
            //Hilos
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONParser jsonParser = new JSONParser();
                        videos = jsonParser.getVideos("http://www.omdbapi.com/?s=superman&apikey=2b28d307&r=json", new Petition(Entity.NONE));
                        getActivity().runOnUiThread(mostrarListaRunnable);
                    } catch (Exception e) {
                        Looper.prepare(); //Maneja la cola de procesos
                        Message msj = mensajeError.obtainMessage(); //
                        msj.obj = e.getMessage();
                        mensajeError.sendMessage(msj);
                        Looper.loop();
                        Looper.myLooper().quit();
                    }
                }
            }).start();
        }else {
            Toast.makeText(getContext(), "no hay internet", Toast.LENGTH_LONG).show();
        }
    }

    private Runnable mostrarListaRunnable = new Runnable() {
        @Override
        public void run() {
            moviesProgressBar.setVisibility(View.GONE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
            moviesRecyclerView.setHasFixedSize(true);
            moviesRecyclerView.setLayoutManager(layoutManager);
            VideoAdapter videoAdapter = new VideoAdapter(videos,getActivity());
            moviesRecyclerView.setAdapter(videoAdapter);
        }
    };

    private Handler mensajeError = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.obj!=null){
                moviesProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(),"Hubo un erro", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
