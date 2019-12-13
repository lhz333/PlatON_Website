package platon.com.po;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;
@Table(name="plat_news_history_opertion_en")
public class PlatNewsHistoryOpertionEn {
    private Integer id;

    private Integer newId;

    private String creator;

    private Integer status;

    private Date createTime;
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    

    public Integer getNewId() {
		return newId;
	}

	public void setNewId(Integer newId) {
		this.newId = newId;
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
}