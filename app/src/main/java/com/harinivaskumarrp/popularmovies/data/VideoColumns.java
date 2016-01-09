package com.harinivaskumarrp.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

import javax.annotation.Nonnull;

/**
 * Created by Hari Nivas Kumar R P on 1/7/2016.
 */
public interface VideoColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.INTEGER) @Nonnull
    @References(table = MovieDatabase.Tables.MOVIES, column = MovieColumns._ID)
    public static final String MOVIE_ID = "movie_id";

    @DataType(DataType.Type.TEXT) @Nonnull
    public static final String VIDEO_ID = "video_id";

    @DataType(DataType.Type.TEXT) @Nonnull
    public static final String KEY = "key";

    @DataType(DataType.Type.TEXT) @Nonnull
    public static final String NAME = "name";

    @DataType(DataType.Type.TEXT)
    public static final String PUBLISHED_SITE = "published_site";

    @DataType(DataType.Type.TEXT)
    public static final String TYPE = "type";
}
