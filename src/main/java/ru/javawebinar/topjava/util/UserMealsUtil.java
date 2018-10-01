package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
//        .toLocalDate();
//        .toLocalTime();
        System.out.println(getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000));
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return getUserMealsWithExceededFromUserMeals(getWithExceed(mealList));
    }

    private static Map<List<UserMeal>, Boolean> getWithExceed(List<UserMeal> meals) {
        Map<List<UserMeal>, Boolean> result = new HashMap<>();
        meals.sort(Comparator.comparing(UserMeal::getDateTime));
        int sum = 0;
        LocalDate currDate = meals.get(0).getDateTime().toLocalDate();

        List<UserMeal> tmp = new ArrayList<>();
        for (int i = 0; i < meals.size(); i++) {
            if (currDate.equals(meals.get(i).getDateTime().toLocalDate())) {
                sum = sum + meals.get(i).getCalories();
                tmp.add(meals.get(i));
                if (i == meals.size()-1) {
                    result.put(tmp, sum > 2000);
                }
            } else {
                currDate = meals.get(i).getDateTime().toLocalDate();
                result.put(tmp, sum > 2000);
                sum = meals.get(i).getCalories();
                tmp = new ArrayList<>();
                tmp.add(meals.get(i));
            }
        }
        return result;
    }

    private static List<UserMealWithExceed> getUserMealsWithExceededFromUserMeals(Map<List<UserMeal>, Boolean> map) {
        List<UserMealWithExceed> result = new ArrayList<>();
        List<UserMeal> meals = new ArrayList<>();
        for (Map.Entry<List<UserMeal>, Boolean> entry: map.entrySet()) {
            if (entry.getValue().equals(true)) {
                meals.addAll(entry.getKey());
            }
        }
        for (UserMeal m: meals) {
            result.add(new UserMealWithExceed(m.getDateTime(), m.getDescription(), m.getCalories(), true));
        }
        return result;
    }
}
