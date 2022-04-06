package cn.happymaya.kubeblog.controller.admin;

import cn.happymaya.kubeblog.po.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/admin")
public class TypeController {
    @Autowired
    private cn.happymaya.kubeblog.service.ITypeService ITypeService;

    @GetMapping("/types")
    public String types(@PageableDefault(size = 3,sort = {"id"},direction = Sort.Direction.DESC)
                                Pageable pageable, Model model) {
        model.addAttribute("page", ITypeService.listType(pageable));
        return "admin/types";
    }

    @GetMapping("/types/input")
    public String input(Model model) {
        model.addAttribute("type", new Type());
        return "admin/types-input";
    }

    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("type", ITypeService.getType(id));
        return "admin/types-input";
    }


    @PostMapping("/types")
    public String post(@Valid Type type,BindingResult result, RedirectAttributes attributes) {
        Type type1 = ITypeService.getTypeByName(type.getName());
        if (type1 != null) {
            result.rejectValue("name","nameError","Cannot add duplicate types");
        }
        //校验没通过
        if (result.hasErrors()) {
            return "admin/types-input";
        }
        Type t = ITypeService.saveType(type);
        if (t == null ) {
            attributes.addFlashAttribute("message", "Add failed");
        } else {
            attributes.addFlashAttribute("message", "Add successfully");
        }
        return "redirect:/admin/types";
    }


    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type, BindingResult result,@PathVariable Long id, RedirectAttributes attributes) {
        Type type1 = ITypeService.getTypeByName(type.getName());
        if (type1 != null) {
            result.rejectValue("name","nameError","Cannot add duplicate types");
        }
        //校验没通过
        if (result.hasErrors()) {
            return "admin/types-input";
        }
        Type t = ITypeService.updateType(id,type);
        if (t == null ) {
            attributes.addFlashAttribute("message", "Update failed");
        } else {
            attributes.addFlashAttribute("message", "Update successfully");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes) {
        ITypeService.deleteType(id);
        attributes.addFlashAttribute("message", "Delete successfully");
        return "redirect:/admin/types";
    }

}
