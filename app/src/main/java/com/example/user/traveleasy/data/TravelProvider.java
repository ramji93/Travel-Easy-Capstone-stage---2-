package com.example.user.traveleasy.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by sam_chordas on 10/5/15.
 */
@ContentProvider(authority = TravelProvider.AUTHORITY, database = TravelDatabase.class)
public class TravelProvider  {
  public static final String AUTHORITY = "com.example.user.traveleasy.data.TravelProvider";

  static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

  interface Path{
    String TRAVELS = "travels";
  }

  private static Uri buildUri(String... paths){
    Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
    for (String path:paths){
      builder.appendPath(path);
    }
    return builder.build();
  }

  @TableEndpoint(table = TravelDatabase.TRAVEL)
  public static class TRAVELS{
    @ContentUri(
        path = Path.TRAVELS,
        type = "vnd.android.cursor.dir/travel"
    )
    public static final Uri CONTENT_URI = buildUri(Path.TRAVELS);


    @InexactContentUri(
        name = "TRAVEL_ID",
        path = Path.TRAVELS + "/*",
        type = "vnd.android.cursor.item/travel",
        whereColumn = TravelColumns.ORIGIN,
        pathSegment = 1
    )
    public static Uri withSymbol(String origin){
      return buildUri(Path.TRAVELS, origin);
    }
  }
}
