package com.castify.tv.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoCard implements Parcelable {


    private String category;
    private boolean detailedCarousel;
    private String id;
    private String title;
    private String description;
    private String thumbnail;
    private String streamURL;
    private String videoQuality;
    private String audioQuality;
    private boolean is_live_streaming;
    private String views;
    private String likes;
    private String currentPlayPosition;
    private String videoDuration;
    private List<VideoCaptions> captions;

    @SerializedName("type") private VideoCard.Type mType;

    public List<VideoCaptions> getCaptions() {
        return captions;
    }

    public void setCaptions(List<VideoCaptions> captions) {
        this.captions = captions;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isDetailedCarousel() {
        return detailedCarousel;
    }

    public void setDetailedCarousel(boolean detailedCarousel) {
        this.detailedCarousel = detailedCarousel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getStreamURL() {
        return streamURL;
    }

    public void setStreamURL(String streamURL) {
        this.streamURL = streamURL;
    }

    public String getVideoQuality() {
        return videoQuality;
    }

    public void setVideoQuality(String videoQuality) {
        this.videoQuality = videoQuality;
    }

    public String getAudioQuality() {
        return audioQuality;
    }

    public void setAudioQuality(String audioQuality) {
        this.audioQuality = audioQuality;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public Type getmType() {
        return mType;
    }

    public void setmType(Type mType) {
        this.mType = mType;
    }

    public void setType(Type type) {
        mType = type;
    }

    public VideoCard.Type getType() {
        return mType;
    }

    public boolean isIs_live_streaming() {
        return is_live_streaming;
    }

    public void setIs_live_streaming(boolean is_live_streaming) {
        this.is_live_streaming = is_live_streaming;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public static Creator<VideoCard> getCREATOR() {
        return CREATOR;
    }

    public String getCurrentPlayPosition() {
        return currentPlayPosition;
    }

    public void setCurrentPlayPosition(String currentPlayPosition) {
        this.currentPlayPosition = currentPlayPosition;
    }


    public VideoCard(Parcel in) {
        category = in.readString();
        detailedCarousel = (in.readInt() == 1);
        id = in.readString();
        title = in.readString();
        description = in.readString();
        thumbnail = in.readString();
        streamURL = in.readString();
        videoQuality = in.readString();
        audioQuality = in.readString();
        is_live_streaming = (in.readInt() == 1);
        views = in.readString();
        likes = in.readString();
        videoDuration = in.readString();
        captions = (List<VideoCaptions>) in.readValue(VideoCaptions.class.getClassLoader());

//        currentPlayPosition = in.readString();

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(category);
        parcel.writeInt(detailedCarousel ? 1 :0);
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(thumbnail);
        parcel.writeString(streamURL);
        parcel.writeString(videoQuality);
        parcel.writeString(audioQuality);
        parcel.writeInt(is_live_streaming ? 1 : 0);
        parcel.writeString(views);
        parcel.writeString(likes);
        parcel.writeString(videoDuration);
        parcel.writeList(captions);

//        parcel.writeString(currentPlayPosition);
    }


    public static final Creator<VideoCard> CREATOR = new Creator<VideoCard>() {
        @Override
        public VideoCard createFromParcel(Parcel in) {
            return new VideoCard(in);
        }

        @Override
        public VideoCard[] newArray(int size) {
            return new VideoCard[size];
        }
    };


    public enum Type {

        MOVIE_COMPLETE,
        MOVIE,
        MOVIE_BASE,
        ICON,
        SQUARE_BIG,
        SINGLE_LINE,
        GAME,
        SQUARE_SMALL,
        DEFAULT,
        SIDE_INFO,
        SIDE_INFO_TEST_1,
        TEXT,
        CHARACTER,
        GRID_SQUARE,
        VIDEO_GRID

    }

}
