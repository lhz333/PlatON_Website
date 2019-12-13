package platon.com.po;

import java.util.Date;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.util.Params;
@Table(name="plat_news_cn")
public class PlatNewsCn extends Params{
	

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
    
    private String newsDescription; //新闻描述
    private String newsText;
    
    private Integer complete;
    
    private Integer descNo;
    
    private Integer bindStatus; //关联的英文版本
    private Integer upSortId; //上一篇
    private Integer nextSortId;//下一篇
    
    private Integer typeId; //分类id
    private String typeName; //分类名称
    private List<PlatLable> lables;  //标签
    
    private List<PlatTypeCn> typsList;  //用户查询 观点分类时，去掉 新闻分类和活动
    
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
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
        this.topImgUrl = topImgUrl == null ? null : topImgUrl.trim();
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
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
        this.newsText = newsText == null ? null : newsText.trim();
    }
    @Transient
	public Integer getDescNo() {
		return descNo;
	}

	public void setDescNo(Integer descNo) {
		this.descNo = descNo;
	}

	public Integer getSortId() {
		return sortId;
	}

	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}

	public Integer getComplete() {
		return complete;
	}

	public void setComplete(Integer complete) {
		this.complete = complete;
	}
	@Transient
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
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	
	
	@Transient
	public List<PlatLable> getLables() {
		return lables;
	}

	public void setLables(List<PlatLable> lables) {
		this.lables = lables;
	}

	public String getNewsDescription() {
		return newsDescription;
	}

	public void setNewsDescription(String newsDescription) {
		this.newsDescription = newsDescription;
	}

	public List<PlatTypeCn> getTypsList() {
		return typsList;
	}

	public void setTypsList(List<PlatTypeCn> typsList) {
		this.typsList = typsList;
	}
    
  
    
	
    
}