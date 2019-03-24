package com.project.photoshoot.main.pdf;

// File Name SendEmail.java

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.util.*;

public class Sendmail {
    public static void send(File file, Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        context.startActivity(Intent.createChooser(emailIntent,"Sending email..."));
    }
}


