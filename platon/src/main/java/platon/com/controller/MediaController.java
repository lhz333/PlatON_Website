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

import platon.com.po.PlatMedia;
import platon.com.service.PlatMediaService;
import platon.com.util.Const;

import com.jdbcTemplateTool.page.Pagination;
import com.util.JsonResp;

@Controller
@RequestMapping("media")
public class MediaController {

	
	private Logger logger=Logger.getLogger(MediaController.class);
	
	
	@Autowired
	private PlatMediaService platMediaService;
	
	/**
	 * 新增媒体
	 * @return
	 */
	@RequestMapping(value="addMedia",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Boolean> addMedia(
			@RequestBody PlatMedia media) {
		JsonResp<Boolean> json = new JsonResp<Boolean>();
		json.setData(false);
		try {
			if(media.getName()==null || media.getLogoUrl()==null || media.getTitle()==null
					 || media.getLink()==null || media.getReTime()==null){
				json.setCode(Const.PARAMS_2.getType());
				return json;
			}
			media.setCreateTime(new Date());
			media.setReleaseTime(new Date());
			platMediaService.insMedia(media);
			json.setData(true);
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
	 * 修改媒体状态
	 * @return
	 */
	@RequestMapping(value="uptMediaStatus",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Boolean> uptMediaStatus(
			@RequestBody PlatMedia media) {
		JsonResp<Boolean> json = new JsonResp<Boolean>();
		try {
			json.setData(false);
			if(media.getId()==null || media.getStatus()==null){
				json.setCode(Const.PARAMS_2.getType());
				return json;
			}
			platMediaService.uptMediaStatus(media);
			json.setData(true);
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
	 * 获取媒体名称列表
	 * @return
	 */
	@RequestMapping(value="findMediaNames",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<List<PlatMedia>> findMediaNames() {
		JsonResp<List<PlatMedia>> json = new JsonResp<List<PlatMedia>>();
		try {
			List<PlatMedia> list=platMediaService.findPlatMedia();
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
	 * 修改媒体信息
	 * @return
	 */
	@RequestMapping(value="uptMedia",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Boolean> uptMedia(
			@RequestBody PlatMedia media) {
		JsonResp<Boolean> json = new JsonResp<Boolean>();
		try {
			if(media.getName()==null || media.getLogoUrl()==null || media.getTitle()==null
					 || media.getLink()==null || media.getReleaseTime()==null || media.getId()==null){
				json.setCode(Const.PARAMS_2.getType());
				return json;
			}
			platMediaService.uptMedia(media);
			json.setData(true);
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
	 * 获取媒体列表
	 * 官网
	 * @return
	 */
	@RequestMapping(value="findPlatMediaOnline",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Pagination<PlatMedia>> findPlatMediaOnline(
			@RequestBody PlatMedia media) {
		JsonResp<Pagination<PlatMedia>> json = new JsonResp<Pagination<PlatMedia>>();
		try {
			media.setStatus(Const.STATUS_TRUE.getType());
			Pagination<PlatMedia> list=platMediaService.findPlatMedia(media);
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
	 * 获取媒体列表
	 * @return
	 */
	@RequestMapping(value="findPlatMedia",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<Pagination<PlatMedia>> findPlatMedia(
			@RequestBody PlatMedia media) {
		JsonResp<Pagination<PlatMedia>> json = new JsonResp<Pagination<PlatMedia>>();
		try {
			Pagination<PlatMedia> list=platMediaService.findPlatMedia(media);
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
	 * 获取单个媒体
	 * @return
	 */
	@RequestMapping(value="findPlatMediaById",method = RequestMethod.POST)
	@ResponseBody
	public JsonResp<PlatMedia> findPlatMediaById(
			@RequestBody PlatMedia media) {
		JsonResp<PlatMedia> json = new JsonResp<PlatMedia>();
		try {
			PlatMedia res=platMediaService.findPlatMediaById(media);
			json.setData(res);
			json.setCode(Const.PARAMS_0.getType());
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setCode(Const.PARAMS.getType());
			json.setMessage("系统异常，请联系管理员");
		}
		return json;
	}
	
	
	
	
}
