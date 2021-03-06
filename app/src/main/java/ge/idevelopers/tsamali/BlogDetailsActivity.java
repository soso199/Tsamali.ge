package ge.idevelopers.tsamali;


import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Handler;
import ge.idevelopers.tsamali.adapters.OtherBlogsAdapter;
import ge.idevelopers.tsamali.models.BlogsModel;
import ge.idevelopers.tsamali.tabs.Blog;

public class BlogDetailsActivity extends AppCompatActivity {

    private TextView title;
    private TextView text;
    private ImageView image;
    private ImageView fbShare;
    private ImageView imageBack;
    private TextView otherArticlesTet;
    private RecyclerView recyclerView;
    private OtherBlogsAdapter blogsAdapter;
    private List<BlogsModel> threeBlogsList;
    private ImageView back;
    public Tracker mTracker;
    public ShareDialog shareDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);
        Bundle extras = getIntent().getExtras();
            String title_string = extras.getString("title");
            String url = extras.getString("url");
            final String text_string = extras.getString("text");
            final String link=extras.getString("link");



        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        mTracker.setScreenName(title_string);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());


        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Share")
                .build());

        imageBack=(ImageView) findViewById(R.id.image_cover);
        text=(TextView)findViewById(R.id.main_text);
        title=(TextView)findViewById(R.id.title_text);
        image=(ImageView)findViewById(R.id.details_image);
        fbShare=(ImageView) findViewById(R.id.fbShare_blog);
        otherArticlesTet=(TextView)findViewById(R.id.other_articles_text);

        back=(ImageView)findViewById(R.id.blog_back);

        Typeface forTitles= Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/alkroundedmtav-medium.otf");
        Typeface forText= Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/bpg_glaho.ttf");

        title.setTypeface(forTitles);
        text.setTypeface(forText);
        otherArticlesTet.setTypeface(forTitles);

        title.setText(title_string);
        Picasso.with(getApplicationContext()).load(url).into(image);


        text.setText(Html.fromHtml(text_string));
        final Spanned[] span = new Spanned[1];
        AsyncTask task = new AsyncTask () {
         @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
             text.setMovementMethod(ScrollingMovementMethod
                     .getInstance());
             if(span != null) {
                 text.setText(span[0]);
             }
}

            @Override
            protected String doInBackground(Object[] params) {
                span[0] =Html.fromHtml(text_string,getImageHTML(),null);
                return null;
            }
        };

        task.execute();

        text.setMovementMethod(LinkMovementMethod.getInstance());


        threeBlogsList=new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        blogsAdapter = new OtherBlogsAdapter(threeBlogsList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(blogsAdapter);


        int arraySize= Blog.blogsList.size();

        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i=1; i<arraySize; i++) {
            list.add(new Integer(i));
        }
        Collections.shuffle(list);
        for (int i=0; i<3; i++) {
            threeBlogsList.add(Blog.blogsList.get(list.get(i)));
        }



        blogsAdapter.notifyDataSetChanged();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BlogDetailsActivity.super.onBackPressed();
            }
        });


        /////fb shearing
        fbShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ShareLinkContent linkContent;

                shareDialog = new ShareDialog(BlogDetailsActivity.this);
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    linkContent = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(link))
                            .build();
                    shareDialog.show(linkContent);
                }



            }
        });

    }
    public Html.ImageGetter getImageHTML() {
        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            public Drawable getDrawable(String source) {
                try {
                    Drawable drawable = Drawable.createFromStream(new URL(source).openStream(), "src name");
                    drawable.setBounds(0, 0, imageBack.getWidth(),imageBack.getHeight());

                    return drawable;
                } catch(IOException exception) {
                    Log.v("IOException",exception.getMessage());
                    return null;
                }
            }
        };
        return imageGetter;
    }




}
