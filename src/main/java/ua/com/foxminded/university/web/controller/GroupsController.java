package ua.com.foxminded.university.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.service.GroupService;

@Controller
@RequestMapping("/groups")
public class GroupsController {

    private final Logger logger = LoggerFactory.getLogger(GroupsController.class);
    private GroupService groupService;

    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping()
    public String showGroups(@ModelAttribute Group group, Model model) {
        List<Group> groups = groupService.getAll();
        logger.debug("Getting {} groups", groups.size());
        model.addAttribute("groups", groups);
        return "groups/list";
    }

    @GetMapping("/new")
    public String showCreatingNew(@ModelAttribute Group group) {
        return "groups/card";
    }

    @GetMapping("/{id}/edit")
    public String showEdit(Model model, @PathVariable("id") long id) {
        Group group = groupService.getById(id);
        model.addAttribute("group", group);
        return "groups/card";
    }

    @PostMapping()
    public String create(@ModelAttribute Group group) {
        groupService.save(group);
        return "redirect:/groups";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute Group group, @PathVariable("id") long id) {
        group.setId(id);
        groupService.update(group);
        return "redirect:/groups";
    }

}
