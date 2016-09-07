package com.example.user.traveleasy.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by sam_chordas on 10/5/15.
 */
public class TravelColumns {
  @DataType(DataType.Type.INTEGER) @PrimaryKey @AutoIncrement
  public static final String _ID = "_id";
  @DataType(DataType.Type.TEXT) @NotNull
  public static final String ORIGIN = "origin";
  @DataType(DataType.Type.TEXT) @NotNull
  public static final String DESTINATION = "destination";
  @DataType(DataType.Type.TEXT) @NotNull
  public static final String DATE = "date";

  @DataType(DataType.Type.INTEGER) @NotNull
  public static final String DATE_LONG = "date_long";

  @DataType(DataType.Type.TEXT) @NotNull
  public static final String CABIN_CLASS = "cabin_class";
  @DataType(DataType.Type.INTEGER) @NotNull
  public static final String ADULTS = "adults";
  @DataType(DataType.Type.INTEGER) @NotNull
  public static final String CHILDREN = "children";
}
