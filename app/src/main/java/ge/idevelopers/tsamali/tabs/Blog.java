package ge.idevelopers.tsamali.tabs;

/**
 * Created by soso on 4/5/17.
 */
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ge.idevelopers.tsamali.R;
import ge.idevelopers.tsamali.adapters.BlogsAdapter;
import ge.idevelopers.tsamali.models.BlogsModel;

public class Blog extends Fragment {

    private List<BlogsModel> blogsList=new ArrayList<>();
    private RecyclerView recyclerView;
    private BlogsAdapter blogsAdapter;
    private boolean is_taken=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.blog, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        blogsAdapter = new BlogsAdapter(blogsList,getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(blogsAdapter);

           final String url="http://tsamali.ge/api/blog";

        final ProgressDialog progress=new ProgressDialog(getActivity());
        JsonObjectRequest getBlogs=new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    "",
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                            JSONArray array = null;
                            try {
                                array = response.getJSONArray("posts");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            blogsList.clear();

                            for (int i = 0; i < array.length(); i++) {

                                try {
                                    JSONObject blogObject = array.getJSONObject(i);

                                    String title = blogObject.getString("title");
                                    String img = blogObject.getString("img");
                                    String text = blogObject.getString("text");
                                    int views = blogObject.getInt("views");

                                    BlogsModel blogsModel = new BlogsModel(title, img, text, views);
                                    blogsList.add(blogsModel);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            Collections.reverse(blogsList);
                            blogsAdapter.notifyDataSetChanged();
                             progress.cancel();



                        }


                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }

                    }

            );

            RequestQueue queue= Volley.newRequestQueue(getActivity().getApplicationContext());
            queue.add(getBlogs);

        progress.setCanceledOnTouchOutside(false);
        progress.show();

        blogsAdapter.notifyDataSetChanged();

        return rootView;
    }



}