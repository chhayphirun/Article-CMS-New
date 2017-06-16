package com.kshrd.articlecms.webservice;

import com.kshrd.articlecms.entity.ArticleResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by pirang on 6/14/17.
 */

public interface ArticleService {

    @GET("v1/api/articles")
    Call<ArticleResponse> findArticles(@Query("limit") int limit);

    @GET("v1/api/articles")
    Call<ArticleResponse> findArticleByTitle(@Query("title") String title);

    @DELETE("/v1/api/articles/{id}")
    Call<ArticleResponse> deleteArticles(@Path("id") int id);
    @PUT("/v1/api/articles/{id}")
    Call<ArticleResponse.Article> updateArticle(@Path("id") int id, @Body ArticleResponse.Article article);

}
