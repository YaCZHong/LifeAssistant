package com.czh.life_assistant.util;

import com.czh.life_assistant.entity.FmsEntity;
import com.czh.life_assistant.entity.TodoEntity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class DBUtil {
    public static boolean saveFmsToDb(String fmsType, String fmsCode, String fmsMark) {

        List<FmsEntity> FmsList = DataSupport.where("fms_type = ? and fms_code = ?", fmsType, fmsCode).find(FmsEntity.class);
        if (FmsList.size() == 0) {
            FmsEntity fmsEntity = new FmsEntity();
            fmsEntity.setFms_type(fmsType);
            fmsEntity.setFms_code(fmsCode);
            fmsEntity.setFms_mark(fmsMark);
            fmsEntity.save();
            return true;
        } else {
            return false;
        }
    }

    public static void updateFmsToDb(int id, String fmsType, String fmsCode, String fmsMark) {

        FmsEntity fmsEntity = new FmsEntity();
        fmsEntity.setFms_type(fmsType);
        fmsEntity.setFms_code(fmsCode);
        fmsEntity.setFms_mark(fmsMark);
        fmsEntity.updateAll("id = ? ", String.valueOf(id));
    }

    public static int deleteFmsInfoInDb(int id) {
        return DataSupport.deleteAll(FmsEntity.class, "id = ?", String.valueOf(id));
    }

    public static ArrayList<FmsEntity> getFmsListFromDb() {
        ArrayList<FmsEntity> FmsList;
        FmsList = (ArrayList<FmsEntity>) DataSupport.order("id desc").find(FmsEntity.class);
        return FmsList;
    }


    public static boolean saveTodoToDb(String title, String content, String date, int isFinish) {

        List<TodoEntity> TodoList = DataSupport.where("title = ? and content = ? and date = ? and isFinish = ?", title, content, date, String.valueOf(isFinish)).find(TodoEntity.class);
        if (TodoList.size() == 0) {
            TodoEntity todoEntity = new TodoEntity();
            todoEntity.setTitle(title);
            todoEntity.setContent(content);
            todoEntity.setDate(date);
            todoEntity.setIsFinish(isFinish);
            todoEntity.save();
            return true;
        } else {
            return false;
        }
    }

    public static void updateTodoInDb(int id, String title, String content, String date, int isFinish) {
        TodoEntity todoEntity = new TodoEntity();
        todoEntity.setTitle(title);
        todoEntity.setContent(content);
        todoEntity.setDate(date);
        todoEntity.setIsFinish(isFinish);
        todoEntity.updateAll("id = ? ", String.valueOf(id));
        //Log.d(TAG, "updateTodoInDb: " + num);
    }

    public static int deleteTodoInDb(int id) {
        return DataSupport.deleteAll(TodoEntity.class, "id = ?", String.valueOf(id));
    }

    public static ArrayList<TodoEntity> getTodoListFromDb(String date, int isFinish) {
        ArrayList<TodoEntity> TodoList;
        if (isFinish == 0) {
            TodoList = (ArrayList<TodoEntity>) DataSupport.where("date= ? ", date).find(TodoEntity.class);
        } else {
            TodoList = (ArrayList<TodoEntity>) DataSupport.where("date= ? and isFinish = ?", date, String.valueOf(isFinish)).find(TodoEntity.class);
        }
        return TodoList;
    }

    public static TodoEntity getTodoFromDb(int id) {
        return DataSupport.find(TodoEntity.class, id);
    }
}
