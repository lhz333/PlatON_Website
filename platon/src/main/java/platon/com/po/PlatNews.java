package platon.com.po;

import java.util.Date;

import com.util.Params;
public class PlatNews extends Params{

    private Integer id;
    
    private Integer sortId;
    
    private String title;

    private String author;

    private Date releaseTime;

    private String topImgUrl;

    private String imgUrl;

    private String creator;

    private Integer status;

    private Date createTime;

    private String newsText;
    
    private Integer complete;
    
    private Integer descNo;
    
    private Integer bindStatus; //关联的英文版本
    
    private Integer upSortId; //上一篇
    private Integer nextSortId;//下一篇
    
    
    private Integer typeId; //分类id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSortId() {
		return sortId;
	}

	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public String getTopImgUrl() {
		return topImgUrl;
	}

	public void setTopImgUrl(String topImgUrl) {
		this.topImgUrl = topImgUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getNewsText() {
		return newsText;
	}

	public void setNewsText(String newsText) {
		this.newsText = newsText;
	}

	public Integer getComplete() {
		return complete;
	}

	public void setComplete(Integer complete) {
		this.complete = complete;
	}

	public Integer getDescNo() {
		return descNo;
	}

	public void setDescNo(Integer descNo) {
		this.descNo = descNo;
	}

	public Integer getBindStatus() {
		return bindStatus;
	}

	public void setBindStatus(Integer bindStatus) {
		this.bindStatus = bindStatus;
	}

	public Integer getUpSortId() {
		return upSortId;
	}

	public void setUpSortId(Integer upSortId) {
		this.upSortId = upSortId;
	}

	public Integer getNextSortId() {
		return nextSortId;
	}

	public void setNextSortId(Integer nextSortId) {
		this.nextSortId = nextSortId;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
    
  
    
	
	
    
}