package com.wizard.hastar.ui.money_manager.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.wizard.hastar.BuildConfig;
import com.wizard.hastar.MyApplication;
import com.wizard.hastar.ui.money_manager.db.DB;
import com.wizard.hastar.ui.money_manager.model.Record;
import com.wizard.hastar.ui.money_manager.model.Tag;
import com.wizard.hastar.util.HaStarUtil;
import com.wizard.hastar.util.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Created by 伟平 on 2015/10/20.
 */

public class RecordManager {

    private static RecordManager recordManager = null;

    private static DB db;

    // the selected values in list activity
    public static Double SELECTED_SUM;
    public static List<Record> SELECTED_RECORDS;

    public static Integer SUM;
    public static List<Record> RECORDS;
    public static List<Tag> TAGS;
    public static Map<Integer, String> TAG_NAMES;

    public static boolean RANDOM_DATA = false;
    private final int RANDOM_DATA_NUMBER_ON_EACH_DAY = 3;
    private final int RANDOM_DATA_EXPENSE_ON_EACH_DAY = 30;

    private static boolean FIRST_TIME = true;

    public static int SAVE_TAG_ERROR_DATABASE_ERROR = -1;
    public static int SAVE_TAG_ERROR_DUPLICATED_NAME = -2;

    public static int DELETE_TAG_ERROR_DATABASE_ERROR = -1;
    public static int DELETE_TAG_ERROR_TAG_REFERENCE = -2;

