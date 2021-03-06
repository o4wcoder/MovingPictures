package com.fourthwardmobile.android.movingpictures.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Cast implements Comparable, Parcelable {

    @SerializedName("cast_id")
    @Expose
    private Integer castId;
    @SerializedName("character")
    @Expose
    private String character;
    @SerializedName("credit_id")
    @Expose
    private String creditId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("profile_path")
    @Expose
    private String profilePath;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("first_air_date")
    @Expose
    private String firstAirDate;

    /**
     *
     * @return
     * The castId
     */
    public Integer getCastId() {
        return castId;
    }

    /**
     *
     * @param castId
     * The cast_id
     */
    public void setCastId(Integer castId) {
        this.castId = castId;
    }

    /**
     *
     * @return
     * The character
     */
    public String getCharacter() {
        return character;
    }

    /**
     *
     * @param character
     * The character
     */
    public void setCharacter(String character) {
        this.character = character;
    }

    /**
     *
     * @return
     * The creditId
     */
    public String getCreditId() {
        return creditId;
    }

    /**
     *
     * @param creditId
     * The credit_id
     */
    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The order
     */
    public Integer getOrder() {
        return order;
    }

    /**
     *
     * @param order
     * The order
     */
    public void setOrder(Integer order) {
        this.order = order;
    }

    /**
     *
     * @return
     * The profilePath
     */
    public String getProfilePath() {
        return profilePath;
    }

    /**
     *
     * @param profilePath
     * The profile_path
     */
    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }


    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public int getReleaseYear() {

        int releaseYear = 0;
        if((releaseDate != null) && (releaseDate != "") && (releaseDate != "null")) {
            String dateArray[] = releaseDate.split("-");

            try {
            releaseYear = Integer.parseInt(dateArray[0]);
            } catch(NumberFormatException e) {}

        }

        return releaseYear;
    }

    public int getFirstAirYear() {

        int firstAirYear = 0;
        if((firstAirDate != null) && (firstAirDate != "") && (firstAirDate != "null")) {
            String dateArray[] = firstAirDate.split("-");

            try {
                firstAirYear = Integer.parseInt(dateArray[0]);
            } catch(NumberFormatException e) {}

        }

        return firstAirYear;
    }


    @Override
    public int compareTo(Object object) {

        if((releaseDate != null) && (releaseDate != "") && (releaseDate != "null")) {
            int compareYear = ((Cast) object).getReleaseYear();
            return compareYear - this.getReleaseYear();
        } else {
            int compareYear = ((Cast) object).getFirstAirYear();
            return compareYear - this.getFirstAirYear();
        }
    }

    protected Cast(Parcel in) {
        castId = in.readByte() == 0x00 ? null : in.readInt();
        character = in.readString();
        creditId = in.readString();
        id = in.readByte() == 0x00 ? null : in.readInt();
        name = in.readString();
        order = in.readByte() == 0x00 ? null : in.readInt();
        profilePath = in.readString();
        posterPath = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        firstAirDate = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (castId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(castId);
        }
        dest.writeString(character);
        dest.writeString(creditId);
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(name);
        if (order == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(order);
        }
        dest.writeString(profilePath);
        dest.writeString(posterPath);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(firstAirDate);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Cast> CREATOR = new Parcelable.Creator<Cast>() {
        @Override
        public Cast createFromParcel(Parcel in) {
            return new Cast(in);
        }

        @Override
        public Cast[] newArray(int size) {
            return new Cast[size];
        }
    };

}