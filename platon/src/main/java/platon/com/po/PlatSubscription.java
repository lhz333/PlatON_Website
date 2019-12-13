package platon.com.po;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.util.Params;

@Table(name="plat_subscription")
public class PlatSubscription extends Params{
    private Integer id;

    private String email;

    private String name;

    private String companyName;

    private Integer idType;
    
    private String idTypes;

    private Integer status;

    private Date createTime;
    
    private String ids;
    
    private Integer descNo;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public Integer getIdType() {
        return idType;
    }

    public void setIdType(Integer idType) {
        this.idType = idType;
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
    @Transient
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	@Transient
	public Integer getDescNo() {
		return descNo;
	}

	public void setDescNo(Integer descNo) {
		this.descNo = descNo;
	}

	public String getIdTypes() {
		return idTypes;
	}

	public void setIdTypes(String idTypes) {
		this.idTypes = idTypes;
	}
    
}