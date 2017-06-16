package com.kshrd.articlecms.popupdialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.kshrd.articlecms.R;
import com.kshrd.articlecms.entity.ArticleResponse;

import java.util.List;

/**
 * Created by LIER on 6/16/2017.
 */

public class EditArticleDialogFramentAdapter extends DialogFragment implements MyClickListenerUpdatePopup {
    List<ArticleResponse.Article> articleList;
    ArticleResponse.Article article;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    int position;
    MyClickListenerUpdatePopup myClickListenerUpdatePopup;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myClickListenerUpdatePopup= (MyClickListenerUpdatePopup) context;
    }

    public ArticleResponse.Article getArticle() {
        return article;
    }

    public void setArticle(ArticleResponse.Article article) {
        this.article = article;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextView tvid= (TextView) view.findViewById(R.id.tvId);
        final EditText ettitle= (EditText) view.findViewById(R.id.ettitle);
        final EditText etdescritipn= (EditText) view.findViewById(R.id.etdesc);
        tvid.setText(String.valueOf(article.getId()));
        ettitle.setText(article.getTitle());
        etdescritipn.setText(article.getDescription());

        view.findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                article.setId(Integer.parseInt(tvid.getText().toString()));
                article.setTitle(String.valueOf(ettitle.getText()));
                article.setDescription(String.valueOf(etdescritipn.getText()));
                myClickListenerUpdatePopup.onUpdateClicked(position,article);
                dismiss();
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.editarticle_dialog_fragment,container);
        return view;
    }

    @Override
    public void onUpdateClicked(int post, ArticleResponse.Article article) {

    }
}
