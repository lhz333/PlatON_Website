package platon.com.util;

public enum Const {
	
	opertion_del(-1,"删除"),
	opertion_create(0,"创建"),
	opertion_push(1,"发布 "),
	opertion_edit(2,"编辑"),
	opertion_revoke(3,"撤回"),
	opertion_sort(4,"调整顺序"),
	
	ID_TYPE_kaifa(1,"开发者"),
	ID_TYPE_touzi(2,"投资者"),
	ID_TYPE_meiti(3,"媒体"),
	ID_TYPE_qita(4,"其他"),
	
	
	STATUS_TRUE(1,"有效"), //or 发布的
	STATUS_FALSE(0,"无效"), //or  未发布
	
	PARAMS(-1,"系统异常"),
	PARAMS_0(0,"成功"),
	PARAMS_1(1,"失败"),
	PARAMS_2(2,"参数不能为空"),
	PARAMS_3(3,"不能重复"),
	PARAMS_4(4,"关联数据未处理");
	
	
	private Integer type;
	private String name;
	
	
	
	public Integer getType() {
		return type;
	}




	public void setType(Integer type) {
		this.type = type;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	private Const(Integer type, String name) {
		this.type = type;
		this.name = name;
	}
	
	
	
	
}
