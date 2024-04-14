package parking.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


import java.util.List;

@Controller
public class UserController
{


        @GetMapping("/user.html")
        public String user(){
            return "user";
        }

         @GetMapping("/loginemail.html")
         public String loginemail(){return "loginemail";}


        @GetMapping("/signinemail.html")
        public String signinemail(){return "signinemail";}

        @GetMapping("/bnm.html")
        public String bnm(){
        return "bnm";
    }

        @GetMapping("/slots.html")
        public String slots(){
        return "slots";
    }

        @GetMapping("/")
        public String home(){return "user";}
        @GetMapping("/admin.html")
        public String admin(){
            return "admin";
        }

    }

