package com.siddapps.android.dailybackground;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    List<RedditListing.Data.Child> mChildren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        RedditClient redditClient = new RedditClient();
        Call<RedditListing> call = redditClient.start();

        call.enqueue(new Callback<RedditListing>() {
            @Override
            public void onResponse(Call<RedditListing> call, Response<RedditListing> response) {
                if (response.isSuccessful()) {
                    RedditListing redditListing = response.body();
                    mChildren = redditListing.getData().getChildren();
                    mRecyclerView.setAdapter(new ChildAdapter(mChildren));
                }
            }

            @Override
            public void onFailure(Call<RedditListing> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public class ChildHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mPhotoImageView;
        private RedditListing.Data.Child mChildren;

        public ChildHolder(View itemView) {
            super(itemView);
            mPhotoImageView = (ImageView) itemView.findViewById(R.id.photo_image_view);
            itemView.setOnClickListener(this);
        }

        public void bind(RedditListing.Data.Child child) {
            mChildren = child;
            Picasso.with(mPhotoImageView.getContext()).load(child.getData().getThumbnail()).into(mPhotoImageView);
        }

        @Override
        public void onClick(View v) {
            Intent i = PhotoDetailActivity.newIntent(mPhotoImageView.getContext(), mChildren.getData().getPreview().getImages().get(0).getSource().getUrl());
            startActivity(i);
        }
    }

    public class ChildAdapter extends RecyclerView.Adapter<ChildHolder> {
        List<RedditListing.Data.Child> mChildren;

        public ChildAdapter(List<RedditListing.Data.Child> children) {
            mChildren = children;
        }

        @Override
        public ChildHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_photos, parent, false);
            return new ChildHolder(v);
        }

        @Override
        public void onBindViewHolder(ChildHolder holder, int position) {
            holder.bind(mChildren.get(position));
        }

        @Override
        public int getItemCount() {
            return mChildren.size();
        }
    }

}



