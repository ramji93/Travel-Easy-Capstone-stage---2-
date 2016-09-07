package com.example.user.traveleasy.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by sam_chordas on 10/5/15.
 */
@Database(version = TravelDatabase.VERSION)
public class TravelDatabase {
  private TravelDatabase(){}

  public static final int VERSION = 1;

  @Table(TravelColumns.class) public static final String TRAVEL = "travel";

}
