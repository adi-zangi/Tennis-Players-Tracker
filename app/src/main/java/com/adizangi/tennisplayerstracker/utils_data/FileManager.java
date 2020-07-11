/*
   Manages reading and writing to files
 */

package com.adizangi.tennisplayerstracker.utils_data;

import android.content.Context;
import android.content.ContextWrapper;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileManager extends ContextWrapper {

    public static final List<String> SELECTED_PLAYERS_DOESNT_EXIST = new ArrayList<>();
    public static final List<String> TOTAL_PLAYERS_DOESNT_EXIST = null;
    public static final Map<String, PlayerStats> PLAYER_STATS_DOESNT_EXIST = null;
    public static final String NOTIFICATION_TEXT_DOESNT_EXIST = "";
    public static final List<String> TOTAL_PLAYERS_NO_RANKINGS = new ArrayList<>();
    public static final Map<String, PlayerStats> PLAYER_STATS_NO_RANKINGS = new HashMap<>();

    private static final String SELECTED_PLAYERS_FILENAME = "selected_players";
    private static final String TOTAL_PLAYERS_FILENAME = "total_players";
    private static final String STATS_FILENAME = "player_stats";
    private static final String NOTIFICATION_FILENAME = "notification_text";

    /*
       Constructs a FileManager with the given application context
     */
    public FileManager(Context base) {
        super(base);
    }

    /*
       Reads the list of the user's players from the file and returns it
       Returns SELECTED_PLAYERS_DOESNT_EXIST if there is an error
     */
    @SuppressWarnings("unchecked")
    public List<String> readSelectedPlayers() {
        FileInputStream inputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            inputStream = openFileInput(SELECTED_PLAYERS_FILENAME);
            objectInputStream = new ObjectInputStream(inputStream);
            return (List<String>) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "There was a problem with getting saved players",
                    Toast.LENGTH_LONG).show();
            return SELECTED_PLAYERS_DOESNT_EXIST;
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
       Reads the total players list from the file and returns it
       Returns TOTAL_PLAYERS_DOESNT_EXIST if there is an error
     */
    @SuppressWarnings("unchecked")
    public List<String> readTotalPlayers() {
        FileInputStream inputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            inputStream = openFileInput(TOTAL_PLAYERS_FILENAME);
            objectInputStream = new ObjectInputStream(inputStream);
            return (List<String>) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return TOTAL_PLAYERS_DOESNT_EXIST;
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
       Reads the player stats map from the file and returns it
       Returns PLAYER_STATS_DOESNT_EXIST if there is an error
     */
    @SuppressWarnings("unchecked")
    public Map<String, PlayerStats> readPlayerStats() {
        FileInputStream inputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            inputStream = openFileInput(STATS_FILENAME);
            objectInputStream = new ObjectInputStream(inputStream);
            return (Map<String, PlayerStats>) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return PLAYER_STATS_DOESNT_EXIST;
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
       Reads the notification text from the file and returns it
       Returns NOTIFICATION_TEXT_DOESNT_EXIST if there is an error
     */
    public String readNotificationText() {
        FileInputStream inputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            inputStream = openFileInput(NOTIFICATION_FILENAME);
            objectInputStream = new ObjectInputStream(inputStream);
            return (String) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return NOTIFICATION_TEXT_DOESNT_EXIST;
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
       Stores the given list of the user's players in a file
     */
    public void storeSelectedPlayers(List<String> selectedPlayers) {
        FileOutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            outputStream = openFileOutput(SELECTED_PLAYERS_FILENAME, Context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(selectedPlayers);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "There was a problem with saving the new players",
                    Toast.LENGTH_LONG).show();
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
       Stores the given total players list in a file
     */
    public void storeTotalPlayers(List<String> totalPlayers) {
        FileOutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            outputStream = openFileOutput(TOTAL_PLAYERS_FILENAME, Context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(totalPlayers);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
       Stores the given player stats map in a file
     */
    public void storePlayerStats(Map<String, PlayerStats> playerStats) {
        FileOutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            outputStream = openFileOutput(STATS_FILENAME, Context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(playerStats);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
       Stores the given notification text in a file
     */
    public void storeNotificationText(String notificationText) {
        FileOutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            outputStream = openFileOutput(NOTIFICATION_FILENAME, Context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(notificationText);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
