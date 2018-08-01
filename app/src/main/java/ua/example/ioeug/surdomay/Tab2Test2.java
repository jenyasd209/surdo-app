package ua.example.ioeug.surdomay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import ua.example.ioeug.surdomay.DB.DBHelper;

/**
 * Created by Asus on 04.06.2017.
 */

public class Tab2Test2 extends Fragment {

    private GridView gridView;
    private Button btnClear2;

    DBHelper dbHelper;
    SQLiteDatabase db;

    List<String> li;
    ArrayAdapter<String> dataAdapter;

    final String LOG_TAG = "myLogs";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_test2, container, false);

        dbHelper = new DBHelper(getActivity());

        gridView = (GridView)rootView.findViewById(R.id.grdView2);
        btnClear2 = (Button)rootView.findViewById(R.id.btnClearT2);

        btnClear2.setOnClickListener(clearTest2);

        li = new ArrayList<>();
        dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,li);
        dataAdapter.setDropDownViewResource(R.layout.tab2_test2);

        writeResult();

        return rootView;
    }

    public void writeResult(){
        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(getActivity());

        db = dbHelper.getWritableDatabase();

        Log.d(LOG_TAG, "--- Rows in mytable: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("Test2", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int no=c.getColumnIndex("_id");
            int time=c.getColumnIndex("time");
            int step=c.getColumnIndex("step");
            int answer=c.getColumnIndex("answer");
            int procent=c.getColumnIndex("procent");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
//                li.add(c.getString(no));
                li.add(c.getString(time));
                li.add(c.getString(step));
                li.add(c.getString(answer));
                li.add(c.getString(procent));
                gridView.setAdapter(dataAdapter);
                gridView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
        db.close();
    }

    View.OnClickListener clearTest2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AledrtD();
        }
    };

    public void AledrtD(){
        final AlertDialog.Builder alertbox = new AlertDialog.Builder(getActivity());

        // Заголовок и текст
        alertbox.setTitle("Вы уверены?");

        // Добавляем кнопку
        alertbox.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // закрываем текущюю Activity
                db = dbHelper.getWritableDatabase();

                Log.d(LOG_TAG, "--- Clear mytable: ---");
                // удаляем все записи
                int clearCount = db.delete("Test2", null, null);
                Log.d(LOG_TAG, "deleted rows count = " + clearCount);
                db.close();
                gridView.removeAllViewsInLayout();
                dataAdapter.clear();
            }
        });
        alertbox.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        // показываем окно
        alertbox.show();
    }
}
