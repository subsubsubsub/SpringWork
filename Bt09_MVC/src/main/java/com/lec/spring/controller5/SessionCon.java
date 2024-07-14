package com.lec.spring.controller5;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;

@Controller
@RequestMapping("/session")
public class SessionCon {

    @RequestMapping("/list2")
    public void list(HttpSession session, Model model) {

        Enumeration<String> enumeration = session.getAttributeNames();

        StringBuffer buff = new StringBuffer();
        int i = 0;
        while (enumeration.hasMoreElements()) {
            String sessionName = enumeration.nextElement();
            String sessionValue = session.getAttribute(sessionName).toString();

            buff.append((i + 1) + "] " + sessionName + " : " + sessionValue + "<br>");
            i++;
        }
        if (i == 0) {
            buff.append(("세션안에 attribute 가 없다. <br>"));
        }
        model.addAttribute("result", buff.toString());
    }

    @RequestMapping("/create2")
    public String create(HttpSession session) {
        String sessionName, sessionValue;
        sessionName = "num1";
        sessionValue = "" + (int) (Math.random() * 100);

        session.setAttribute(sessionName, sessionValue);
        sessionName = "datetime";
        sessionValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        session.setAttribute(sessionName, sessionValue);

        return "redirect:/session/list2";
    }

    @RequestMapping("/delete2")
    public String delete(HttpSession session) {
        session.removeAttribute("num1");

        return "redirect:/session/list";
    }

    public static final String admin_id = "admin";
    public static final String admin_pw = "1234";

    @GetMapping("/login2")
    public void login(HttpSession session, Model model) {
        if (session.getAttribute("username") != null) {
            model.addAttribute("username", session.getAttribute("username"));
        }
    }

    @PostMapping("/login2")
    public String loginOk(HttpSession session, Model model, String username, String password) {
        String sessionName = "username";
        String sessionValue = username;

        if (admin_id.equalsIgnoreCase(username) && admin_pw.equals(password)) {
            session.setAttribute(sessionName, sessionValue);

            model.addAttribute("result", true);
        } else {
            session.removeAttribute(sessionName);
        }
        return "session/loginOk";
    }

    @PostMapping("/logout2")
    public String logout2(HttpSession session) {
        session.removeAttribute("username");

        return "session/logout";
    }
}
