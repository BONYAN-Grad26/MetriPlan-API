package com.abdelaziz26.metriplate.services.goal;

import com.abdelaziz26.metriplate.dtos.goal.CreateGoalDto;
import com.abdelaziz26.metriplate.dtos.goal.GoalSummaryDto;
import com.abdelaziz26.metriplate.dtos.goal.ReadGoalDto;
import com.abdelaziz26.metriplate.dtos.goal.UpdateGoalDto;
import com.abdelaziz26.metriplate.entities.user.Goal;
import com.abdelaziz26.metriplate.entities.user.User;
import com.abdelaziz26.metriplate.repositories.GoalRepository;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Errors;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.security.SecurityContextService;
import com.abdelaziz26.metriplate.utils.mappers.GoalMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("GoalService")
@RequiredArgsConstructor

public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final GoalMapper goalMapper;
    private final SecurityContextService securityContextService;

    @Transactional
    @Override
    public Result<List<ReadGoalDto>, Error> getGoalsByUserId() {
        User user = securityContextService.getCurrentUser().orElse(null);
        if (user == null) {
            return Result.CreateErrorResult(Errors.UnauthorizedErr("U r not authorized"));
        }
        List<Goal> goals = goalRepository.findAllByUser_Id(user.getId());
        return Result.CreateSuccessResult(goals.stream().map(goalMapper::toDto).toList());
    }

    @Override
    public Result<List<GoalSummaryDto>, Error> getGoalsByUserId(Long userId) {
        List<Goal> goals = goalRepository.findAllByUser_Id(userId);
        return Result.CreateSuccessResult(goals.stream().map(goalMapper::toSummary).toList());
    }

    @Transactional
    @Override
    public Result<ReadGoalDto, Error> AddGoal(CreateGoalDto createGoalDto) {

        User user = securityContextService.getCurrentUser().orElse(null);
        if (user == null) {
            return Result.CreateErrorResult(Errors.UnauthorizedErr("U r not authorized"));
        }

        Goal goal = goalMapper.toEntity(createGoalDto, user);
        goalRepository.save(goal);

        return Result.CreateSuccessResult(goalMapper.toDto(goal));
    }

    @Transactional
    @PreAuthorize("@GoalService.isOwner(#id)")
    @Override
    public Result<ReadGoalDto, Error> UpdateGoal(Long id, UpdateGoalDto updateGoalDto) {
        User user = securityContextService.getCurrentUser().orElse(null);
        if (user == null) {
            return Result.CreateErrorResult(Errors.UnauthorizedErr("U r not authorized"));
        }

        Goal goal = goalRepository.findById(id).orElse(null);
        if (goal == null) {
            return Result.CreateErrorResult(Errors.NotFoundErr("Goal not found"));
        }

        Goal updatedGoal = goalMapper.toEntity(updateGoalDto, goal);
        updatedGoal = goalRepository.save(updatedGoal);

        return Result.CreateSuccessResult(goalMapper.toDto(updatedGoal));
    }

    @PreAuthorize("@GoalService.isOwner(#id)")
    @Override
    public Result<String, Error> DeleteGoal(Long id) {
        goalRepository.deleteById(id);
        return Result.CreateSuccessResult("Goal Deleted Successfully");
    }

    public boolean isOwner(Long goalId) {
        User user = securityContextService.getCurrentUser().orElse(null);
        if (user == null) {
            return false;
        }
        return goalRepository.existsByIdAndUser_Id(goalId, user.getId());
    }
}
