package com.fourthwardmobile.android.movingpictures.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.fourthwardmobile.android.movingpictures.data.FavoritesContract;
import com.fourthwardmobile.android.movingpictures.interfaces.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TvShow implements Constants, Parcelable {

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("created_by")
    @Expose
    private List<CreatedBy> createdBy = new ArrayList<CreatedBy>();
    @SerializedName("episode_run_time")
    @Expose
    private List<Integer> episodeRunTime = new ArrayList<Integer>();
    @SerializedName("first_air_date")
    @Expose
    private String firstAirDate;
    @SerializedName("genres")
    @Expose
    private List<Genre> genres = new ArrayList<Genre>();
    @SerializedName("homepage")
    @Expose
    private String homepage;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("in_production")
    @Expose
    private Boolean inProduction;
    @SerializedName("languages")
    @Expose
    private List<String> languages = new ArrayList<String>();
    @SerializedName("last_air_date")
    @Expose
    private String lastAirDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("networks")
    @Expose
    private List<Network> networks = new ArrayList<Network>();
    @SerializedName("number_of_episodes")
    @Expose
    private Integer numberOfEpisodes;
    @SerializedName("number_of_seasons")
    @Expose
    private Integer numberOfSeasons;
    @SerializedName("origin_country")
    @Expose
    private List<String> originCountry = new ArrayList<String>();
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("original_name")
    @Expose
    private String originalName;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("production_companies")
    @Expose
    private List<ProductionCompany> productionCompanies = new ArrayList<ProductionCompany>();
    @SerializedName("seasons")
    @Expose
    private List<Season> seasons = new ArrayList<Season>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    @SerializedName("credits")
    @Expose
    private Credits credits;
    @SerializedName("videos")
    @Expose
    private VideoList videos;
    @SerializedName("content_ratings")
    @Expose
    private ContentRatings contentRatings;
    /**
     *
     * @return
     * The backdropPath
     */
    public String getBackdropPath() {
        return backdropPath;
    }

    /**
     *
     * @param backdropPath
     * The backdrop_path
     */
    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    /**
     *
     * @return
     * The createdBy
     */
    public List<CreatedBy> getCreatedBy() {
        return createdBy;
    }

    /**
     *
     * @param createdBy
     * The created_by
     */
    public void setCreatedBy(List<CreatedBy> createdBy) {
        this.createdBy = createdBy;
    }

    /**
     *
     * @return
     * The episodeRunTime
     */
    public List<Integer> getEpisodeRunTime() {
        return episodeRunTime;
    }

    /**
     *
     * @param episodeRunTime
     * The episode_run_time
     */
    public void setEpisodeRunTime(List<Integer> episodeRunTime) {
        this.episodeRunTime = episodeRunTime;
    }

    /**
     *
     * @return
     * The firstAirDate
     */
    public String getFirstAirDate() {
        return firstAirDate;
    }

    /**
     *
     * @param firstAirDate
     * The first_air_date
     */
    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    /**
     *
     * @return
     * The genres
     */
    public List<Genre> getGenres() {
        return genres;
    }

    /**
     *
     * @param genres
     * The genres
     */
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    /**
     *
     * @return
     * The homepage
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     *
     * @param homepage
     * The homepage
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
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
     * The inProduction
     */
    public Boolean getInProduction() {
        return inProduction;
    }

    /**
     *
     * @param inProduction
     * The in_production
     */
    public void setInProduction(Boolean inProduction) {
        this.inProduction = inProduction;
    }

    /**
     *
     * @return
     * The languages
     */
    public List<String> getLanguages() {
        return languages;
    }

    /**
     *
     * @param languages
     * The languages
     */
    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    /**
     *
     * @return
     * The lastAirDate
     */
    public String getLastAirDate() {
        return lastAirDate;
    }

    /**
     *
     * @param lastAirDate
     * The last_air_date
     */
    public void setLastAirDate(String lastAirDate) {
        this.lastAirDate = lastAirDate;
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
     * The networks
     */
    public List<Network> getNetworks() {
        return networks;
    }

    /**
     *
     * @param networks
     * The networks
     */
    public void setNetworks(List<Network> networks) {
        this.networks = networks;
    }

    /**
     *
     * @return
     * The numberOfEpisodes
     */
    public Integer getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    /**
     *
     * @param numberOfEpisodes
     * The number_of_episodes
     */
    public void setNumberOfEpisodes(Integer numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }

    /**
     *
     * @return
     * The numberOfSeasons
     */
    public Integer getNumberOfSeasons() {
        return numberOfSeasons;
    }

    /**
     *
     * @param numberOfSeasons
     * The number_of_seasons
     */
    public void setNumberOfSeasons(Integer numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    /**
     *
     * @return
     * The originCountry
     */
    public List<String> getOriginCountry() {
        return originCountry;
    }

    /**
     *
     * @param originCountry
     * The origin_country
     */
    public void setOriginCountry(List<String> originCountry) {
        this.originCountry = originCountry;
    }

    /**
     *
     * @return
     * The originalLanguage
     */
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    /**
     *
     * @param originalLanguage
     * The original_language
     */
    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    /**
     *
     * @return
     * The originalName
     */
    public String getOriginalName() {
        return originalName;
    }

    /**
     *
     * @param originalName
     * The original_name
     */
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    /**
     *
     * @return
     * The overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     *
     * @param overview
     * The overview
     */
    public void setOverview(String overview) {
        this.overview = overview;
    }

    /**
     *
     * @return
     * The popularity
     */
    public Double getPopularity() {
        return popularity;
    }

    /**
     *
     * @param popularity
     * The popularity
     */
    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    /**
     *
     * @return
     * The posterPath
     */
    public String getPosterPath() {
        return posterPath;
    }

    /**
     *
     * @param posterPath
     * The poster_path
     */
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    /**
     *
     * @return
     * The productionCompanies
     */
    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    /**
     *
     * @param productionCompanies
     * The production_companies
     */
    public void setProductionCompanies(List<ProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    /**
     *
     * @return
     * The seasons
     */
    public List<Season> getSeasons() {
        return seasons;
    }

    /**
     *
     * @param seasons
     * The seasons
     */
    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The voteAverage
     */
    public Double getVoteAverage() {
        return voteAverage;
    }

    /**
     *
     * @param voteAverage
     * The vote_average
     */
    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    /**
     *
     * @return
     * The voteCount
     */
    public Integer getVoteCount() {
        return voteCount;
    }

    /**
     *
     * @param voteCount
     * The vote_count
     */
    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    /**
     *
     * @return
     * The credits
     */
    public Credits getCredits() {
        return credits;
    }

    /**
     *
     * @param credits
     * The credits
     */
    public void setCredits(Credits credits) {
        this.credits = credits;
    }

    /**
     *
     * @return
     * The videos
     */
    public VideoList getVideos() {
        return videos;
    }

    /**
     *
     * @param videos
     * The videos
     */
    public void setVideos(VideoList videos) {
        this.videos = videos;
    }

    /**
     *
     * @return
     * The contentRatings
     */
    public ContentRatings getContentRatings() {
        return contentRatings;
    }

    /**
     *
     * @param contentRatings
     * The content_ratings
     */
    public void setContentRatings(ContentRatings contentRatings) {
        this.contentRatings = contentRatings;
    }

    public String getGenreListString() {


        String str = "";
        //Set up display string of list
        for(int i = 0; i< genres.size(); i++) {
            str += genres.get(i).getName() + ", ";
        }

        if(genres.size() > 0)
            str = str.substring(0,str.length() - 2);

        return str;
    }

    public ContentValues getContentValues() {

        ContentValues movieValues = new ContentValues();
        movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_ID, this.id);
        movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH, this.posterPath);
        movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_MEDIA_TYPE, ENT_TYPE_TV );
        movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_NAME,this.name);

        return movieValues;
    }


    protected TvShow(Parcel in) {
        backdropPath = in.readString();
        if (in.readByte() == 0x01) {
            createdBy = new ArrayList<CreatedBy>();
            in.readList(createdBy, CreatedBy.class.getClassLoader());
        } else {
            createdBy = null;
        }
        if (in.readByte() == 0x01) {
            episodeRunTime = new ArrayList<Integer>();
            in.readList(episodeRunTime, Integer.class.getClassLoader());
        } else {
            episodeRunTime = null;
        }
        firstAirDate = in.readString();
        if (in.readByte() == 0x01) {
            genres = new ArrayList<Genre>();
            in.readList(genres, Genre.class.getClassLoader());
        } else {
            genres = null;
        }
        homepage = in.readString();
        id = in.readByte() == 0x00 ? null : in.readInt();
        byte inProductionVal = in.readByte();
        inProduction = inProductionVal == 0x02 ? null : inProductionVal != 0x00;
        if (in.readByte() == 0x01) {
            languages = new ArrayList<String>();
            in.readList(languages, String.class.getClassLoader());
        } else {
            languages = null;
        }
        lastAirDate = in.readString();
        name = in.readString();
        if (in.readByte() == 0x01) {
            networks = new ArrayList<Network>();
            in.readList(networks, Network.class.getClassLoader());
        } else {
            networks = null;
        }
        numberOfEpisodes = in.readByte() == 0x00 ? null : in.readInt();
        numberOfSeasons = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            originCountry = new ArrayList<String>();
            in.readList(originCountry, String.class.getClassLoader());
        } else {
            originCountry = null;
        }
        originalLanguage = in.readString();
        originalName = in.readString();
        overview = in.readString();
        popularity = in.readByte() == 0x00 ? null : in.readDouble();
        posterPath = in.readString();
        if (in.readByte() == 0x01) {
            productionCompanies = new ArrayList<ProductionCompany>();
            in.readList(productionCompanies, ProductionCompany.class.getClassLoader());
        } else {
            productionCompanies = null;
        }
        if (in.readByte() == 0x01) {
            seasons = new ArrayList<Season>();
            in.readList(seasons, Season.class.getClassLoader());
        } else {
            seasons = null;
        }
        status = in.readString();
        type = in.readString();
        voteAverage = in.readByte() == 0x00 ? null : in.readDouble();
        voteCount = in.readByte() == 0x00 ? null : in.readInt();
        credits = (Credits) in.readValue(Credits.class.getClassLoader());
        videos = (VideoList) in.readValue(VideoList.class.getClassLoader());
        contentRatings = (ContentRatings) in.readValue(ContentRatings.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(backdropPath);
        if (createdBy == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(createdBy);
        }
        if (episodeRunTime == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(episodeRunTime);
        }
        dest.writeString(firstAirDate);
        if (genres == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(genres);
        }
        dest.writeString(homepage);
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        if (inProduction == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (inProduction ? 0x01 : 0x00));
        }
        if (languages == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(languages);
        }
        dest.writeString(lastAirDate);
        dest.writeString(name);
        if (networks == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(networks);
        }
        if (numberOfEpisodes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(numberOfEpisodes);
        }
        if (numberOfSeasons == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(numberOfSeasons);
        }
        if (originCountry == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(originCountry);
        }
        dest.writeString(originalLanguage);
        dest.writeString(originalName);
        dest.writeString(overview);
        if (popularity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(popularity);
        }
        dest.writeString(posterPath);
        if (productionCompanies == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(productionCompanies);
        }
        if (seasons == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(seasons);
        }
        dest.writeString(status);
        dest.writeString(type);
        if (voteAverage == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(voteAverage);
        }
        if (voteCount == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(voteCount);
        }
        dest.writeValue(credits);
        dest.writeValue(videos);
        dest.writeValue(contentRatings);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TvShow> CREATOR = new Parcelable.Creator<TvShow>() {
        @Override
        public TvShow createFromParcel(Parcel in) {
            return new TvShow(in);
        }

        @Override
        public TvShow[] newArray(int size) {
            return new TvShow[size];
        }
    };
}