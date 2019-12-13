package platon.com.po;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;
@Table(name="plat_faq_history_opertion")
public class PlatFaqHistoryOpertion {
    private Integer id;

    private Integer faqId;

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

    public Integer getFaqId() {
        return faqId;
    }

    public void setFaqId(Integer faqId) {
        this.faqId = faqId;
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