    // constructor//////////////////////////////////////////////////////////////////////////////////////
    private RecordManager(Context context) {
        try {
            db = db.getInstance(context);
            if (BuildConfig.DEBUG)
                if (BuildConfig.DEBUG) Log.d("CoCoin", "db.getInstance(context) S");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (FIRST_TIME) {
// if the app starts firstly, create tags///////////////////////////////////////////////////////////
            SharedPreferences preferences =
                    context.getSharedPreferences("Values", Context.MODE_PRIVATE);
            if (preferences.getBoolean("FIRST_TIME", true)) {
                createTags();
                SharedPreferences.Editor editor =
                        context.getSharedPreferences("Values", Context.MODE_PRIVATE).edit();
                editor.putBoolean("FIRST_TIME", false);
                editor.commit();
            }
        }
        if (RANDOM_DATA) {

            SharedPreferences preferences =
                    context.getSharedPreferences("Values", Context.MODE_PRIVATE);
            if (preferences.getBoolean("RANDOM", false)) {
                return;
            }

            randomDataCreater();

            SharedPreferences.Editor editor =
                    context.getSharedPreferences("Values", Context.MODE_PRIVATE).edit();
            editor.putBoolean("RANDOM", true);
            editor.commit();

        }
    }

    // getInstance//////////////////////////////////////////////////////////////////////////////////////
    public synchronized static RecordManager getInstance(Context context) {
        if (RECORDS == null || TAGS == null || TAG_NAMES == null || SUM == null || recordManager == null) {
            SUM = 0;
            RECORDS = new LinkedList<>();
            TAGS = new LinkedList<>();
            TAG_NAMES = new HashMap<>();
            recordManager = new RecordManager(context);

            db.getData();

            if (BuildConfig.DEBUG) {
                if (BuildConfig.DEBUG) Log.d("CoCoin", "Load " + RECORDS.size() + " records S");
                if (BuildConfig.DEBUG) Log.d("CoCoin", "Load " + TAGS.size() + " tags S");
            }

            TAGS.add(0, new Tag(-1, "Sum Histogram", -4));
            TAGS.add(0, new Tag(-2, "Sum Pie", -5));

            for (Tag tag : TAGS) TAG_NAMES.put(tag.getId(), tag.getName());

            sortTAGS();
        }
        return recordManager;
    }

    // saveRecord///////////////////////////////////////////////////////////////////////////////////////
    public static long saveRecord(final Record coCoinRecord) {
        long insertId = -1;
        coCoinRecord.setIsUploaded(false);
        if (BuildConfig.DEBUG)
            if (BuildConfig.DEBUG)
                Log.d("CoCoin", "recordManager.saveRecord: Save " + coCoinRecord.toString() + " S");
        insertId = db.saveRecord(coCoinRecord);
        if (insertId == -1) {
            if (BuildConfig.DEBUG)
                if (BuildConfig.DEBUG)
                    Log.d("CoCoin", "recordManager.saveRecord: Save the above coCoinRecord FAIL!");
            ToastUtil.displayShortToast(MyApplication.getAppContext(), "保存失败");
        } else {
            if (BuildConfig.DEBUG)
                if (BuildConfig.DEBUG)
                    Log.d("CoCoin", "recordManager.saveRecord: Save the above coCoinRecord SUCCESSFULLY!");
            RECORDS.add(coCoinRecord);
            SUM += (int) coCoinRecord.getMoney();
            ToastUtil.displayShortToast(MyApplication.getAppContext(), "保存成功");
        }
        return insertId;
    }

    // save tag/////////////////////////////////////////////////////////////////////////////////////////
    public static int saveTag(Tag tag) {
        int insertId = -1;
        if (BuildConfig.DEBUG) {
            if (BuildConfig.DEBUG) Log.d("CoCoin", "recordManager.saveTag: " + tag.toString());
        }
        boolean duplicatedName = false;
        for (Tag t : TAGS) {
            if (t.getName().equals(tag.getName())) {
                duplicatedName = true;
                break;
            }
        }
        if (duplicatedName) {
            return SAVE_TAG_ERROR_DUPLICATED_NAME;
        }
        insertId = db.saveTag(tag);
        if (insertId == -1) {
            if (BuildConfig.DEBUG) {
                if (BuildConfig.DEBUG) Log.d("CoCoin", "Save the above tag FAIL!");
                return SAVE_TAG_ERROR_DATABASE_ERROR;
            }
        } else {
            if (BuildConfig.DEBUG) {
                if (BuildConfig.DEBUG) Log.d("CoCoin", "Save the above tag SUCCESSFULLY!");
            }
            TAGS.add(tag);
            TAG_NAMES.put(tag.getId(), tag.getName());
            sortTAGS();
        }
        return insertId;
    }

    // delete a coCoinRecord//////////////////////////////////////////////////////////////////////////////////
    public static long deleteRecord(final Record coCoinRecord, boolean deleteInList) {
        long deletedNumber = db.deleteRecord(coCoinRecord.getId());
        if (deletedNumber > 0) {
            if (BuildConfig.DEBUG) Log.d("CoCoin",
                    "recordManager.deleteRecord: Delete " + coCoinRecord.toString() + " S");
            ToastUtil.displayShortToast(MyApplication.getAppContext(), "删除成功");
            // update RECORDS list and SUM
            SUM -= (int) coCoinRecord.getMoney();
            if (deleteInList) {
                int size = RECORDS.size();
                for (int i = 0; i < RECORDS.size(); i++) {
                    if (RECORDS.get(i).getId() == coCoinRecord.getId()) {
                        RECORDS.remove(i);
                        if (BuildConfig.DEBUG) Log.d("CoCoin",
                                "recordManager.deleteRecord: Delete in RECORD " + coCoinRecord.toString() + " S");
                        break;
                    }
                }
            }
        } else {
            if (BuildConfig.DEBUG) Log.d("CoCoin",
                    "recordManager.deleteRecord: Delete " + coCoinRecord.toString() + " F");
            ToastUtil.displayShortToast(MyApplication.getAppContext(), "删除失败");
        }


        return coCoinRecord.getId();
    }


    private static int p;

    public static long updateRecord(final Record coCoinRecord) {
        long updateNumber = db.updateRecord(coCoinRecord);
        if (updateNumber <= 0) {
            if (BuildConfig.DEBUG) {
                if (BuildConfig.DEBUG)
                    Log.d("CoCoin", "recordManager.updateRecord " + coCoinRecord.toString() + " F");
            }
            ToastUtil.displayShortToast(MyApplication.getAppContext(), "修改成功");
        } else {
            if (BuildConfig.DEBUG) {
                if (BuildConfig.DEBUG)
                    Log.d("CoCoin", "recordManager.updateRecord " + coCoinRecord.toString() + " S");
            }
            p = RECORDS.size() - 1;
            for (; p >= 0; p--) {
                if (RECORDS.get(p).getId() == coCoinRecord.getId()) {
                    SUM -= (int) RECORDS.get(p).getMoney();
                    SUM += (int) coCoinRecord.getMoney();
                    RECORDS.get(p).set(coCoinRecord);
                    break;
                }
            }
            coCoinRecord.setIsUploaded(false);
            db.updateRecord(coCoinRecord);
            ToastUtil.displayShortToast(MyApplication.getAppContext(), "修改成功");
        }
        return updateNumber;
    }


    public static long updateTag(Tag tag) {
        int updateId = -1;
        if (BuildConfig.DEBUG) Log.d("CoCoin",
                "Manager: Update tag: " + tag.toString());
        updateId = db.updateTag(tag);
        if (updateId == -1) {
            if (BuildConfig.DEBUG) Log.d("CoCoin", "Update the above tag FAIL!");
        } else {
            if (BuildConfig.DEBUG)
                Log.d("CoCoin", "Update the above tag SUCCESSFULLY!" + " - " + updateId);
            for (Tag t : TAGS) {
                if (t.getId() == tag.getId()) {
                    t.set(tag);
                    break;
                }
            }
            sortTAGS();
        }
        return updateId;
    }


    public static int getCurrentMonthExpense() {
        Calendar calendar = Calendar.getInstance();
        Calendar left = HaStarUtil.GetThisMonthLeftRange(calendar);
        int monthSum = 0;
        for (int i = RECORDS.size() - 1; i >= 0; i--) {
            if (RECORDS.get(i).getCalendar().before(left)) break;
            monthSum += RECORDS.get(i).getMoney();
        }
        return monthSum;
    }

    public static List<Record> queryRecordByTime(Calendar c1, Calendar c2) {
        List<Record> list = new LinkedList<>();
        for (Record coCoinRecord : RECORDS) {
            if (coCoinRecord.isInTime(c1, c2)) {
                list.add(coCoinRecord);
            }
        }
        return list;
    }

    public static List<Record> queryRecordByCurrency(String currency) {
        List<Record> list = new LinkedList<>();
        for (Record coCoinRecord : RECORDS) {
            if (coCoinRecord.getCurrency().equals(currency)) {
                list.add(coCoinRecord);
            }
        }
        return list;
    }

    public static List<Record> queryRecordByTag(int tag) {
        List<Record> list = new LinkedList<>();
        for (Record coCoinRecord : RECORDS) {
            if (coCoinRecord.getTag() == tag) {
                list.add(coCoinRecord);
            }
        }
        return list;
    }

    public static List<Record> queryRecordByMoney(double money1, double money2, String currency) {
        List<Record> list = new LinkedList<>();
        for (Record coCoinRecord : RECORDS) {
            if (coCoinRecord.isInMoney(money1, money2, currency)) {
                list.add(coCoinRecord);
            }
        }
        return list;
    }

    public static List<Record> queryRecordByRemark(String remark) {
        List<Record> list = new LinkedList<>();
        for (Record coCoinRecord : RECORDS) {
            if (HaStarUtil.IsStringRelation(coCoinRecord.getRemark(), remark)) {
                list.add(coCoinRecord);
            }
        }
        return list;
    }

    private void createTags() {
        saveTag(new Tag(-1, "Meal", -1));
        saveTag(new Tag(-1, "Clothing & Footwear", 1));
        saveTag(new Tag(-1, "Home", 2));
        saveTag(new Tag(-1, "Traffic", 3));
        saveTag(new Tag(-1, "Vehicle Maintenance", 4));
        saveTag(new Tag(-1, "Book", 5));
        saveTag(new Tag(-1, "Hobby", 6));
        saveTag(new Tag(-1, "Internet", 7));
        saveTag(new Tag(-1, "Friend", 8));
        saveTag(new Tag(-1, "Education", 9));
        saveTag(new Tag(-1, "Entertainment", 10));
        saveTag(new Tag(-1, "Medical", 11));
        saveTag(new Tag(-1, "Insurance", 12));
        saveTag(new Tag(-1, "Donation", 13));
        saveTag(new Tag(-1, "Sport", 14));
        saveTag(new Tag(-1, "Snack", 15));
        saveTag(new Tag(-1, "Music", 16));
        saveTag(new Tag(-1, "Fund", 17));
        saveTag(new Tag(-1, "Drink", 18));
        saveTag(new Tag(-1, "Fruit", 19));
        saveTag(new Tag(-1, "Film", 20));
        saveTag(new Tag(-1, "Baby", 21));
        saveTag(new Tag(-1, "Partner", 22));
        saveTag(new Tag(-1, "Housing Loan", 23));
        saveTag(new Tag(-1, "Pet", 24));
        saveTag(new Tag(-1, "Telephone Bill", 25));
        saveTag(new Tag(-1, "Travel", 26));
        saveTag(new Tag(-1, "Lunch", -2));
        saveTag(new Tag(-1, "Breakfast", -3));
        saveTag(new Tag(-1, "MidnightSnack", 0));
        sortTAGS();
    }

    private void randomDataCreater() {

        Random random = new Random();

        List<Record> createdCoCoinRecords = new ArrayList<>();

        Calendar now = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        c.set(2015, 0, 1, 0, 0, 0);
        c.add(Calendar.SECOND, 1);

        while (c.before(now)) {
            for (int i = 0; i < RANDOM_DATA_NUMBER_ON_EACH_DAY; i++) {
                Calendar r = (Calendar) c.clone();
                int hour = random.nextInt(24);
                int minute = random.nextInt(60);
                int second = random.nextInt(60);

                r.set(Calendar.HOUR_OF_DAY, hour);
                r.set(Calendar.MINUTE, minute);
                r.set(Calendar.SECOND, second);
                r.add(Calendar.SECOND, 0);

                int tag = random.nextInt(TAGS.size());
                int expense = random.nextInt(RANDOM_DATA_EXPENSE_ON_EACH_DAY) + 1;

                Record coCoinRecord = new Record();
                coCoinRecord.setCalendar(r);
                coCoinRecord.setMoney(expense);
                coCoinRecord.setTag(tag);
                coCoinRecord.setCurrency("RMB");
                coCoinRecord.setRemark("备注：这里显示备注~");

                createdCoCoinRecords.add(coCoinRecord);
            }
            c.add(Calendar.DATE, 1);
        }

        Collections.sort(createdCoCoinRecords, new Comparator<Record>() {
            @Override
            public int compare(Record lhs, Record rhs) {
                if (lhs.getCalendar().before(rhs.getCalendar())) {
                    return -1;
                } else if (lhs.getCalendar().after(rhs.getCalendar())) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        for (Record coCoinRecord : createdCoCoinRecords) {
            saveRecord(coCoinRecord);
        }
    }

    // Todo bug here
    private static void sortTAGS() {
        Collections.sort(TAGS, new Comparator<Tag>() {
            @Override
            public int compare(Tag lhs, Tag rhs) {
                if (lhs.getWeight() != rhs.getWeight()) {
                    return Integer.valueOf(lhs.getWeight()).compareTo(rhs.getWeight());
                } else if (!lhs.getName().equals(rhs.getName())) {
                    return lhs.getName().compareTo(rhs.getName());
                } else {
                    return Integer.valueOf(lhs.getId()).compareTo(rhs.getId());
                }
            }
        });
    }

}
