package com.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.dao.CategoryMapper;
import com.mall.dao.ProductMapper;
import com.mall.pojo.Category;
import com.mall.pojo.Product;
import com.mall.service.ICategoryService;
import com.mall.service.IProductService;
import com.mall.util.PropertiesUtil;
import com.mall.util.TimeUtil;
import com.mall.vo.ProductDetilVo;
import com.mall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("iProductService")
public class IProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    public ServerResponse saveAndUpdate(Product product){
        if(product != null){
            if(StringUtils.isNotBlank(product.getSubImages())){
               String[] images = product.getSubImages().split(",");
               if(images.length > 0){
                   product.setMainImage(images[0]);
               }
            }
            if(product.getId() != null){
                int rowResult=productMapper.updateByPrimaryKey(product);
                if(rowResult > 0){
                    return ServerResponse.creatBySuccessMessage("商品更新成功");
                }
                return ServerResponse.creatByErrorMessage("产品更新失败");
            }else{
                int rowResult=productMapper.insert(product);
                if(rowResult > 0){
                    return ServerResponse.creatBySuccessMessage("商品增加成功");
                }
                return ServerResponse.creatByErrorMessage("产品增加失败");
            }
        }
        return ServerResponse.creatByErrorMessage("商品参数有误");
    }


    public ServerResponse productPower ( Integer productId, Integer Status){
        if(productId == null || Status == null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(Status);
        int rowR = productMapper.updateByPrimaryKeySelective(product);
        if(rowR > 0){
            return ServerResponse.creatBySuccessMessage("产品状态更新成功");
        }
        return  ServerResponse.creatByErrorMessage("产品状态更新失败");
    }

    public ServerResponse getDetail(Integer productId){
        if(productId == null ){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponse.creatByErrorMessage("产品删除或者已经下架");
        }
        ProductDetilVo productDetilVo = assembleProductDetilVo(product);
        return ServerResponse.creatBySuccess(productDetilVo);
    }

    private ProductDetilVo assembleProductDetilVo(Product product){
        ProductDetilVo productDetilVo = new ProductDetilVo();
        productDetilVo.setId(product.getId());
        productDetilVo.setCategoryId(product.getCategoryId());
        productDetilVo.setDetail(product.getDetail());
        productDetilVo.setName(product.getName());
        productDetilVo.setMainImage(product.getMainImage());
        productDetilVo.setSubImages(product.getSubImages());
        productDetilVo.setSubtitle(product.getSubtitle());
        productDetilVo.setStatus(product.getStatus());
        productDetilVo.setStock(product.getStock());
        productDetilVo.setPrice(product.getPrice());

        productDetilVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category != null){
            productDetilVo.setCategoryParentId(category.getParentId());
        }else{
            productDetilVo.setCategoryParentId(0);
        }

        productDetilVo.setCreateTime(TimeUtil.dateToStr(product.getCreateTime()));
        productDetilVo.setUpdateTime(TimeUtil.dateToStr(product.getUpdateTime()));

        return productDetilVo;
    }

    public ServerResponse getListProduct(Integer pageNum,Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectList();
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem: productList) {
            productListVoList.add(assembleProductListVo(productItem));
        }
        PageInfo result = new PageInfo(productList);//todo 这里应该是写的比较麻烦，下面可以不写直接在这赋值吗？
        result.setList(productListVoList);
        return ServerResponse.creatBySuccess(result);
    }
    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setName(product.getName());
        productListVo.setPrice(product.getPrice());
        productListVo.setStatus(product.getStatus());
        productListVo.setSubtitle(product.getSubtitle());
        return productListVo;
    }


    public ServerResponse searchProduct(String productName,Integer productId,Integer pageNum,Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.searchProduct(productName,productId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem: productList) {
            productListVoList.add(assembleProductListVo(productItem));
        }
        PageInfo result = new PageInfo(productList);//todo 这里应该是写的比较麻烦，下面可以不写直接在这赋值吗？
        result.setList(productListVoList);
        return ServerResponse.creatBySuccess(result);
    }
//前台获取商品信息
        public ServerResponse Detail(Integer productId){
            if(productId == null ){
                return ServerResponse.creatByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
            }
            Product product = productMapper.selectByPrimaryKey(productId);
            if(product == null){
                return ServerResponse.creatByErrorMessage("产品删除或者已经下架");
            }
            if(product.getStatus()!= Const.ProductStatus.ON_STATUS.getCode()){
                return ServerResponse.creatByErrorMessage("产品不存在");
            }
            ProductDetilVo productDetilVo = assembleProductDetilVo(product);
            return ServerResponse.creatBySuccess(productDetilVo);
        }

//获取商品信息并排序
        public ServerResponse listByKeywordAndCategoryId (String keyword,Integer categoryId,int pageNum,int pageSize ,String orderBy){
            //校验参数
            if(StringUtils.isBlank(keyword)&& categoryId == null){
                return ServerResponse.creatByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
            }
            List<Integer> categoryIdlist = new ArrayList<Integer>();
            if(categoryId != null) {
                Category category = categoryMapper.selectByPrimaryKey(categoryId);
                if (category == null && StringUtils.isBlank(keyword)) {
                    //这里也要返回一个结果集,虽然是空的
                    PageHelper.startPage(pageNum, pageSize);
                    List<ProductDetilVo> productDetilVoList = Lists.newArrayList();
                    PageInfo result = new PageInfo(productDetilVoList);
                    return ServerResponse.creatBySuccess(result);
                }
                //查询平级的categoryid要调用categoryService
                categoryIdlist = iCategoryService.selectAllChildByParent(category.getId()).getData();
            }
            if(StringUtils.isNotBlank(keyword)){
                keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
            }
            //数据校验完成开始排序
            PageHelper.startPage(pageNum,pageSize);//分页配置好
            if(StringUtils.isNotBlank(orderBy)){
                //配置排序
                if(Const.ProdectListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                    String[] stringOrderBy = orderBy.split("_");
                    PageHelper.orderBy(stringOrderBy[0] + " " +stringOrderBy[1]);
                }
            }
            //做一个productlist的查询,并对传入参数进行校验
            List<Product> productList = productMapper.productListByKeywordCategoryIdList(
                    StringUtils.isBlank(keyword)?null:keyword,
                    categoryIdlist.size()==0?null:categoryIdlist);

            List<ProductListVo> productListVoList = Lists.newArrayList();
            for(Product prodect: productList){
                    productListVoList.add(assembleProductListVo(prodect));
            }
            PageInfo resultList = new PageInfo(productList);
            resultList.setList(productListVoList);
            return ServerResponse.creatBySuccess(resultList);
        }

}
