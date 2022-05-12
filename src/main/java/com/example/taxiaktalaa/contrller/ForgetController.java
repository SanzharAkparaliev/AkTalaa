package com.example.taxiaktalaa.contrller;

import com.example.taxiaktalaa.entity.User;
import com.example.taxiaktalaa.repository.UserRepository;
import com.example.taxiaktalaa.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Random;

@Controller
public class ForgetController {
    Random random = new Random(1000);
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("/forgot")
    public String openEmail(){
        return "forgot_email_form";
    }

    @PostMapping("/send-otp")
    public String sendOTP(@RequestParam("email") String email, HttpSession session){

        System.out.println("EMAIL " + email);
        int otp = random.nextInt(999999);
        System.out.println("OTP  " + otp);

        //write code for send otp to email...
        String subject = "OTP from SCM";
        String message = ""
                + "<div style='border:1px solid #e2e2e2;padding:20px'>"
                +"<h1>"
                +"OTP is "
                +"<b>"+otp
                +"</n>"
                +"</h1>"
                +"</div>";

        String to = email;
        boolean flag =  emailService.sendEmail(subject,message,to);
        if(flag){
            session.setAttribute("myotp",otp);
            session.setAttribute("email",email);
            return "verify_otp";
        }else {
            session.setAttribute("message","Check your email id !!");
            return "forgot_email_form";
        }
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") int otp,HttpSession session){
        int myOtp = (int)session.getAttribute("myotp");
        String email = (String)session.getAttribute("email");

        if(myOtp==otp){
            //password change from
            User user = userRepository.getUserByUserName(email);
            if(user==null){
                //send error message
                session.setAttribute("message","User does not exits with email!!");
                return "forgot_email_form";
            }else
                //send change password form


            return "password_change_form";
        }else {
            session.setAttribute("message","You have entered wrong otp !!");
            return "verify_otp";
        }
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("newpassword") String newpassword,HttpSession session){
        String email = (String)session.getAttribute("email");
        User user = userRepository.getUserByUserName(email);
        user.setPassword(bCryptPasswordEncoder.encode(newpassword));
        userRepository.save(user);
        return "redirect:/singin?change=password changed successfully...";
    }
}
