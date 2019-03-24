package com.project.photoshoot.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageBookModel implements Parcelable {


    static AtomicInteger nextId = new AtomicInteger();


    Uri photoURI;
    File file;
    int id;

    public ImageBookModel(String coverUrl) {
        photoUrl = coverUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    String photoUrl;


    public ImageBookModel(Uri photoURI) {
        this.photoURI = photoURI;
        this.id = nextId.incrementAndGet();
    }

    protected ImageBookModel(Parcel in) {
        photoURI = in.readParcelable(Uri.class.getClassLoader());
        id = in.readInt();
    }

    public static final Creator<ImageBookModel> CREATOR = new Creator<ImageBookModel>() {
        @Override
        public ImageBookModel createFromParcel(Parcel in) {
            return new ImageBookModel(in);
        }

        @Override
        public ImageBookModel[] newArray(int size) {
            return new ImageBookModel[size];
        }
    };

    public Uri getPhotoURI() {
        return photoURI;
    }

    public void setPhotoURI(Uri photoURI) {
        this.photoURI = photoURI;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        Uri.writeToParcel(dest,photoURI);
    }


    public static class UriSerializer implements JsonSerializer<Uri> {
        public JsonElement serialize(Uri src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

    public static class UriDeserializer implements JsonDeserializer<Uri> {
        @Override
        public Uri deserialize(final JsonElement src, final Type srcType,
                               final JsonDeserializationContext context) throws JsonParseException {
            return Uri.parse(src.getAsString());
        }
    }
}
