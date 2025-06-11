package com.filmrentalfrontend.controller;

import com.filmrentalfrontend.model.entity.TeamMember;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
public class TeamController {

    private final List<TeamMember> teamMembers = Arrays.asList(
            new TeamMember(1L, "Alice Smith"),
            new TeamMember(2L, "Bob Johnson"),
            new TeamMember(3L, "Carol White"),
            new TeamMember(4L, "David Brown"),
            new TeamMember(5L, "Emma Davis"),
            new TeamMember(6L, "Frank Wilson"),
            new TeamMember(7L, "Grace Lee"),
            new TeamMember(8L, "Henry Clark")
    );

    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("teamMembers", teamMembers);
        return "index";
    }

    @GetMapping("/details/{id}")
    public String getDetails(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return switch (id.intValue()) {
            case 1 -> "redirect:/actors/details/" + id + "?page=" + page + "&size=" + size;
            case 2 -> "redirect:/films/details/" + id + "?page=" + page + "&size=" + size;
            case 3 -> "redirect:/inventory/details/" + id + "?page=" + page + "&size=" + size;
            case 4 -> "redirect:/payment/details/" + id + "?page=" + page + "&size=" + size;
            case 5 -> "redirect:/rental/details/" + id + "?page=" + page + "&size=" + size;
            case 6 -> "redirect:/staff/details/" + id + "?page=" + page + "&size=" + size;
            case 7 -> "redirect:/store/details/" + id + "?page=" + page + "&size=" + size;
            case 8 -> "redirect:/categories/details/" + id + "?page=" + page + "&size=" + size;
            default -> "pagesecond";
        };
    }
}