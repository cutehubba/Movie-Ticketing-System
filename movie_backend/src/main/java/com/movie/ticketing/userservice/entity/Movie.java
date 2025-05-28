package com.movie.ticketing.userservice.entity;

import java.time.LocalDate;

public class Movie {
    private Long id;                // 电影的唯一标识符
    private String title;           // 电影标题
    private LocalDate releaseDate;     // 上映日期
    private Integer duration;       // 电影时长（分钟）
    private String genre;           // 电影类型（如动作、喜剧等）
    private String director;        // 导演
    private String description;     // 电影简介
    private String releaseStatus;   // 上映状态（如已上映、未上映等）
    private Double rating;          // 电影评分
    private String posterUrl;       // 海报图片链接
    private String trailerUrl;      // 预告片链接

    // 默认构造方法
    public Movie() {}

    // 带参数的构造方法
    public Movie(Long id, String title, LocalDate releaseDate, Integer duration, String genre,
                 String director, String description, String releaseStatus,
                 Double rating, String posterUrl, String trailerUrl) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.genre = genre;
        this.director = director;
        this.description = description;
        this.releaseStatus = releaseStatus;
        this.rating = rating;
        this.posterUrl = posterUrl;
        this.trailerUrl = trailerUrl;
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseStatus() {
        return releaseStatus;
    }

    public void setReleaseStatus(String releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }
}