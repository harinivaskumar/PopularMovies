package com.harinivaskumarrp.popularmovies.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by Hari Nivas Kumar R P on 1/7/2016.
 */
@ContentProvider(authority = MovieProvider.AUTHORITY,
        database = MovieDatabase.class)
public final class MovieProvider {

    public static final String AUTHORITY =
            "com.harinivaskumarrp.popularmovies.data.MovieProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path{
        String MOVIES = "movies";
        String REVIEWS = "reviews";
        String REVIEW_LIST_FOR_MOVIE = "reviewListForMovie";
        String VIDEOS = "videos";
        String VIDEO_LIST_FOR_MOVIE = "videoListForMovie";
    }

    public static Uri buildUri(String...paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths){
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = MovieDatabase.Tables.MOVIES)
    public static class Movies {
        @ContentUri(
                path = Path.MOVIES,
                type = "vnd.android.cursor.dir/movie",
                defaultSort = MovieColumns._ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.MOVIES);

        @InexactContentUri(
                path = Path.MOVIES + "/#",
                name = "MOVIE_ID",
                type = "vnd.android.cursor.item/movie",
                whereColumn = MovieColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.MOVIES, String.valueOf(id));
        }
    }

    @TableEndpoint(table = MovieDatabase.Tables.REVIEWS)
    public static class Reviews {
        @ContentUri(
                path = Path.REVIEWS,
                type = "vnd.android.cursor.dir/review",
                defaultSort = ReviewColumns._ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.REVIEWS);

        @InexactContentUri(
                name = "REVIEW_ID",
                path = Path.REVIEWS + "/#",
                type = "vnd.android.cursor.item/review",
                whereColumn = ReviewColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.REVIEWS, String.valueOf(id));
        }

        @InexactContentUri(
                name = "REVIEWS_LIST_FOR_MOVIE_ID",
                path = Path.REVIEWS + "/" + Path.REVIEW_LIST_FOR_MOVIE + "/#",
                type = "vnd.android.cursor.dir/movie",
                whereColumn = ReviewColumns.MOVIE_ID,
                defaultSort = ReviewColumns._ID + "ASC",
                pathSegment = 2)
        public static Uri reviewListForMovie(long id) {
            return buildUri(Path.REVIEWS, Path.REVIEW_LIST_FOR_MOVIE, String.valueOf(id));
        }
    }

    @TableEndpoint(table = MovieDatabase.Tables.VIDEOS)
    public static class Videos {
        @ContentUri(
                path = Path.VIDEOS,
                type = "vnd.android.cursor.dir/video",
                defaultSort = VideoColumns._ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.VIDEOS);

        @InexactContentUri(
                name = "VIDEO_ID",
                path = Path.VIDEOS + "/#",
                type = "vnd.android.cursor.item/video",
                whereColumn = ReviewColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.VIDEOS, String.valueOf(id));
        }

        @InexactContentUri(
                name = "VIDEO_LIST_FOR_MOVIE_ID",
                path = Path.VIDEOS + "/" + Path.MOVIES + "/#",
                type = "vnd.android.cursor.dir/movie",
                whereColumn = VideoColumns.MOVIE_ID,
                defaultSort = VideoColumns._ID + "ASC",
                pathSegment = 2)
        public static Uri videoListForMovie(long id) {
            return buildUri(Path.VIDEOS, Path.VIDEO_LIST_FOR_MOVIE, String.valueOf(id));
        }
    }
}
