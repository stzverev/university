package ua.com.foxminded.university.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.service.GroupService;
import ua.com.foxminded.university.web.dto.GroupDto;
import ua.com.foxminded.university.web.mapper.GroupMapper;

@RestController
@RequestMapping("/groups-rest")
public class GroupsRestController {

    private GroupService groupService;
    private GroupMapper groupMapper;

    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    @Autowired
    public void setGroupMapper(GroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }

    @GetMapping
    public List<GroupDto> getGroups(@RequestParam int limit, @RequestParam int offset) {
        Sort sortByName = Sort.by("name").descending();
        PageRequest pageRequest = PageRequest.of(offset, limit, sortByName);
        return groupService.findAll(pageRequest)
                .stream()
                .map(groupMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public GroupDto getGroup(@PathVariable long id) {
        Group group = groupService.findById(id);
        return groupMapper.toDto(group);
    }

    @PatchMapping
    public void update(@RequestBody @Valid GroupDto group) {
        groupService.save(groupMapper.toEntity(group));
    }

    @PostMapping
    public void create(@RequestBody @Valid GroupDto group) {
        update(group);
    }

    @DeleteMapping
    public void delete(@RequestParam long id) {
        groupService.deleteById(id);
    }

}
