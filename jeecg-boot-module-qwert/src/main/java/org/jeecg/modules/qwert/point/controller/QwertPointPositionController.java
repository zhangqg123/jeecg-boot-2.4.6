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
import org.jeecg.modules.qwert.point.entity.PositionTreeModel;
import org.jeecg.modules.qwert.point.entity.QwertPointDev;
import org.jeecg.modules.qwert.point.entity.QwertPointPosition;
import org.jeecg.modules.qwert.point.service.IQwertPointDevService;
import org.jeecg.modules.qwert.point.service.IQwertPointPositionService;

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
 * @Description: qwert_point_position
 * @Author: jeecg-boot
 * @Date:   2021-11-23
 * @Version: V1.0
 */
@Api(tags="qwert_point_position")
@RestController
@RequestMapping("/point/qwertPointPosition")
@Slf4j
public class QwertPointPositionController extends JeecgController<QwertPointPosition, IQwertPointPositionService>{
	@Autowired
	private IQwertPointPositionService qwertPointPositionService;
	 @Autowired
	 private IQwertPointDevService qwertPointDevService;

	/**
	 * ??????????????????
	 *
	 * @param qwertPointPosition
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "qwert_point_position-??????????????????")
	@ApiOperation(value="qwert_point_position-??????????????????", notes="qwert_point_position-??????????????????")
	@GetMapping(value = "/rootList")
	public Result<?> queryPageList(QwertPointPosition qwertPointPosition,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String hasQuery = req.getParameter("hasQuery");
        if(hasQuery != null && "true".equals(hasQuery)){
            QueryWrapper<QwertPointPosition> queryWrapper =  QueryGenerator.initQueryWrapper(qwertPointPosition, req.getParameterMap());
            List<QwertPointPosition> list = qwertPointPositionService.queryTreeListNoPage(queryWrapper);
            IPage<QwertPointPosition> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        }else{
            String parentId = qwertPointPosition.getPid();
            if (oConvertUtils.isEmpty(parentId)) {
                parentId = "0";
            }
            qwertPointPosition.setPid(null);
            QueryWrapper<QwertPointPosition> queryWrapper = QueryGenerator.initQueryWrapper(qwertPointPosition, req.getParameterMap());
            // ?????? eq ??????????????????
            queryWrapper.eq("pid", parentId);
            Page<QwertPointPosition> page = new Page<QwertPointPosition>(pageNo, pageSize);
            IPage<QwertPointPosition> pageList = qwertPointPositionService.page(page, queryWrapper);
            return Result.OK(pageList);
        }
	}

	 /**
      * ???????????????
      * @param qwertPointPosition
      * @param req
      * @return
      */
	@AutoLog(value = "qwert_point_position-???????????????")
	@ApiOperation(value="qwert_point_position-???????????????", notes="qwert_point_position-???????????????")
	@GetMapping(value = "/childList")
	public Result<?> queryPageList(QwertPointPosition qwertPointPosition,HttpServletRequest req) {
		QueryWrapper<QwertPointPosition> queryWrapper = QueryGenerator.initQueryWrapper(qwertPointPosition, req.getParameterMap());
		List<QwertPointPosition> list = qwertPointPositionService.list(queryWrapper);
		IPage<QwertPointPosition> pageList = new Page<>(1, 10, list.size());
        pageList.setRecords(list);
		return Result.OK(pageList);
	}

    /**
      * ?????????????????????
      * @param parentIds ???ID????????????????????????????????????
      * @return ?????? IPage
      * @param parentIds
      * @return
      */
	@AutoLog(value = "qwert_point_position-?????????????????????")
    @ApiOperation(value="qwert_point_position-?????????????????????", notes="qwert_point_position-?????????????????????")
    @GetMapping("/getChildListBatch")
    public Result getChildListBatch(@RequestParam("parentIds") String parentIds) {
        try {
            QueryWrapper<QwertPointPosition> queryWrapper = new QueryWrapper<>();
            List<String> parentIdList = Arrays.asList(parentIds.split(","));
            queryWrapper.in("pid", parentIdList);
            List<QwertPointPosition> list = qwertPointPositionService.list(queryWrapper);
            IPage<QwertPointPosition> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("??????????????????????????????" + e.getMessage());
        }
    }
	
	/**
	 *   ??????
	 *
	 * @param qwertPointPosition
	 * @return
	 */
	@AutoLog(value = "qwert_point_position-??????")
	@ApiOperation(value="qwert_point_position-??????", notes="qwert_point_position-??????")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody QwertPointPosition qwertPointPosition) {
		qwertPointPositionService.addQwertPointPosition(qwertPointPosition);
		return Result.OK("???????????????");
	}
	
	/**
	 *  ??????
	 *
	 * @param qwertPointPosition
	 * @return
	 */
	@AutoLog(value = "qwert_point_position-??????")
	@ApiOperation(value="qwert_point_position-??????", notes="qwert_point_position-??????")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody QwertPointPosition qwertPointPosition) {
		qwertPointPositionService.updateQwertPointPosition(qwertPointPosition);
		return Result.OK("????????????!");
	}
	
	/**
	 *   ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "qwert_point_position-??????id??????")
	@ApiOperation(value="qwert_point_position-??????id??????", notes="qwert_point_position-??????id??????")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		qwertPointPositionService.deleteQwertPointPosition(id);
		return Result.OK("????????????!");
	}
	
	/**
	 *  ????????????
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "qwert_point_position-????????????")
	@ApiOperation(value="qwert_point_position-????????????", notes="qwert_point_position-????????????")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.qwertPointPositionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("?????????????????????");
	}
	
	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "qwert_point_position-??????id??????")
	@ApiOperation(value="qwert_point_position-??????id??????", notes="qwert_point_position-??????id??????")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		QwertPointPosition qwertPointPosition = qwertPointPositionService.getById(id);
		if(qwertPointPosition==null) {
			return Result.error("?????????????????????");
		}
		return Result.OK(qwertPointPosition);
	}

    /**
    * ??????excel
    *
    * @param request
    * @param qwertPointPosition
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, QwertPointPosition qwertPointPosition) {
		return super.exportXls(request, qwertPointPosition, QwertPointPosition.class, "qwert_point_position");
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
		return super.importExcel(request, response, QwertPointPosition.class);
    }

	 @RequestMapping(value = "/queryPositionTreeList", method = RequestMethod.GET)
	 public Result<List<PositionTreeModel>> queryPositionTreeList() {
		 Result<List<PositionTreeModel>> result = new Result<>();
		 LambdaQueryWrapper<QwertPointDev> query = new LambdaQueryWrapper<QwertPointDev>();
		 List<QwertPointDev> qpdList = qwertPointDevService.list(query);
		 try {
			 List<PositionTreeModel> list = qwertPointPositionService.queryPositionTreeList(qpdList);
			 result.setResult(list);
			 result.setSuccess(true);
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
		 }
		 return result;
	 }

}
