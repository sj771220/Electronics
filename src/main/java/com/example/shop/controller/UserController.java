package com.example.shop.controller;

import com.example.shop.dto.*;
import com.example.shop.etc.Pagination;
import com.example.shop.service.ShopService;
import com.example.shop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ShopService shopService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/home")
    public String home(Model model) { // 인증된 사용자의 정보를 보여줌
        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // token에 저장되어 있는 인증된 사용자의 id 값

        UserVo userVo = userService.getUserById(id);
        userVo.setPassword(null); // password는 보이지 않도록 null로 설정
        model.addAttribute("user", userVo);
        return "home";
    }

    @GetMapping("/userList")
    public String getUserList(Model model) { // User 테이블의 전체 정보를 보여줌
        List<UserVo> userList = userService.getUserList();
        model.addAttribute("list", userList);
        return "userListPage";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            if (error != null) {
                model.addAttribute("error", "*아이디, 혹은 패스워드가 일치하지 않습니다.");
                model.addAttribute("iserror", 1);
            }
            return "loginPage";
        }

        return "redirect:/main";
    }

    @GetMapping("/letsignup")
    public String signup(){
        return "signup";
    }

    @PostMapping("/signupre")
    public String signupPo(@RequestParam("mName")String name,@RequestParam("mId")String userid,@RequestParam("mPwd")String pwd,@RequestParam("mTel")String phone,
                         @RequestParam("email")String mail,@RequestParam("zip_code")int postcode,@RequestParam("addr")String addr,@RequestParam("addr_dtl")String addr_dtl){

        userService.makeUser(name, userid, mail,pwd,addr,postcode, phone, addr_dtl);

        return "SignupSuccess";
    }




    @RequestMapping(value = "/checkid", method = {RequestMethod.GET})
    public @ResponseBody int idCheck(String mId) {
        if(mId == null || mId == "")
            return -1;
        else
            return userService.checkId(mId);
    }

    @RequestMapping(value = "/checktel", method = {RequestMethod.GET})
    public @ResponseBody int telCheck(String mId) {
        if(mId == null || mId == ""){
            return -1;
        }
        else
            return userService.checktel(mId);
    }

    @RequestMapping(value = "/checkmail", method = {RequestMethod.GET})
    public @ResponseBody int mailCheck(String mId) {
        if(mId == null || mId == ""){
            return -1;
        }
        else
            return userService.checkmail(mId);
    }

    @GetMapping("/orderlist/{page}")
    public String orderlist(Model model,@PathVariable int page){
        int totalPrice=0;
        int totalCnt=0;
        Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try{

            model.addAttribute("islogin",loginid);

            List<Cart> cartList=shopService.getCartListByUserID(loginid);

            for(int i=0;i<cartList.size();i++){
                Product product=shopService.getProductByCartID(cartList.get(i).getId());
                totalPrice+=cartList.get(i).getPd_count()*(product.getPd_price()-(product.getPd_price()*product.getPd_sale_percent()/100));
                totalCnt+=cartList.get(i).getPd_count();

            }



        } catch (Exception e){

        }
        model.addAttribute("totalPrice",shopService.calKrPrice(totalPrice));
        model.addAttribute("totalCnt",totalCnt);


        int totalListCnt=userService.gettotalorderct(loginid);
        if(totalListCnt<5){
            model.addAttribute("n",1);
        } else if (totalListCnt>=5) {
            double howmanyPage=(double)totalListCnt/4;
            int totalpage=(int)Math.ceil(howmanyPage);
            model.addAttribute("n",totalpage);
        }


        // DB select start index


        // 페이지 당 보여지는 게시글의 최대 개수
        int pageSize = 4;
        int startIndex = (page-1) * pageSize;


        List<OrderList>list=userService.getOrderList(loginid,startIndex,pageSize);

        for(int i=0;i<list.size();i++){

            List<OrderPd>list2=shopService.getOrderPD(list.get(i).getMerchant_uid());

            for(int j=0;j<list2.size();j++){
                list2.get(j).setPd_name(shopService.getProductNameByID(list2.get(j).getPd_id()));
                list2.get(j).setPrice(userService.getprice(list2.get(j).getPd_id()));
                list2.get(j).setKr_price(userService.calculKr(userService.getprice(list2.get(j).getPd_id())));
            }

            String pd_name=shopService.getProductNameByID(list2.get(0).getPd_id());
            list.get(i).setPdlist(list2);

            int total=0;

            for(int j=0;j<list2.size();j++){

                total+=list2.get(0).getPd_count();
                list.get(i).setMerchant_count(total);
            }

            if(list2.size()==1){
                String nameex=pd_name;
                list.get(i).setPd_nameex(nameex);
            } else {
                String nameex=pd_name +" 외 ("+(list2.size()-1)+")"+"건";
                list.get(i).setPd_nameex(nameex);
            }

        }


        model.addAttribute("list",list);



        return "orderlist";
    }


    @GetMapping("/cancelOrder/{uid}")
    public String cancelOrder(@PathVariable("uid") String uid){

        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OrderList orderList=userService.getOrderbyUid(uid);
        userService.cancelOrder(uid,id,orderList.getAmount(), LocalDate.now());
        userService.cancelOrder2("취소신청",uid);
        return "redirect:/orderlist/1";
    }



    @GetMapping("/orderlistSearch/{date}/{page}")
    public String searchpage(@PathVariable("date")String orderdate,@PathVariable("page")int page,Model model){

        int totalPrice=0;
        int totalCnt=0;
        Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try{

            model.addAttribute("islogin",loginid);

            List<Cart> cartList=shopService.getCartListByUserID(loginid);

            for(int i=0;i<cartList.size();i++){
                Product product=shopService.getProductByCartID(cartList.get(i).getId());
                totalPrice+=cartList.get(i).getPd_count()*(product.getPd_price()-(product.getPd_price()*product.getPd_sale_percent()/100));
                totalCnt+=cartList.get(i).getPd_count();

            }


        } catch (Exception e){

        }

        model.addAttribute("totalPrice",shopService.calKrPrice(totalPrice));
        model.addAttribute("totalCnt",totalCnt);


        int pageSize = 4;
        int startIndex = (page-1) * pageSize;

        List<OrderList>list=userService.getOrderSearch(loginid,orderdate,startIndex,pageSize);
        int totalListCnt=userService.getOrderSearchct(loginid,orderdate);

        if(totalListCnt<5){
            model.addAttribute("n",1);
        } else if (totalListCnt>=5) {
            double howmanyPage=(double)totalListCnt/4;
            int totalpage=(int)Math.ceil(howmanyPage);
            model.addAttribute("n",totalpage);
        }

        for(int i=0;i<list.size();i++){

            List<OrderPd>list2=shopService.getOrderPD(list.get(i).getMerchant_uid());

            for(int j=0;j<list2.size();j++){
                list2.get(j).setPd_name(shopService.getProductNameByID(list2.get(j).getPd_id()));
                list2.get(j).setPrice(userService.getprice(list2.get(j).getPd_id()));
                list2.get(j).setKr_price(userService.calculKr(userService.getprice(list2.get(j).getPd_id())));
            }

            String pd_name=shopService.getProductNameByID(list2.get(0).getPd_id());
            list.get(i).setPdlist(list2);

            int total=0;

            for(int j=0;j<list2.size();j++){

                total+=list2.get(0).getPd_count();
                list.get(i).setMerchant_count(total);
            }

            if(list2.size()==1){
                String nameex=pd_name;
                list.get(i).setPd_nameex(nameex);
            } else {
                String nameex=pd_name +" 외 ("+(list2.size()-1)+")"+"건";
                list.get(i).setPd_nameex(nameex);
            }

        }


        model.addAttribute("list",list);

        return "orderlist";


    }


    @PostMapping("/orderlist/checkpd")
    public ResponseEntity<List<String>> yourControllerMethod(@RequestParam String uid) {


        List<OrderPd>list=shopService.getOrderPD(uid);
        List<String> data = new ArrayList<>();
        for(int i=0;i<list.size();i++){

            data.add(shopService.getProductNameByID(list.get(i).getPd_id()));

        }

        return ResponseEntity.ok(data);
    }


    @PostMapping("/rating")
    public String rating(@RequestParam(value="uid")String uid,@RequestParam(value="rating",required = false)
    String rating,@RequestParam(value = "review",required = true)String review,@RequestParam(value = "ratingSelect",required = false) String pd_name,Model model) {

        Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        if(rating==null||review==null||pd_name==null){
            model.addAttribute("resultType","빈칸");
            return "reviewFailed";
        }


        int pd_id=userService.getidwithname(pd_name);

        int isaleady=userService.isaleadyrate(pd_id,loginid);

        if(isaleady==0){
            int ratenum=Integer.parseInt(rating);
            userService.insertreview(pd_id,uid,loginid,review,ratenum);
            int pd_rate=userService.getPd_rate(pd_id);
            userService.updaterate(pd_rate,pd_id);
            model.addAttribute("resultType","성공");
            return "reviewFailed";
        } else {
            model.addAttribute("resultType","이미");
            return "reviewFailed";
        }


    }


    @GetMapping("/userInfo")
    public String userInfo(Model model){

        Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("islogin",loginid);

        List<Cart> cartList=shopService.getCartListByUserID(loginid);
        int totalPrice=0;
        int totalCnt=0;

        for(int i=0;i<cartList.size();i++){
            Product product=shopService.getProductByCartID(cartList.get(i).getId());
            totalPrice+=cartList.get(i).getPd_count()*(product.getPd_price()-(product.getPd_price()*product.getPd_sale_percent()/100));
            totalCnt+=cartList.get(i).getPd_count();

        }
        UserVo userInfo=shopService.getUserInfo(loginid);
        userInfo.setPassword("proctectedpwd");
        model.addAttribute("userDto",userInfo);
        model.addAttribute("totalPrice",shopService.calKrPrice(totalPrice));
        model.addAttribute("totalCnt",totalCnt);
        return "userinfo";
    }


    @PostMapping("/passwordCh")
    public String changePwd(@RequestParam("mPwd") String pw, Model model){
        Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("updateaddr",1);
        userService.updatepwd(pw,loginid);

        return "userSuccess";
    }




    @PostMapping("/updateDefaultAdd" )
    public String updateAddr(@RequestParam("addr")String addr,@RequestParam("zip_code") int postcode,@RequestParam("addr_dtl") String addr_dtl,Model model){


        Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("islogin",loginid);


            userService.updateAddr(addr,addr_dtl,postcode,loginid);
            model.addAttribute("updateaddr",1);

            return "userSuccess";
    }



    @PostMapping("/subcribe")
    public String subcribe(@RequestParam("email")String email,Model model){

        int aleady=userService.aleadynews(email);

        if(aleady==0){
            userService.subcribenews(email);
            model.addAttribute("result","success");
        return "subcribe";
        } else {
            model.addAttribute("result","fail");
            return "subcribe";
        }

    }


}