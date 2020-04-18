package com.jueezy.spotify;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import android.util.Log;

public abstract class RootAccess {
    private static final String TAG = "RootAccess";
    protected abstract ArrayList<String> runCommandsWithRootAccess();

    //Check for Root Access
    public static boolean hasRootAccess() {
        boolean rootBoolean = false;
        Process suProcess;

        try {
            suProcess = Runtime.getRuntime().exec("su");

            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            DataInputStream is = new DataInputStream(suProcess.getInputStream());

            if (os != null && is != null) {
                // Getting current user's UID to check for Root Access
                os.writeBytes("id\n");
                os.flush();

                String outputSTR = is.readLine();
                boolean exitSu = false;
                if (outputSTR == null) {
                    rootBoolean = false;
                    exitSu = false;
                    Log.d(TAG, "Can't get Root Access or Root Access deneid by user");
                } else if (outputSTR.contains("uid=0")) {
                    //If is contains uid=0, It means Root Access is granted
                    rootBoolean = true;
                    exitSu = true;
                    Log.d(TAG, "Root Access Granted");
                } else {
                    rootBoolean = false;
                    exitSu = true;
                    Log.d(TAG, "Root Access Rejected: " + is.readLine());
                }

                if (exitSu) {
                    os.writeBytes("exit\n");
                    os.flush();
                }
            }
        } catch (Exception e) {
            rootBoolean = false;
            Log.d(TAG, "Root access rejected [" + e.getClass().getName() + "] : " + e.getMessage());
        }

        return rootBoolean;
    }

    //Execute commands with ROOT Permission
    public final boolean execute() {
        boolean rootBoolean = false;

        try {
            ArrayList<String> commands = runCommandsWithRootAccess();
            if ( commands != null && commands.size() > 0) {
                Process suProcess = Runtime.getRuntime().exec("su");

                DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());

                // Execute commands with ROOT Permission
                for (String currentCommand : commands) {
                    os.writeBytes(currentCommand + "\n");
                    os.flush();
                }

                os.writeBytes("exit\n");
                os.flush();

                try {
                    int suProcessRetval = suProcess.waitFor();
                    if ( suProcessRetval != 255) {
                        // Root Access granted
                        rootBoolean = true;
                    } else {
                        // Root Access denied
                        rootBoolean = false;
                    }
                } catch (Exception ex) {
                    Log.e(TAG, "Error executing Root Action", ex);

                }
            }
        } catch (IOException ex) {
            Log.w(TAG, "Can't get Root Access", ex);
        } catch (SecurityException ex) {
            Log.w(TAG, "Can't get Root Access", ex);
        } catch (Exception ex) {
            Log.w(TAG, "Error executing operation", ex);
        }

        return rootBoolean;
    }


}