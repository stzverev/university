package ua.com.foxminded.university.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.service.GroupService;
import ua.com.foxminded.university.web.dto.GroupDto;
import ua.com.foxminded.university.web.mapper.GroupMapper;

@RestController
@RequestMapping("/groups-rest")
@Tag(name = "Groups controller", description = "This conroller for managing groups")
@RequiredArgsConstructor
public class GroupsRestController {

    private final GroupService groupService;
    private final GroupMapper groupMapper;

    @GetMapping
    @Operation(description = "Returns groups depending on parameters 'limit' and 'offset'.")
    public List<GroupDto> getGroups(@RequestParam int limit, @RequestParam int offset) {
        Sort sortByName = Sort.by("name").descending();
        PageRequest pageRequest = PageRequest.of(offset, limit, sortByName);
        return groupService.findAll(pageRequest)
                .stream()
                .map(groupMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(description = "Returns a group by id.")
    public GroupDto getGroup(@PathVariable long id) {
        Group group = groupService.findById(id);
        return groupMapper.toDto(group);
    }

    @PatchMapping
    @Operation(description = "Update a group. Group id must not be empty.")
    public void update(@RequestBody @Valid GroupDto group) {
        groupService.save(groupMapper.toEntity(group));
    }

    @PostMapping
    @Operation(description = "Create new group")
    public void create(@RequestBody @Valid GroupDto group) {
        update(group);
    }

    @DeleteMapping
    @Operation(description = "Delete group by id")
    public void delete(@RequestParam long id) {
        groupService.deleteById(id);
    }

}
