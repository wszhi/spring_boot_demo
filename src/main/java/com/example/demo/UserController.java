package com.example.demo;

import com.example.demo.entity.Department;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @RequestMapping("/")
    public String welcome(Map<String, Object> model) {
        model.put("users", userRepository.findAll());
        return "home";
    }

    @RequestMapping("/new")
    public ModelAndView create(ModelMap model) {
        String[] photo = {"/images/user/photo.jpg"};
        model.addAttribute(photo);
        return new ModelAndView("user/new");
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(User user,Map<String, Object> model) {
        user.setCreatedate(new Date());
        userRepository.save(user);
        logger.info("save user:" + user.getId());
        model.put("users", userRepository.findAll());
        return "home";
    }

    @RequestMapping(value = "/{id}")
    public ModelAndView show(ModelMap model, @PathVariable Long id) {
        User user = userRepository.getOne(id);
        model.addAttribute(user);
        return new ModelAndView("user/show");
    }

    @RequestMapping(value = "/edit/{id}")
    public ModelAndView update(ModelMap model, @PathVariable Long id) {
        User user = userRepository.getOne(id);
        List<Role> roles = roleRepository.findAll();
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute(user);
        model.addAttribute(roles);
        model.addAttribute(departments);
        return new ModelAndView("user/edit");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(User user, HttpServletRequest request) {

        User currentUser = userRepository.getOne(user.getId());
        String department = request.getParameter("department");
        currentUser.setDepartment(new Department(department));
        userRepository.save(user);
        logger.info("update user:" + user.getId());
        return "home";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable Long id,Map<String, Object> model) {
        User currentUser = userRepository.getOne(id);
        userRepository.delete(currentUser);
        logger.info("delete user:" + id);
        model.put("users", userRepository.findAll());
        return "home";
    }

    @RequestMapping(value = "/list")
    public ResponseEntity list(HttpServletRequest request) {
        String page = request.getParameter("page");
        String size = request.getParameter("size");

        Pageable pageable = new PageRequest(page == null ? 0 : Integer.parseInt(page),
                size == null ? 10 : Integer.parseInt(size),
                new Sort(Sort.Direction.DESC, "id"));

        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll(pageable));

    }

}
