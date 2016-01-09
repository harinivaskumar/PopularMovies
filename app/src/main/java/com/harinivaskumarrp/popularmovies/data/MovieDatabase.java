package com.harinivaskumarrp.popularmovies.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Hari Nivas Kumar R P on 1/7/2016.
 */
@Database(version = MovieDatabase.VERSION)
public final class MovieDatabase {

    public static final int VERSION = 1;

    public static class Tables {
        @Table(MovieColumns.class)
        public static final String MOVIES = "movies";

        @Table(ReviewColumns.class)
        public static final String REVIEWS = "reviews";

        @Table(VideoColumns.class)
        public static final String VIDEOS = "videos";
    }
}
