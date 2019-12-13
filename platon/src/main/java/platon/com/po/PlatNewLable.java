package platon.com.po;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="plat_new_lable")
public class PlatNewLable {
	private Integer id;
	private Integer lableId;
	private Integer newCnId;
	private Integer newEnId;
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getNewCnId() {
		return newCnId;
	}
	public void setNewCnId(Integer newCnId) {
		this.newCnId = newCnId;
	}
	public Integer getNewEnId() {
		return newEnId;
	}
	public void setNewEnId(Integer newEnId) {
		this.newEnId = newEnId;
	}
	public Integer getLableId() {
		return lableId;
	}
	public void setLableId(Integer lableId) {
		this.lableId = lableId;
	}
	
	
	
}
