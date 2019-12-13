package platon.com.po;

import java.util.Date;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Table;
@Table(name="plat_menu")
public class PlatMenu {
    private Integer id;

    private Integer parentId;

    private String name;

    private String url;

    private Integer status;

    private Date createTime;
    
    private String icon;
    
    private List<PlatMenu> children;
    
    
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
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

	public List<PlatMenu> getChildren() {
		return children;
	}

	public void setChildren(List<PlatMenu> children) {
		this.children = children;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
    
}