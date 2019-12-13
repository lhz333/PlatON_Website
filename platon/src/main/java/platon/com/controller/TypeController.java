package platon.com.controller;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import platon.com.po.PlatNewsCn;
import platon.com.po.PlatNewsEn;
import platon.com.po.PlatTypeCn;
import platon.com.po.PlatTypeEn;
import platon.com.service.PlatMediaService;
import platon.com.service.PlatNewsService;
import platon.com.service.PlatTypeService;
import platon.com.util.Const;

import com.jdbcTemplateTool.page.Pagination;
import com.util.JsonResp;

@Controller
@RequestMapping("type")
public class TypeController {
	
	private Logger logger=Logger.getLogger(TypeController.class);
	
	
	@Autowired
	private PlatMediaService platMediaService;
	
	@Autowired
	private PlatTypeService platTypeService;
	@Autowired
	private PlatNewsService platNewsService;
	
	
	/**
	 * 新增分类
	 * 中文
	 * @return
	 */
	@RequestMapping(value="addTypeCn",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Boolean> addPlatTypeCn(
			@RequestBody PlatTypeCn cn) {
		JsonResp<Boolean> json = new JsonResp<Boolean>();
		try {
			json.setData(false);
			PlatTypeCn params=new PlatTypeCn();
			params.setName(cn.getName());
			List<PlatTypeCn> names=platTypeService.findPlatTypeCnByParams(params);
			if(names!=null && names.size()>0){
				json.setCode(Const.PARAMS_3.getType());
				return json;
			}
			cn.setCreateTime(new Date());
			Integer i=platTypeService.insTypeCn(cn);
			if(i>0){
				json.setData(true);
				json.setCode(Const.PARAMS_0.getType());
			}else{
				json.setCode(Const.PARAMS_1.getType());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setCode(Const.PARAMS.getType());
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	/**
	 * 新增分类
	 * 英文
	 * @return
	 */
	@RequestMapping(value="addTypeEn",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Boolean> addPlatTypeEn(
			@RequestBody PlatTypeEn en) {
		JsonResp<Boolean> json = new JsonResp<Boolean>();
		try {
			json.setData(false);
			PlatTypeEn params=new PlatTypeEn();
			params.setName(en.getName());
			List<PlatTypeEn> names=platTypeService.findPlatTypeEnByParams(params);
			if(names!=null && names.size()>0){
				json.setCode(Const.PARAMS_3.getType());
				return json;
			}
			en.setCreateTime(new Date());
			Integer i=platTypeService.insTypeEn(en);
			if(i>0){
				json.setData(true);
				json.setCode(Const.PARAMS_0.getType());
			}else{
				json.setCode(Const.PARAMS_1.getType());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setCode(Const.PARAMS.getType());
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	

	/**
	 * 获取分类列表
	 * 中文
	 * @return
	 */
	@RequestMapping(value="pageTypeCn",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Pagination<PlatTypeCn>> pageTypeCn(
			@RequestBody PlatTypeCn cn) {
		JsonResp<Pagination<PlatTypeCn>> json = new JsonResp<Pagination<PlatTypeCn>>();
		try {
			Pagination<PlatTypeCn> list=platTypeService.findPlatTypeCn(cn);
			json.setData(list);
			json.setCode(Const.PARAMS_0.getType());
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setCode(Const.PARAMS.getType());
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	

	/**
	 * 获取分类列表
	 * 英文
	 * @return
	 */
	@RequestMapping(value="pageTypeEn",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Pagination<PlatTypeEn>> pageTypeEn(
			@RequestBody PlatTypeEn en) {
		JsonResp<Pagination<PlatTypeEn>> json = new JsonResp<Pagination<PlatTypeEn>>();
		try {
			Pagination<PlatTypeEn> list=platTypeService.findPlatTypeEn(en);
			json.setData(list);
			json.setCode(Const.PARAMS_0.getType());
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setCode(Const.PARAMS.getType());
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	

	/**
	 * 无分页
	 * 获取分类列表
	 * 中文
	 * 根据条件获取分类
	 * @return
	 */
	@RequestMapping(value="findTypeCn",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<List<PlatTypeCn>> findTypeCn(
			@RequestBody PlatTypeCn cn) {
		JsonResp<List<PlatTypeCn>> json = new JsonResp<>();
		try {
			List<PlatTypeCn> list=platTypeService.findPlatTypeCnByParams(cn);
			json.setData(list);
			json.setCode(Const.PARAMS_0.getType());
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setCode(Const.PARAMS.getType());
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	

	/**
	 * 无分页
	 * 获取分类列表
	 * 英文
	 * 根据条件获取分类
	 * id 
	 * name
	 * @return
	 */
	@RequestMapping(value="findTypeEn",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<List<PlatTypeEn>> findTypeEn(
			@RequestBody PlatTypeEn cn) {
		JsonResp<List<PlatTypeEn>> json = new JsonResp<>();
		try {
			List<PlatTypeEn> list=platTypeService.findPlatTypeEnByParams(cn);
			json.setData(list);
			json.setCode(Const.PARAMS_0.getType());
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setCode(Const.PARAMS.getType());
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	

	/**
	 * 无分页
	 * 获取分类列表
	 * 中文
	 * 根据条件获取分类
	 * @return
	 */
	@RequestMapping(value="findTypeCnOnline",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<List<PlatTypeCn>> findTypeCnOnline(
			@RequestBody PlatTypeCn cn) {
		JsonResp<List<PlatTypeCn>> json = new JsonResp<>();
		try {
			List<PlatTypeCn> list=platTypeService.findPlatTypeCnByParams(cn);
			json.setData(list);
			json.setCode(Const.PARAMS_0.getType());
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setCode(Const.PARAMS.getType());
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	

	/**
	 * 无分页
	 * 获取分类列表
	 * 英文
	 * 根据条件获取分类
	 * id 
	 * name
	 * @return
	 */
	@RequestMapping(value="findTypeEnOnline",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<List<PlatTypeEn>> findTypeEnOnline(
			@RequestBody PlatTypeEn cn) {
		JsonResp<List<PlatTypeEn>> json = new JsonResp<>();
		try {
			List<PlatTypeEn> list=platTypeService.findPlatTypeEnByParams(cn);
			json.setData(list);
			json.setCode(Const.PARAMS_0.getType());
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setCode(Const.PARAMS.getType());
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	
	
	
	/**
	 * 修改分类名称
	 * yignwen
	 * @return
	 */
	@RequestMapping(value="updTypeEnName",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Boolean> updTypeEnName(
			@RequestBody PlatTypeEn en) {
		JsonResp<Boolean> json = new JsonResp<Boolean>();
		try {
			json.setData(false);
			if(en.getName()==null || "".equals(en.getName())
					|| en.getId()==null ){
				json.setCode(Const.PARAMS_2.getType());
				return json;
			}
			PlatTypeEn params=new PlatTypeEn();
			params.setName(en.getName());
			List<PlatTypeEn> names=platTypeService.findPlatTypeEnByParams(params);
			if(names!=null && names.size()>0){
				
				json.setCode(Const.PARAMS_3.getType());
				return json;
			}
			Integer i=platTypeService.uptPlatTypeEn(en);
			if(i!=null && i>0){
				json.setData(true);
				json.setCode(Const.PARAMS_0.getType());
			}else{
				json.setCode(Const.PARAMS_1.getType());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setCode(Const.PARAMS.getType());
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	/**
	 * 修改分类名称
	 * 中文
	 * @return
	 */
	@RequestMapping(value="updTypeCnName",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Boolean> updTypeCnName(
			@RequestBody PlatTypeCn en) {
		JsonResp<Boolean> json = new JsonResp<Boolean>();
		try {
			json.setData(false);
			if(en.getName()==null || "".equals(en.getName())){
				json.setCode(Const.PARAMS_2.getType());
				return json;
			}
			PlatTypeCn params=new PlatTypeCn();
			params.setName(en.getName());
			List<PlatTypeCn> names=platTypeService.findPlatTypeCnByParams(params);
			if(names!=null && names.size()>0){
				json.setCode(Const.PARAMS_3.getType());
				return json;
			}
			Integer i=platTypeService.uptPlatTypeCn(en);
			if(i!=null && i>0){
				json.setData(true);
				json.setCode(Const.PARAMS_0.getType());
			}else{
				json.setCode(Const.PARAMS_1.getType());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setCode(Const.PARAMS.getType());
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	

	/**
	 * 删除中文分类
	 * @return
	 */
	@RequestMapping(value="removeTypeCn",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Boolean> removeTypeCn(
			@RequestBody PlatTypeCn cn) {
		JsonResp<Boolean> json = new JsonResp<Boolean>();
		try {
			json.setData(false);
			if(cn.getId()==null || "".equals(cn.getId())){
				json.setCode(Const.PARAMS_2.getType());
				return json;
			}
			List<PlatNewsCn> news=platNewsService.findPlatCnByTypeId(cn.getId());
			if(news!=null && news.size()>0){
				json.setCode(Const.PARAMS_4.getType());
				return json;
			}
			Integer i=platTypeService.removePlatTypeCn(cn);
			if(i!=null && i>0){
				json.setData(true);
				json.setCode(Const.PARAMS_0.getType());
			}else{
				json.setCode(Const.PARAMS_1.getType());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setCode(Const.PARAMS.getType());
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	

	/**
	 * 删除分类
	 * 英文
	 * @return
	 */
	@RequestMapping(value="removeTypeEn",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Boolean> removeTypeEn(
			@RequestBody PlatTypeEn en) {
		JsonResp<Boolean> json = new JsonResp<Boolean>();
		try {
			json.setData(false);
			if(en.getId()==null || "".equals(en.getId())){
				json.setCode(Const.PARAMS_2.getType());
				return json;
			}
			List<PlatNewsEn> news=platNewsService.findPlatEnByTypeId(en.getId());
			if(news!=null && news.size()>0){
				json.setCode(Const.PARAMS_4.getType());
				return json;
			}
			Integer i=platTypeService.removePlatTypeCn(en);
			if(i!=null && i>0){
				json.setData(true);
				json.setCode(Const.PARAMS_0.getType());
			}else{
				json.setCode(Const.PARAMS_1.getType());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setCode(Const.PARAMS.getType());
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
}
