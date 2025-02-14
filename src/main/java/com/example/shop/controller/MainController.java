package com.example.shop.controller;

import com.example.shop.dto.*;
import com.example.shop.etc.Pagination;
import com.example.shop.etc.ScheduleUtil;
import com.example.shop.service.ShopService;
import com.example.shop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.util.*;

@Controller
public class MainController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ScheduleUtil.class);

    @Autowired
    ShopService shopService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String goindex(){
        return "redirect:/main";
    }

    public String calKrPrice(int totalPrice){
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);

        String formattedAmount = numberFormat.format(totalPrice);

        return formattedAmount;
    }

    @GetMapping("/main")
    public String mainPage(Model model){
        int totalPrice=0;
        int totalCnt=0;

        try{
           Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("islogin",loginid);

            List<Cart> cartList=shopService.getCartListByUserID(loginid);

            for(int i=0;i<cartList.size();i++){
                Product product=shopService.getProductByCartID(cartList.get(i).getId());
                totalPrice+=cartList.get(i).getPd_count()*(product.getPd_price()-(product.getPd_price()*product.getPd_sale_percent()/100));
                totalCnt+=cartList.get(i).getPd_count();
            }

        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        model.addAttribute("totalPrice",calKrPrice(totalPrice));
        model.addAttribute("totalCnt",totalCnt);

        List<Product> bestList=shopService.getBestList();
        List<Product> topList=shopService.getListForRank("top");
        List<Product> viewList=shopService.getListForRank("view");
        List<Product> ratingList=shopService.getListForRank("rating");

        model.addAttribute("topList",topList);
        model.addAttribute("viewList",viewList);
        model.addAttribute("ratingList",ratingList);
        model.addAttribute("bestList",bestList);

        //test


        return "mainPage";
    }

    @GetMapping("/single/{id}")
    public String single(@PathVariable int id, Model model){
        int totalPrice=0;
        int totalCnt=0;

        try{
            Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("islogin",loginid);

            List<Cart> cartList=shopService.getCartListByUserID(loginid);

            for(int i=0;i<cartList.size();i++){
                Product product=shopService.getProductByCartID(cartList.get(i).getId());
                totalPrice+=cartList.get(i).getPd_count()*(product.getPd_price()-(product.getPd_price()*product.getPd_sale_percent()/100));
                totalCnt+=cartList.get(i).getPd_count();

            }

        } catch (Exception e){

        }


        List<Review> reviewList=userService.getSingleReview(id);

        for(int i=0;i<reviewList.size();i++){
            String userid=userService.maskString(userService.getUseridWithLoginid(Long.valueOf(reviewList.get(i).getId())));
            reviewList.get(i).setId(userid);

        }


        shopService.updateView(id);

        model.addAttribute("reviewList",reviewList);
        model.addAttribute("totalPrice",calKrPrice(totalPrice));
        model.addAttribute("totalCnt",totalCnt);

        Product thisProduct=shopService.getProduct(id);
        thisProduct.setPd_newprice(thisProduct.getPd_price()-(thisProduct.getPd_price()*thisProduct.getPd_sale_percent()/100));
        List<Product>categoryList=shopService.getCategoryList(thisProduct.getCategory());


        for(int i=0;i<categoryList.size();i++){
            if(id==categoryList.get(i).getId()){
                categoryList.remove(i);
            }
        }

        Collections.shuffle(categoryList);

        categoryList.subList(3,categoryList.size()).clear();

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);

        String formattedAmount = numberFormat.format(thisProduct.getPd_newprice());
        thisProduct.setKr_price(formattedAmount);

        model.addAttribute("product",thisProduct);
        model.addAttribute("list",categoryList);

        return "single-product";
    }


    @GetMapping("/shop/{page}")
    public String home(Model model, @PathVariable int page) {
        int totalPrice=0;
        int totalCnt=0;

        try{
            Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("islogin",loginid);

            List<Cart> cartList=shopService.getCartListByUserID(loginid);

            for(int i=0;i<cartList.size();i++){
                Product product=shopService.getProductByCartID(cartList.get(i).getId());
                totalPrice+=cartList.get(i).getPd_count()*(product.getPd_price()-(product.getPd_price()*product.getPd_sale_percent()/100));
                totalCnt+=cartList.get(i).getPd_count();

            }



        } catch (Exception e){

        }

        model.addAttribute("totalPrice",calKrPrice(totalPrice));
        model.addAttribute("totalCnt",totalCnt);

        int totalListCnt = shopService.getCnt();

        if(totalListCnt<9){
            model.addAttribute("n",1);
        } else if (totalListCnt>=9) {
            double howmanyPage=(double)totalListCnt/8;
            int totalpage=(int)Math.ceil(howmanyPage);
            model.addAttribute("n",totalpage);
        }

        Pagination pagination = new Pagination(totalListCnt, page);

       
        int startIndex = pagination.getStartIndex();
       
        int pageSize = pagination.getPageSize();

        List<Product> findListPaging=shopService.findListPaging(startIndex,pageSize);


        for(int i=0;i<findListPaging.size();i++){

            String name="product"+i;
            model.addAttribute(name,findListPaging.get(i));

        }

        model.addAttribute("list",findListPaging);
        model.addAttribute("isok",findListPaging.size());
        model.addAttribute("page",page);


        return "shop";
    }


    @GetMapping("/category/{category}/{page}")
    public String categoryPage(Model model,@PathVariable String category,@PathVariable int page){
        int totalPrice=0;
        int totalCnt=0;

        try{
            Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("islogin",loginid);

            List<Cart> cartList=shopService.getCartListByUserID(loginid);

            for(int i=0;i<cartList.size();i++){
                Product product=shopService.getProductByCartID(cartList.get(i).getId());
                totalPrice+=cartList.get(i).getPd_count()*(product.getPd_price()-(product.getPd_price()*product.getPd_sale_percent()/100));
                totalCnt+=cartList.get(i).getPd_count();

            }



        } catch (Exception e){

        }

        model.addAttribute("totalPrice",calKrPrice(totalPrice));
        model.addAttribute("totalCnt",totalCnt);
        // 총 게시물 수
        int totalListCnt = shopService.getCategoryCnt(category);
        if(totalListCnt<9){
            model.addAttribute("n",1);
        } else if (totalListCnt>=9) {
            double howmanyPage=(double)totalListCnt/8;
            int totalpage=(int)Math.ceil(howmanyPage);
            model.addAttribute("n",totalpage);
        }

        Pagination pagination = new Pagination(totalListCnt, page);

        int startIndex = pagination.getStartIndex();

        int pageSize = pagination.getPageSize();

        List<Product> findListPaging=shopService.getCategoryPaging(category,startIndex,pageSize);
        model.addAttribute("list",findListPaging);
        String catTitle="";

        switch (category){
            case "Phone": catTitle="휴대폰";
                break;
            case "Tv":  catTitle="티비";
                break;
            case "Laptop":  catTitle="노트북";
                break;
            case "Game":   catTitle="게임기기";
            break;
        }

        model.addAttribute("catTitle",catTitle);
        model.addAttribute("isok",findListPaging.size());
        model.addAttribute("page",page);
        model.addAttribute("category",category);

        return "category";
    }


    @GetMapping("/cart")
    public String cartPage(Model model){
        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Cart> list=shopService.getCartListByUserID(id);

        int totalCnt=0;
        int totalPrice=0;

        for(int i=0;i<list.size();i++){
            totalCnt+=list.get(i).getPd_count();
            totalPrice+=list.get(i).getPd_price();
        }

        List<Product> list2=shopService.getListForRank("top");
        model.addAttribute("islogin",id);
        model.addAttribute("list2",list2);
        model.addAttribute("totalCnt",totalCnt);
        model.addAttribute("totalPrice",calKrPrice(totalPrice));
        model.addAttribute("list",list);

        return "cart";
    }


    @GetMapping("/plus/{id}")
    public String plustCart(@PathVariable int id){
        Long userid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        shopService.plustCart(userid,id);

        return "redirect:/cart";
    }

    @GetMapping("/minus/{id}")
    public String minusCart(@PathVariable int id){
        Long userid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        shopService.minusCart(userid,id);

        return "redirect:/cart";
    }


    @GetMapping("/removeCart/{id}/{count}")
    public String removeCart(@PathVariable int id,@PathVariable int count){
        Long userid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        shopService.removeCart(userid,id);
        shopService.plusInventoryStock(count,id);
        return "redirect:/cart";
    }


    @GetMapping("/minusFail")
    public String minusFailed(){

        return "cartFail";
    }



    @PostMapping("/addCart/{id}")
    public String insertCartPost(@PathVariable int id,@RequestParam int quantity ){



        Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int inventory=shopService.getpdCnt(id);

        if(inventory>=quantity){
            int isaleady=shopService.aleadyInsert(loginid,id);

            if(isaleady==0){
                shopService.insertCart(loginid,quantity,id);
                shopService.minusInventoryStock(quantity,id);
                return "cartSuccess";
            } else if(isaleady>0){
                shopService.extraCart(quantity,loginid,id);
                shopService.minusInventoryStock(quantity,id);
                return "cartSuccess";
            }
        }

        return "cartFailed";
    }


    @GetMapping("/addCart/{pd_id}/{pd_count}")
    public String insertCart(@PathVariable int pd_id, @PathVariable int pd_count){
       Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

       int inventory=shopService.getpdCnt(pd_id);

       if(inventory>=pd_count){
           int isaleady=shopService.aleadyInsert(id,pd_id);

           if(isaleady==0){
               shopService.insertCart(id,pd_count,pd_id);
               shopService.minusInventoryStock(pd_count,pd_id);
               return "cartSuccess";
           } else if(isaleady>0){
               shopService.extraCart(pd_count,id,pd_id);
               shopService.minusInventoryStock(pd_count,pd_id);
               return "cartSuccess";
           }
       }


        return "cartFailed";
    }

    @GetMapping("/searchResult")
    public String search(Model model,@RequestParam(name = "keyword", required = false) String keyword,
                         @RequestParam(name = "page", required = false) int page){

        int totalPrice=0;
        int totalCnt=0;

        try{
            Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("islogin",loginid);

            List<Cart> cartList=shopService.getCartListByUserID(loginid);

            for(int i=0;i<cartList.size();i++){
                Product product=shopService.getProductByCartID(cartList.get(i).getId());
                totalPrice+=cartList.get(i).getPd_count()*(product.getPd_price()-(product.getPd_price()*product.getPd_sale_percent()/100));
                totalCnt+=cartList.get(i).getPd_count();

            }



        } catch (Exception e){

        }

        model.addAttribute("totalPrice",calKrPrice(totalPrice));
        model.addAttribute("totalCnt",totalCnt);

        switch (keyword){
            case "휴대폰":
            case "Phone":
            case "phone":
            case "전화":
            case "스마트폰":
                return "redirect:/category/Phone/1";
            case "티비":
            case "Tv":
            case "tv":
                return "redirect:/category/Tv/1";
            case "노트북":
            case "컴퓨터":
            case "랩탑":
            case "Laptop":
            case "laptop":
                return "redirect:/category/Laptop/1";
            case "게임기기":
            case "게임기":
            case "게임":
            case "game":
                return "redirect:/category/Game/1";
            default:
                break;
        }


        try{
            int totalListCnt = shopService.searchCnt(keyword);
            if(totalListCnt<9){
                model.addAttribute("n",1);
            } else if (totalListCnt>=9) {
                double howmanyPage=(double)totalListCnt/8;
                int totalpage=(int)Math.ceil(howmanyPage);
                model.addAttribute("n",totalpage);
            }
            
            Pagination pagination = new Pagination(totalListCnt, page);

            
            int startIndex = pagination.getStartIndex();
            
            int pageSize = pagination.getPageSize();

            List<Product> findListPaging=shopService.searchKeyword(keyword,startIndex,pageSize);
            model.addAttribute("list",findListPaging);

            String htmlkeyword="'"+keyword+"'";
            model.addAttribute("htmlkeyword",htmlkeyword);
            model.addAttribute("isok",findListPaging.size());
            model.addAttribute("keyword",keyword);
            model.addAttribute("page",page);
            model.addAttribute("totalCnt",totalListCnt);

            return "search";
        } catch (Exception e){

            return "nullSearch";

        }


    }


    @GetMapping("/search/{keyword}/{page}")
    public String searchPage(Model model,@PathVariable String keyword,@PathVariable int page){
        int totalPrice=0;
        int totalCnt=0;

        try{
            Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("islogin",loginid);

            List<Cart> cartList=shopService.getCartListByUserID(loginid);

            for(int i=0;i<cartList.size();i++){
                Product product=shopService.getProductByCartID(cartList.get(i).getId());
                totalPrice+=cartList.get(i).getPd_count()*(product.getPd_price()-(product.getPd_price()*product.getPd_sale_percent()/100));
                totalCnt+=cartList.get(i).getPd_count();

            }



        } catch (Exception e){

        }

        model.addAttribute("totalPrice",calKrPrice(totalPrice));
        model.addAttribute("totalCnt",totalCnt);

        switch (keyword){
            case "휴대폰":
            case "Phone":
            case "phone":
            case "전화":
            case "스마트폰":
                return "redirect:/category/Phone/1";
            case "티비":
            case "Tv":
            case "tv":
                return "redirect:/category/Tv/1";
            case "노트북":
            case "컴퓨터":
            case "랩탑":
            case "Laptop":
            case "laptop":
                return "redirect:/category/Laptop/1";
            case "게임기기":
            case "게임기":
            case "게임":
            case "game":
                return "redirect:/category/Game/1";
            default:
                break;
        }


        try{
            int totalListCnt = shopService.searchCnt(keyword);
            if(totalListCnt<9){
                model.addAttribute("n",1);
            } else if (totalListCnt>=9) {
                double howmanyPage=(double)totalListCnt/8;
                int totalpage=(int)Math.ceil(howmanyPage);
                model.addAttribute("n",totalpage);
            }
            // 생성인자로  총 게시물 수, 현재 페이지를 전달
            Pagination pagination = new Pagination(totalListCnt, page);

            // DB select start index
            int startIndex = pagination.getStartIndex();
            // 페이지 당 보여지는 게시글의 최대 개수
            int pageSize = pagination.getPageSize();

            List<Product> findListPaging=shopService.searchKeyword(keyword,startIndex,pageSize);
            model.addAttribute("list",findListPaging);

            String htmlkeyword="'"+keyword+"'";
            model.addAttribute("htmlkeyword",htmlkeyword);
            model.addAttribute("isok",findListPaging.size());
            model.addAttribute("keyword",keyword);
            model.addAttribute("page",page);
            model.addAttribute("totalCnt",totalListCnt);

            return "search";
        } catch (Exception e){

            return "nullSearch";

        }

    }



    @GetMapping("/checkout")
    public String checkout(Model model){
        int totalPrice=0;
        int totalCnt=0;

        try{
            Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("islogin",loginid);

            List<Cart> cartList=shopService.getCartListByUserID(loginid);

            for(int i=0;i<cartList.size();i++){
                Product product=shopService.getProductByCartID(cartList.get(i).getId());
                totalPrice+=cartList.get(i).getPd_count()*(product.getPd_price()-(product.getPd_price()*product.getPd_sale_percent()/100));
                totalCnt+=cartList.get(i).getPd_count();

            }
            List<Cart> list=shopService.getCartListByUserID(loginid);
            model.addAttribute("list",list);
            UserVo user=shopService.getUserInfo(loginid);
            model.addAttribute("user",user);

        } catch (Exception e){

        }
        List<Product> list2=shopService.getListForRank("top");
        model.addAttribute("list2",list2);
        model.addAttribute("totalPrice2",calKrPrice(totalPrice));
        model.addAttribute("totalPrice",totalPrice);
        model.addAttribute("totalCnt",totalCnt);



        return "checkout";
    }


    @GetMapping("/emptyCart")
    public String emptyCart(){

        return "emptyCart";
    }




}
