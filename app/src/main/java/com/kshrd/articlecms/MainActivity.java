package com.kshrd.articlecms;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kshrd.articlecms.entity.ArticleResponse;
import com.kshrd.articlecms.popupdialog.EditArticleDialogFramentAdapter;
import com.kshrd.articlecms.popupdialog.MyClickListenerUpdatePopup;
import com.kshrd.articlecms.webservice.ArticleService;
import com.kshrd.articlecms.webservice.ServiceGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements TextWatcher, MyClickListener,MyClickListenerUpdatePopup {

    @BindView(R.id.rvArticle)
    RecyclerView rvArticle;

    @BindView(R.id.etKeyword)
    EditText etKeyword;

    ArticleAdapter articleAdapter;
    ArticleService articleService;

    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Initialization
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                loadArticles(etKeyword.getText().toString());
            }
        };

        setRecyclerView();

        articleService = ServiceGenerator.createService(ArticleService.class);
        loadArticles(etKeyword.getText().toString());
        etKeyword.addTextChangedListener(this);

        // Code..

    }

    private void loadArticles(String keyword) {
        Call<ArticleResponse> call = articleService.findArticleByTitle(keyword);
        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {

                ArticleResponse articleResponse = response.body();
                articleAdapter.clearList();
                articleAdapter.addMoreItems(articleResponse.getArticlelist());
                Log.e("ooooo", articleAdapter.getItemCount()+ "");

            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setRecyclerView() {
        articleAdapter = new ArticleAdapter();
        rvArticle.setLayoutManager(new LinearLayoutManager(this));
        rvArticle.setAdapter(articleAdapter);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence keyword, int i, int i1, int i2) {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
    EditArticleDialogFramentAdapter editdialog;
    @Override
    public void onClicked(final int position, View view) {
        final ArticleResponse.Article article = articleAdapter.getArticle(position);
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
        popupMenu.inflate(R.menu.my_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                         ArticleService articleService1=ServiceGenerator.createService(ArticleService.class);
                        Call<ArticleResponse> call2=articleService1.deleteArticles(article.getId());
                        call2.enqueue(new Callback<ArticleResponse>() {
                            @Override
                            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                                ArticleResponse article1=response.body();
                            }

                            @Override
                            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                            }
                        });
                        articleAdapter.removteArticle(position);
                        articleAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this,"Delete Successe", Toast.LENGTH_SHORT).show();
                        break;


                    case R.id.update:
                        editdialog=new EditArticleDialogFramentAdapter();
                        editdialog.setPosition(position);
                        editdialog.setArticle(article);
//                         ArticleResponse.Article article1=editdialog.getArticle();
                        editdialog.show(getSupportFragmentManager(),null);
                        Toast.makeText(MainActivity.this, article.getId()+"", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }

        });
        popupMenu.show();
    }

    @Override
    public void onUpdateClicked(final int post, final ArticleResponse.Article article) {
        Call<ArticleResponse.Article> call3=articleService.updateArticle(article.getId(),article);
        call3.enqueue(new Callback<ArticleResponse.Article>() {
            @Override
            public void onResponse(Call<ArticleResponse.Article> call, Response<ArticleResponse.Article> response) {
                articleAdapter.removteArticle(post);
                articleAdapter.addArticleToRecycler(post,article);
                articleAdapter.notifyDataSetChanged();
                Log.e("ooooo","Position :"+post);
            }

            @Override
            public void onFailure(Call<ArticleResponse.Article> call, Throwable t) {

            }
        });
    }
}
