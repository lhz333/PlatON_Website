package platon.com.po;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.util.Params;

@Table(name="plat_faq")
public class PlatFaq extends Params{
    private Integer id;
    private Integer sortId;
    private String problemCn;

    private String replyCn;
    
    private String problemEn;

    private String replyEn;
    
    private Integer status;

    private Integer complete;
    
    
    private String creator;
    
    private Date createTime;
    
    private Integer descNo;
    
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProblemCn() {
        return problemCn;
    }

    public void setProblemCn(String problemCn) {
        this.problemCn = problemCn == null ? null : problemCn.trim();
    }

    public String getProblemEn() {
        return problemEn;
    }

    public void setProblemEn(String problemEn) {
        this.problemEn = problemEn == null ? null : problemEn.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getComplete() {
        return complete;
    }

    public void setComplete(Integer complete) {
        this.complete = complete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public Integer getSortId() {
		return sortId;
	}

	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}
	@Transient
	public Integer getDescNo() {
		return descNo;
	}

	public void setDescNo(Integer descNo) {
		this.descNo = descNo;
	}

	public String getReplyCn() {
		return replyCn;
	}

	public void setReplyCn(String replyCn) {
		this.replyCn = replyCn;
	}

	public String getReplyEn() {
		return replyEn;
	}

	public void setReplyEn(String replyEn) {
		this.replyEn = replyEn;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
    
}