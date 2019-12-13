package platon.com.po;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.util.Params;
@Table(name="plat_news_cn_en")
public class PlatNewsCnEn extends Params{
	

    private Integer id;
    
    private Integer cnId;
    
    private Integer enId;
    
    private String creator;

    private Integer status;

    private Date createTime;

    
    
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	
	public Integer getCnId() {
		return cnId;
	}

	public void setCnId(Integer cnId) {
		this.cnId = cnId;
	}

	public Integer getEnId() {
		return enId;
	}

	public void setEnId(Integer enId) {
		this.enId = enId;
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

  
    
}