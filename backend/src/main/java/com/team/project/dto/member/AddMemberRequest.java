package com.team.project.dto.member;

/** roleInProject is "member" or "visitor"; defaults to "member" when null. */
public record AddMemberRequest(Integer userId, String roleInProject) {}
