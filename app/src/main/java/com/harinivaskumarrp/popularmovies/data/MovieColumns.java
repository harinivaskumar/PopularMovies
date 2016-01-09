package com.harinivaskumarrp.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

import javax.annotation.Nonnull;

/**
 * Created by Hari Nivas Kumar R P on 1/7/2016.
 */
public interface MovieColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.TEXT) @Nonnull
    public static final String MOVIE_ID = "movie_id";

    @DataType(DataType.Type.TEXT) @Nonnull
    public static final String TITLE = "title";

    @DataType(DataType.Type.TEXT)
    public static final String POSTER = "poster";

    @DataType(DataType.Type.TEXT)
    public static final String OVERVIEW = "overview";

    @DataType(DataType.Type.TEXT)
    public static final String RATING = "rating";

    @DataType(DataType.Type.TEXT)
    public static final String RELEASE_DATE = "release_date";
}
