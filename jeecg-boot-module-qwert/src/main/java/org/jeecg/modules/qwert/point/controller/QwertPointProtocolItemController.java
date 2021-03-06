package org.jeecg.modules.qwert.point.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.qwert.point.entity.ProtocolTreeModel;
import org.jeecg.modules.qwert.point.entity.QwertPointPosition;
import org.jeecg.modules.qwert.point.entity.QwertPointProtocol;
import org.jeecg.modules.qwert.point.entity.QwertPointProtocolItem;
import org.jeecg.modules.qwert.point.service.IQwertPointProtocolItemService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: qwert_point_protocol_item
 * @Author: jeecg-boot
 * @Date:   2021-11-18
 * @Version: V1.0
 */
@Api(tags="qwert_point_protocol_item")
@RestController
@RequestMapping("/point/qwertPointProtocolItem")
@Slf4j
public class QwertPointProtocolItemController extends JeecgController<QwertPointProtocolItem, IQwertPointProtocolItemService> {
	@Autowired
	private IQwertPointProtocolItemService qwertPointProtocolItemService;
	
	/**
	 * ??????????????????
	 *
	 * @param qwertPointProtocolItem
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "qwert_point_protocol_item-??????????????????")
	@ApiOperation(value="qwert_point_protocol_item-??????????????????", notes="qwert_point_protocol_item-??????????????????")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(QwertPointProtocolItem qwertPointProtocolItem,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<QwertPointProtocolItem> queryWrapper = QueryGenerator.initQueryWrapper(qwertPointProtocolItem, req.getParameterMap());
		Page<QwertPointProtocolItem> page = new Page<QwertPointProtocolItem>(pageNo, pageSize);
		IPage<QwertPointProtocolItem> pageList = qwertPointProtocolItemService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   ??????
	 *
	 * @param qwertPointProtocolItem
	 * @return
	 */
	@AutoLog(value = "qwert_point_protocol_item-??????")
	@ApiOperation(value="qwert_point_protocol_item-??????", notes="qwert_point_protocol_item-??????")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody QwertPointProtocolItem qwertPointProtocolItem) {
		qwertPointProtocolItemService.save(qwertPointProtocolItem);
		return Result.OK("???????????????");
	}
	
	/**
	 *  ??????
	 *
	 * @param qwertPointProtocolItem
	 * @return
	 */
	@AutoLog(value = "qwert_point_protocol_item-??????")
	@ApiOperation(value="qwert_point_protocol_item-??????", notes="qwert_point_protocol_item-??????")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody QwertPointProtocolItem qwertPointProtocolItem) {
		qwertPointProtocolItemService.updateById(qwertPointProtocolItem);
		return Result.OK("????????????!");
	}
	
	/**
	 *   ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "qwert_point_protocol_item-??????id??????")
	@ApiOperation(value="qwert_point_protocol_item-??????id??????", notes="qwert_point_protocol_item-??????id??????")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		qwertPointProtocolItemService.removeById(id);
		return Result.OK("????????????!");
	}
	
	/**
	 *  ????????????
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "qwert_point_protocol_item-????????????")
	@ApiOperation(value="qwert_point_protocol_item-????????????", notes="qwert_point_protocol_item-????????????")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.qwertPointProtocolItemService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("??????????????????!");
	}
	
	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "qwert_point_protocol_item-??????id??????")
	@ApiOperation(value="qwert_point_protocol_item-??????id??????", notes="qwert_point_protocol_item-??????id??????")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		QwertPointProtocolItem qwertPointProtocolItem = qwertPointProtocolItemService.getById(id);
		if(qwertPointProtocolItem==null) {
			return Result.error("?????????????????????");
		}
		return Result.OK(qwertPointProtocolItem);
	}

    /**
    * ??????excel
    *
    * @param request
    * @param qwertPointProtocolItem
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, QwertPointProtocolItem qwertPointProtocolItem) {
        return super.exportXls(request, qwertPointProtocolItem, QwertPointProtocolItem.class, "qwert_point_protocol_item");
    }

    /**
      * ??????excel????????????
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, QwertPointProtocolItem.class);
    }
	 @RequestMapping(value = "/queryprotocolList", method = RequestMethod.GET)
	 public Result<List<QwertPointProtocolItem>> queryProtocolTreeList() {
		 Result<List<QwertPointProtocolItem>> result = new Result<>();
		 LambdaQueryWrapper<QwertPointProtocolItem> query = new LambdaQueryWrapper<QwertPointProtocolItem>();
//		 query.orderByAsc(QwertPointProtocolItem::getItemNo);
		 try {
			 List<QwertPointProtocolItem> list = qwertPointProtocolItemService.list(query);
			 result.setResult(list);
			 result.setSuccess(true);
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
		 }
		 return result;
	 }

}
