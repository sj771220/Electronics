package com.example.shop.controller;

import com.example.shop.dto.*;
import com.example.shop.etc.Pagination;
import com.example.shop.service.ShopService;
import com.example.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Controller
public class MainController {

    @Autowired
    ShopService shopService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String goindex(){
        return "redirect:/main";
    }

    public String calKrPrice(int totalprice){
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);

        // 숫자를 형식화하여 문자열로 변환
        String formattedAmount = numberFormat.format(totalprice);

        return formattedAmount;
    }

    @GetMapping("/main")
    public String mainpage(Model model){
        int totalprice=0;
        int totalcnt=0;

        try{
           Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("islogin",loginid);

            List<Cart> cartList=shopService.showCart2(loginid);

            for(int i=0;i<cartList.size();i++){
                Product product=shopService.showCart1(cartList.get(i).getId());
                totalprice+=cartList.get(i).getPd_count()*(product.getPd_price()-(product.getPd_price()*product.getPd_sale_percent()/100));
                totalcnt+=cartList.get(i).getPd_count();

            }



        } catch (Exception e){

        }

        model.addAttribute("totalprice",calKrPrice(totalprice));
        model.addAttribute("totalcnt",totalcnt);

        List<Product> list=shopService.gettest();
        List<Product> toplist=shopService.getlist("top");
        List<Product> viewlist=shopService.getlist("view");
        List<Product> ratinglist=shopService.getlist("rating");

        model.addAttribute("toplist",toplist);
        model.addAttribute("viewlist",viewlist);
        model.addAttribute("ratinglist",ratinglist);
        model.addAttribute("product1",list.get(0));
        model.addAttribute("product2",list.get(1));
        model.addAttribute("product3",list.get(2));
        model.addAttribute("product4",list.get(3));
        model.addAttribute("product5",list.get(4));

        return "index";
    }

    @GetMapping("/single/{id}")
    public String single(@PathVariable int id, Model model){
        int totalprice=0;
        int totalcnt=0;

        try{
            Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("islogin",loginid);

            List<Cart> cartList=shopService.showCart2(loginid);

            for(int i=0;i<cartList.size();i++){
                Product product=shopService.showCart1(cartList.get(i).getId());
                totalprice+=cartList.get(i).getPd_count()*(product.getPd_price()-(product.getPd_price()*product.getPd_sale_percent()/100));
                totalcnt+=cartList.get(i).getPd_count();

            }



        } catch (Exception e){

        }


        List<Review> reviewlist=userService.getSingleReview(id);

        for(int i=0;i<reviewlist.size();i++){
            String userid=userService.maskString(userService.getUseridWithLoginid(Long.valueOf(reviewlist.get(i).getId())));
            reviewlist.get(i).setId(userid);

        }


        shopService.updateView(id);
        model.addAttribute("reviewlist",reviewlist);
        model.addAttribute("totalprice",calKrPrice(totalprice));
        model.addAttribute("totalcnt",totalcnt);
        Product thisProduct=shopService.getProduct(id);
        thisProduct.setPd_newprice(thisProduct.getPd_price()-(thisProduct.getPd_price()*thisProduct.getPd_sale_percent()/100));
        List<Product>categoryList=shopService.getCategoryList(thisProduct.getCategory());


        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);

        // 숫자를 형식화하여 문자열로 변환
        String formattedAmount = numberFormat.format(thisProduct.getPd_newprice());
        thisProduct.setKr_price(formattedAmount);

        model.addAttribute("product",thisProduct);
        model.addAttribute("list",categoryList);
        return "single-product";
    }


    @GetMapping("/shop/{page}")
    public String home(Model model, @PathVariable int page) {
        int totalprice=0;
        int totalcnt=0;

        try{
            Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("islogin",loginid);

            List<Cart> cartList=shopService.showCart2(loginid);

            for(int i=0;i<cartList.size();i++){
                Product product=shopService.showCart1(cartList.get(i).getId());
                totalprice+=cartList.get(i).getPd_count()*(product.getPd_price()-(product.getPd_price()*product.getPd_sale_percent()/100));
                totalcnt+=cartList.get(i).getPd_count();

            }



        } catch (Exception e){

        }

        model.addAttribute("totalprice",calKrPrice(totalprice));
        model.addAttribute("totalcnt",totalcnt);
        // 총 게시물 수
        int totalListCnt = shopService.getCnt();



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
        int totalprice=0;
        int totalcnt=0;

        try{
            Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("islogin",loginid);

            List<Cart> cartList=shopService.showCart2(loginid);

            for(int i=0;i<cartList.size();i++){
                Product product=shopService.showCart1(cartList.get(i).getId());
                totalprice+=cartList.get(i).getPd_count()*(product.getPd_price()-(product.getPd_price()*product.getPd_sale_percent()/100));
                totalcnt+=cartList.get(i).getPd_count();

            }



        } catch (Exception e){

        }

        model.addAttribute("totalprice",calKrPrice(totalprice));
        model.addAttribute("totalcnt",totalcnt);
        // 총 게시물 수
        int totalListCnt = shopService.getCategoryCnt(category);
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

        List<Cart> list=shopService.showCart2(id);

        int totalcnt=0;
        int totalprice=0;

        for(int i=0;i<list.size();i++){
            totalcnt+=list.get(i).getPd_count();
            totalprice+=list.get(i).getPd_price();
        }

        List<Product> list2=shopService.getlist("top");
        model.addAttribute("islogin",id);
        model.addAttribute("list2",list2);
        model.addAttribute("totalcnt",totalcnt);
        model.addAttribute("totalprice",calKrPrice(totalprice));
        model.addAttribute("list",list);

        return "cart";
    }


    @GetMapping("/plus/{id}")
    public String pluscart(@PathVariable int id){
        Long userid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        shopService.pluscart(userid,id);

        return "redirect:/cart";
    }

    @GetMapping("/minus/{id}")
    public String minuscart(@PathVariable int id){
        Long userid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        shopService.minuscart(userid,id);

        return "redirect:/cart";
    }


    @GetMapping("/removeCart/{id}/{count}")
    public String removeCart(@PathVariable int id,@PathVariable int count){
        Long userid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        shopService.removeCart(userid,id);
        shopService.inventoryCart2(count,id);
        return "redirect:/cart";
    }


    @GetMapping("/minusfail")
    public String minusfailed(){

        return "easyname";
    }



    @PostMapping("/addcart/{id}")
    public String insertCartPost(@PathVariable int id,@RequestParam int quantity ){



        Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int inventory=shopService.getpdCnt(id);

        if(inventory>=quantity){
            int isaleady=shopService.aleadyInsert(loginid,id);

            if(isaleady==0){
                shopService.insertCart(loginid,quantity,id);
                shopService.inventoryCart1(quantity,id);
                return "cartSuccess";
            } else if(isaleady>0){
                shopService.extraCart(quantity,loginid,id);
                shopService.inventoryCart1(quantity,id);
                return "cartSuccess";
            }
        }

        return "cartFailed";
    }


    @GetMapping("/addcart/{pd_id}/{pd_count}")
    public String insertCart(@PathVariable int pd_id, @PathVariable int pd_count){
       Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

       int inventory=shopService.getpdCnt(pd_id);

       if(inventory>=pd_count){
           int isaleady=shopService.aleadyInsert(id,pd_id);

           if(isaleady==0){
               shopService.insertCart(id,pd_count,pd_id);
               shopService.inventoryCart1(pd_count,pd_id);
               return "cartSuccess";
           } else if(isaleady>0){
               shopService.extraCart(pd_count,id,pd_id);
               shopService.inventoryCart1(pd_count,pd_id);
               return "cartSuccess";
           }
       }


        return "cartFailed";
    }

    @GetMapping("/seesa")
    public String search(Model model,@RequestParam(name = "keyword", required = false) String keyword,
                         @RequestParam(name = "page", required = false) int page){

        int totalprice=0;
        int totalcnt=0;

        try{
            Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("islogin",loginid);

            List<Cart> cartList=shopService.showCart2(loginid);

            for(int i=0;i<cartList.size();i++){
                Product product=shopService.showCart1(cartList.get(i).getId());
                totalprice+=cartList.get(i).getPd_count()*(product.getPd_price()-(product.getPd_price()*product.getPd_sale_percent()/100));
                totalcnt+=cartList.get(i).getPd_count();

            }



        } catch (Exception e){

        }

        model.addAttribute("totalprice",calKrPrice(totalprice));
        model.addAttribute("totalcnt",totalcnt);

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

            String htmlkeyward="'"+keyword+"'";
            model.addAttribute("htmlkeyward",htmlkeyward);
            model.addAttribute("isok",findListPaging.size());
            model.addAttribute("keyward",keyword);
            model.addAttribute("page",page);
            model.addAttribute("totalCnt",totalListCnt);

            return "search";
        } catch (Exception e){

            return "nullSearch";

        }


    }


    @GetMapping("/search/{keyward}/{page}")
    public String searchPage(Model model,@PathVariable String keyward,@PathVariable int page){
        int totalprice=0;
        int totalcnt=0;

        try{
            Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("islogin",loginid);

            List<Cart> cartList=shopService.showCart2(loginid);

            for(int i=0;i<cartList.size();i++){
                Product product=shopService.showCart1(cartList.get(i).getId());
                totalprice+=cartList.get(i).getPd_count()*(product.getPd_price()-(product.getPd_price()*product.getPd_sale_percent()/100));
                totalcnt+=cartList.get(i).getPd_count();

            }



        } catch (Exception e){

        }

        model.addAttribute("totalprice",calKrPrice(totalprice));
        model.addAttribute("totalcnt",totalcnt);

        switch (keyward){
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
            int totalListCnt = shopService.searchCnt(keyward);
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

            List<Product> findListPaging=shopService.searchKeyword(keyward,startIndex,pageSize);
            model.addAttribute("list",findListPaging);

            String htmlkeyward="'"+keyward+"'";
            model.addAttribute("htmlkeyward",htmlkeyward);
            model.addAttribute("isok",findListPaging.size());
            model.addAttribute("keyward",keyward);
            model.addAttribute("page",page);
            model.addAttribute("totalCnt",totalListCnt);

            return "search";
        } catch (Exception e){

            return "nullSearch";

        }

    }



    @GetMapping("/checkout")
    public String checkout(Model model){
        int totalprice=0;
        int totalcnt=0;

        try{
            Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("islogin",loginid);

            List<Cart> cartList=shopService.showCart2(loginid);

            for(int i=0;i<cartList.size();i++){
                Product product=shopService.showCart1(cartList.get(i).getId());
                totalprice+=cartList.get(i).getPd_count()*(product.getPd_price()-(product.getPd_price()*product.getPd_sale_percent()/100));
                totalcnt+=cartList.get(i).getPd_count();

            }
            List<Cart> list=shopService.showCart2(loginid);
            model.addAttribute("list",list);
            UserVo user=shopService.getUserInfo(loginid);
            model.addAttribute("user",user);

        } catch (Exception e){

        }
        List<Product> list2=shopService.getlist("top");
        model.addAttribute("list2",list2);
        model.addAttribute("totalprice2",calKrPrice(totalprice));
        model.addAttribute("totalprice",totalprice);
        model.addAttribute("totalcnt",totalcnt);



        return "checkout";
    }


    @GetMapping("/emptyCart")
    public String emptyCart(){

        return "emptyCart";
    }



}
